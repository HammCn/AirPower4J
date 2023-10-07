package cn.hamm.airpower.security;

import cn.hamm.airpower.config.GlobalConfig;
import cn.hamm.airpower.result.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * <h1>全局权限拦截器抽象类</h1>
 *
 * @author Hamm
 */
public abstract class AbstractAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse, @NotNull Object object) {
        // 设置允许跨域
        if (setCrossOriginHeaders(httpServletRequest, httpServletResponse)) {
            return false;
        }
        beforeRequestHandle(httpServletRequest);
        HandlerMethod handlerMethod = (HandlerMethod) object;
        //取出控制器和方法
        Class<?> clazz = handlerMethod.getBean().getClass();
        Method method = handlerMethod.getMethod();
        AccessConfig accessConfig = AccessUtil.getWhatNeedAccess(clazz, method);
        if (!accessConfig.login) {
            saveRequestLog(httpServletRequest, clazz, method, null);
            // 不需要登录 直接返回有权限
            return true;
        }
        //需要登录
        String accessToken = httpServletRequest.getHeader(GlobalConfig.authorizeHeader);
        Result.UNAUTHORIZED.whenEmpty(accessToken);
        Long userId = JwtUtil.getUserId(accessToken);
        JwtUtil.verify(getUserPassword(userId), accessToken);
        saveRequestLog(httpServletRequest, clazz, method, userId);
        //需要RBAC
        if (accessConfig.authorize) {
            //验证用户是否有接口的访问权限
            return checkPermissionAccess(userId, AccessUtil.getPermissionIdentity(clazz, method));
        }
        return true;
    }

    /**
     * <h2>保存请求日志</h2>
     *
     * @param request 请求体
     * @param clazz   调用类
     * @param method  调用方法
     * @param userId  用户ID
     */
    public void saveRequestLog(HttpServletRequest request, Class<?> clazz, Method method, Long userId) {
    }

    /**
     * <h2>获取用户的密码</h2>
     *
     * @param userId 用户ID
     * @return 密码
     */
    public abstract String getUserPassword(Long userId);

    /**
     * <h2>请求初始化前置处理方法</h2>
     *
     * @param httpServletRequest 请求
     */
    public void beforeRequestHandle(HttpServletRequest httpServletRequest) {
    }

    /**
     * <h2>验证指定的用户是否有指定权限标识的权限</h2>
     *
     * @param userId             用户ID
     * @param permissionIdentity 权限标识
     * @return 验证结果
     */
    public abstract boolean checkPermissionAccess(Long userId, String permissionIdentity);

    /**
     * <h2>设置允许跨域的头</h2>
     *
     * @param httpServletResponse response对象
     */
    private boolean setCrossOriginHeaders(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");


        if (HttpMethod.OPTIONS.name().equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            try {
                PrintWriter writer = httpServletResponse.getWriter();
                writer.println("Hello World");
                writer.flush();
                writer.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
