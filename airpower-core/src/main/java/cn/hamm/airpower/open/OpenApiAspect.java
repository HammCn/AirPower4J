package cn.hamm.airpower.open;

import cn.hamm.airpower.enums.ServiceError;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.model.Json;
import cn.hamm.airpower.util.Utils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <h1>OpenApi切面</h1>
 *
 * @author Hamm.cn
 */
@Aspect
@Component
public class OpenApiAspect<S extends IOpenAppService, LS extends IOpenLogService> {
    @Autowired(required = false)
    S openAppService;

    @Autowired(required = false)
    LS openLogService;

    @SuppressWarnings("EmptyMethod")
    @Pointcut("@annotation(cn.hamm.airpower.open.OpenApi)")
    public void pointCut() {

    }

    /**
     * <h2>开放API</h2>
     */
    @Around("pointCut()")
    public Object openApi(@NotNull ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        OpenApi openApi = method.getAnnotation(OpenApi.class);
        ServiceError.API_SERVICE_UNSUPPORTED.whenNull(openApi);
        Long openLogId = null;
        String response = "";
        if (args.length != 1) {
            throw new ServiceException("OpenApi必须接收一个参数");
        }
        if (!(args[0] instanceof OpenRequest openRequest)) {
            throw new ServiceException("OpenApi必须接收一个OpenRequest参数");
        }
        try {
            ServiceError.INVALID_APP_KEY.when(!StringUtils.hasText(openRequest.getAppKey()));
            ServiceError.SERVICE_ERROR.whenNull(openAppService, "注入OpenAppService失败");
            IOpenApp openApp = openAppService.getByAppKey(openRequest.getAppKey());
            ServiceError.INVALID_APP_KEY.whenNull(openApp);
            openRequest.setOpenApp(openApp);
            Object object = proceedingJoinPoint.proceed();
            openLogId = addOpenLog(openRequest.getOpenApp(), Utils.getRequest().getRequestURI(), openRequest.decodeContent());
            openRequest.checkSignature();
            if (object instanceof Json json) {
                // 日志记录原始数据
                response = Json.toString(json);
                updateLogResponse(openLogId, response);
                // 如果是Json 需要将 Json.data 对输出的数据进行加密
                json.setData(OpenResponse.encodeResponse(openRequest.getOpenApp(), json.getData()));
            }
            updateLogResponse(openLogId, response);
            return object;
        } catch (ServiceException serviceException) {
            response = Json.toString(Json.create().setCode(serviceException.getCode()).setMessage(serviceException.getMessage()));
            updateLogResponse(openLogId, response);
            throw serviceException;
        } catch (Exception exception) {
            updateExceptionResponse(openLogId, exception);
            throw exception;
        }
    }

    /**
     * <h2>添加日志</h2>
     *
     * @param openApp     OpenApp
     * @param url         请求URL
     * @param requestBody 请求数据
     * @return 日志ID
     */
    private @Nullable Long addOpenLog(IOpenApp openApp, String url, String requestBody) {
        if (Objects.nonNull(openLogService)) {
            return openLogService.addRequest(openApp, url, requestBody);
        }
        return null;
    }

    /**
     * <h2>更新日志返回数据</h2>
     *
     * @param openLogId    日志ID
     * @param responseBody 返回值
     */
    private void updateLogResponse(Long openLogId, String responseBody) {
        if (Objects.isNull(openLogId) || Objects.isNull(openLogService)) {
            return;
        }
        openLogService.updateResponse(openLogId, responseBody);
    }

    /**
     * +<h2>更新日志异常</h2>
     *
     * @param openLogId 日志ID
     * @param exception 异常
     */
    private void updateExceptionResponse(Long openLogId, Exception exception) {
        if (Objects.isNull(openLogId)) {
            return;
        }
        updateLogResponse(openLogId, Json.toString(Json.create().setMessage(exception.getMessage())));
    }
}