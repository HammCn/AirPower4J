package cn.hamm.airpower.interceptor;

import cn.hamm.airpower.config.AirConfig;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.enums.SystemError;
import cn.hamm.airpower.interceptor.document.ApiDocument;
import cn.hamm.airpower.model.Access;
import cn.hamm.airpower.util.AirUtil;
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
 * @see #checkPermissionAccess(Long, String, HttpServletRequest)
 * @see #beforeHandleRequest(HttpServletRequest, HttpServletResponse, Class, Method)
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

    @Override
    public final boolean preHandle(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull Object object
    ) {
        HandlerMethod handlerMethod = (HandlerMethod) object;
        //取出控制器和方法
        Class<?> clazz = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();

        setShareData(REQUEST_METHOD_KEY, method);

        if (HttpMethod.GET.name().equalsIgnoreCase(request.getMethod()) &&
                AirConfig.getGlobalConfig().isEnableDocument()) {
            // 如果是GET 方法，并且开启了文档
            GetMapping getMapping = AirUtil.getReflectUtil().getAnnotation(GetMapping.class, method);
            if (Objects.isNull(getMapping)) {
                // 如果没有GetMapping注解，则直接返回文档
                ApiDocument.writeApiDocument(response, clazz, method);
                return false;
            }
        }

        beforeHandleRequest(request, response, clazz, method);
        Access access = AirUtil.getAccessUtil().getWhatNeedAccess(clazz, method);
        if (!access.isLogin()) {
            // 不需要登录 直接返回有权限
            return true;
        }
        //需要登录
        String accessToken = request.getHeader(AirConfig.getGlobalConfig().getAuthorizeHeader());

        // 优先使用 Get 参数传入的身份
        String accessTokenFromParam = request.getParameter(AirConfig.getGlobalConfig().getAuthorizeHeader());
        if (StringUtils.hasText(accessTokenFromParam)) {
            accessToken = accessTokenFromParam;
        }
        SystemError.UNAUTHORIZED.whenEmpty(accessToken);
        Long userId = AirUtil.getSecurityUtil().getUserIdFromAccessToken(accessToken);
        //需要RBAC
        if (access.isAuthorize()) {
            //验证用户是否有接口的访问权限
            return checkPermissionAccess(userId, AirUtil.getAccessUtil().getPermissionIdentity(clazz, method), request);
        }
        return true;
    }


    /**
     * <h2>验证指定的用户是否有指定权限标识的权限</h2>
     *
     * @param userId             用户ID
     * @param permissionIdentity 权限标识
     * @param request            请求对象
     * @return 验证结果
     */
    protected abstract boolean checkPermissionAccess(
            Long userId, String permissionIdentity, HttpServletRequest request
    );

    /**
     * <h2>请求拦截器前置方法</h2>
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param clazz    控制器类
     * @param method   执行方法
     */
    @SuppressWarnings({"EmptyMethod", "unused"})
    protected void beforeHandleRequest(
            HttpServletRequest request, HttpServletResponse response, Class<?> clazz, Method method
    ) {
    }

    /**
     * <h2>设置共享数据提供给其他拦截器实用</h2>
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
        if (AirUtil.getRequestUtil().isUploadRequest(request)) {
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
