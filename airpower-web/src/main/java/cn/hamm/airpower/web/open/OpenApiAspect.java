package cn.hamm.airpower.web.open;

import cn.hamm.airpower.core.exception.ServiceException;
import cn.hamm.airpower.core.model.Json;
import jakarta.servlet.http.HttpServletRequest;
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

import static cn.hamm.airpower.core.exception.ServiceError.*;

/**
 * <h1>{@code OpenApi} 切面</h1>
 *
 * @author Hamm.cn
 */
@Aspect
@Component
public class OpenApiAspect<S extends IOpenAppService, LS extends IOpenLogService> {
    @Autowired(required = false)
    private S openAppService;

    @Autowired(required = false)
    private LS openLogService;

    @Autowired
    private HttpServletRequest request;

    /**
     * <h3>验证切面点是否支持OpenApi</h3>
     *
     * @param proceedingJoinPoint {@code ProceedingJoinPoint}
     */
    private static void validOpenApi(@NotNull ProceedingJoinPoint proceedingJoinPoint) {
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        OpenApi openApi = method.getAnnotation(OpenApi.class);
        API_SERVICE_UNSUPPORTED.whenNull(openApi);
    }

    /**
     * <h3>获取OpenApi请求参数</h3>
     *
     * @param proceedingJoinPoint {@code ProceedingJoinPoint}
     * @return {@code OpenRequest}
     */
    private static @NotNull OpenRequest getOpenRequest(@NotNull ProceedingJoinPoint proceedingJoinPoint) {
        Object[] args = proceedingJoinPoint.getArgs();
        SERVICE_ERROR.when(args.length != 1, "OpenApi必须接收一个参数");
        if (!(args[0] instanceof OpenRequest openRequest)) {
            throw new ServiceException("OpenApi必须接收一个OpenRequest参数");
        }
        return openRequest;
    }

    @SuppressWarnings("EmptyMethod")
    @Pointcut("@annotation(cn.hamm.airpower.web.open.OpenApi)")
    public void pointCut() {

    }

    /**
     * <h3>{@code OpenApi} 切面</h3>
     */
    @Around("pointCut()")
    public Object openApi(@NotNull ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        validOpenApi(proceedingJoinPoint);
        OpenRequest openRequest = getOpenRequest(proceedingJoinPoint);
        Long openLogId = null;
        String response = "";
        try {
            validOpenRequest(openRequest);
            Object object = proceedingJoinPoint.proceed();
            openLogId = addOpenLog(
                    openRequest.getOpenApp(),
                    request.getRequestURI(),
                    openRequest.decodeContent()
            );
            if (object instanceof Json json) {
                // 日志记录原始数据
                response = Json.toString(json);
                // 如果是Json 需要将 Json.data 对输出的数据进行加密
                json.setData(OpenResponse.encodeResponse(openRequest.getOpenApp(), json.getData()));
            }
            updateLogResponse(openLogId, response);
            return object;
        } catch (ServiceException serviceException) {
            updateLogResponse(openLogId, serviceException);
            throw serviceException;
        } catch (Exception exception) {
            updateLogResponse(openLogId, exception);
            throw exception;
        }
    }

    /**
     * <h3>验证请求</h3>
     *
     * @param openRequest {@code OpenRequest}
     */
    private void validOpenRequest(@NotNull OpenRequest openRequest) {
        INVALID_APP_KEY.when(!StringUtils.hasText(openRequest.getAppKey()));
        SERVICE_ERROR.whenNull(openAppService, "注入OpenAppService失败");
        IOpenApp openApp = openAppService.getByAppKey(openRequest.getAppKey());
        INVALID_APP_KEY.whenNull(openApp);
        FORBIDDEN_OPEN_APP_DISABLED.when(openApp.getIsDisabled());
        openRequest.setOpenApp(openApp);
        openRequest.check();
    }

    /**
     * <h3>添加日志</h3>
     *
     * @param openApp     {@code OpenApp}
     * @param url         请求 {@code URL}
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
     * <h3>更新日志返回数据</h3>
     *
     * @param openLogId    日志 {@code ID}
     * @param responseBody 返回值
     */
    private void updateLogResponse(Long openLogId, String responseBody) {
        if (Objects.isNull(openLogId) || Objects.isNull(openLogService)) {
            return;
        }
        openLogService.updateResponse(openLogId, responseBody);
    }

    /**
     * <h3>更新日志异常</h3>
     *
     * @param openLogId        日志 {@code ID}
     * @param serviceException 异常
     */
    private void updateLogResponse(Long openLogId, @NotNull ServiceException serviceException) {
        updateLogResponse(openLogId, Json.toString(Json.create()
                .setCode(serviceException.getCode())
                .setMessage(serviceException.getMessage())
        ));
    }

    /**
     * <h3>更新日志异常</h3>
     *
     * @param openLogId 日志 {@code ID}
     * @param exception 异常
     */
    private void updateLogResponse(Long openLogId, @NotNull Exception exception) {
        updateLogResponse(openLogId, Json.toString(Json.create().setMessage(exception.getMessage())));
    }
}
