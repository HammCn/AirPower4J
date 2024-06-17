package cn.hamm.airpower.interceptor;

import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.config.MessageConstant;
import cn.hamm.airpower.enums.ServiceError;
import cn.hamm.airpower.interceptor.document.ApiDocument;
import cn.hamm.airpower.model.Access;
import cn.hamm.airpower.util.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <h1>全局权限拦截器抽象类</h1>
 *
 * @author Hamm.cn
 * @see #checkUserPermission(Long, String, HttpServletRequest)
 * @see #interceptRequest(HttpServletRequest, HttpServletResponse, Class, Method)
 * @see #getRequestBody(HttpServletRequest)
 * @see #setShareData(String, Object)
 */
@Component
@Slf4j
public abstract class AbstractRequestInterceptor implements HandlerInterceptor {
    /**
     * <h2>缓存的REQUEST_METHOD_KEY</h2>
     */
    protected static final String REQUEST_METHOD_KEY = "REQUEST_METHOD_KEY";

    /**
     * <h2>拦截器</h2>
     *
     * @param request  请求
     * @param response 响应
     * @param object   请求对象
     * @return 拦截结果
     */
    @Override
    public final boolean preHandle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object object
    ) {
        ServiceError.SERVICE_ERROR.when(!Configs.getServiceConfig().isServiceRunning(), MessageConstant.SERVICE_MAINTAINING_AND_TRY_LATER);
        HandlerMethod handlerMethod = (HandlerMethod) object;
        //取出控制器和方法
        Class<?> clazz = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();

        setShareData(REQUEST_METHOD_KEY, method);

        if (HttpMethod.GET.name().equalsIgnoreCase(request.getMethod()) &&
                Configs.getServiceConfig().isEnableDocument()) {
            // 如果是GET 方法，并且开启了文档
            GetMapping getMapping = Utils.getReflectUtil().getAnnotation(GetMapping.class, method);
            if (Objects.isNull(getMapping)) {
                // 如果没有GetMapping注解，则直接返回文档
                ApiDocument.writeApiDocument(response, clazz, method);
                return false;
            }
        }
        handleRequest(request, response, clazz, method);
        return true;
    }

    /**
     * <h2>请求拦截器</h2>
     *
     * @param request  请求
     * @param response 响应
     * @param clazz    控制器
     * @param method   方法
     */
    private void handleRequest(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, Class<?> clazz, Method method) {
        interceptRequest(request, response, clazz, method);
        Access access = Utils.getAccessUtil().getWhatNeedAccess(clazz, method);
        if (!access.isLogin()) {
            // 不需要登录 直接返回有权限
            return;
        }
        //需要登录
        String accessToken = request.getHeader(Configs.getServiceConfig().getAuthorizeHeader());

        // 优先使用 Get 参数传入的身份
        String accessTokenFromParam = request.getParameter(Configs.getServiceConfig().getAuthorizeHeader());
        if (StringUtils.hasText(accessTokenFromParam)) {
            accessToken = accessTokenFromParam;
        }
        ServiceError.UNAUTHORIZED.whenEmpty(accessToken);
        Long userId = Utils.getSecurityUtil().getIdFromAccessToken(accessToken);
        //需要RBAC
        if (access.isAuthorize()) {
            //验证用户是否有接口的访问权限
            checkUserPermission(userId, Utils.getAccessUtil().getPermissionIdentity(clazz, method), request);
        }
    }

    /**
     * <h2>验证指定的用户是否有指定权限标识的权限</h2>
     *
     * @param userId             用户ID
     * @param permissionIdentity 权限标识
     * @param request            请求对象
     * @apiNote 抛出异常则为拦截
     */
    protected void checkUserPermission(
            Long userId, String permissionIdentity, HttpServletRequest request
    ) {
    }

    /**
     * <h2>拦截请求</h2>
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param clazz    控制器类
     * @param method   执行方法
     * @apiNote 抛出异常则为拦截
     */
    @SuppressWarnings({"EmptyMethod", "unused"})
    protected void interceptRequest(
            HttpServletRequest request, HttpServletResponse response, Class<?> clazz, Method method
    ) {
    }

    /**
     * <h2>设置共享数据</h2>
     *
     * @param key   KEY
     * @param value VALUE
     */
    protected final void setShareData(String key, Object value) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            requestAttributes.setAttribute(key, value, RequestAttributes.SCOPE_REQUEST);
        }
    }

    /**
     * <h2>从请求中获取请求的包体</h2>
     *
     * @param request 请求
     * @return 包体字符串
     */
    protected final @NotNull String getRequestBody(HttpServletRequest request) {
        // 文件上传的请求 返回空
        if (Utils.getRequestUtil().isUploadRequest(request)) {
            return Constant.EMPTY_STRING;
        }
        try {
            StringBuilder requestBody = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return requestBody.toString();
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
        return Constant.EMPTY_STRING;
    }
}
