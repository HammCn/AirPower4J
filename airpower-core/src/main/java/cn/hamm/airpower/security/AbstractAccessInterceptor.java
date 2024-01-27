package cn.hamm.airpower.security;

import cn.hamm.airpower.config.GlobalConfig;
import cn.hamm.airpower.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

/**
 * <h1>全局权限拦截器抽象类</h1>
 *
 * @author Hamm
 */
@Component
@Slf4j
public abstract class AbstractAccessInterceptor implements HandlerInterceptor {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private GlobalConfig globalConfig;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object object) {
        HandlerMethod handlerMethod = (HandlerMethod) object;
        beforeHandleRequest(request, response, handlerMethod);
        //取出控制器和方法
        Class<?> clazz = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();
        AccessConfig accessConfig = AccessUtil.getWhatNeedAccess(clazz, method);
        if (!accessConfig.login) {
            // 不需要登录 直接返回有权限
            return true;
        }
        //需要登录
        String accessToken = request.getHeader(globalConfig.getAuthorizeHeader());
        Result.UNAUTHORIZED.whenEmpty(accessToken);
        Long userId = securityUtil.getUserIdFromAccessToken(accessToken);
        //需要RBAC
        if (accessConfig.authorize) {
            //验证用户是否有接口的访问权限
            return checkPermissionAccess(userId, AccessUtil.getPermissionIdentity(clazz, method), request);
        }
        return true;
    }

    /**
     * 验证指定的用户是否有指定权限标识的权限
     *
     * @param userId             用户ID
     * @param permissionIdentity 权限标识
     * @param request            请求对象
     * @return 验证结果
     */
    public abstract boolean checkPermissionAccess(Long userId, String permissionIdentity, HttpServletRequest request);

    /**
     * 请求拦截器前置方法
     *
     * @param request       请求对象
     * @param response      响应对象
     * @param handlerMethod 请求方法
     */
    @SuppressWarnings("unused")
    protected void beforeHandleRequest(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {

    }
}
