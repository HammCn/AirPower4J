package cn.hamm.airpower.security;

import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <h1>权限处理助手</h1>
 *
 * @author Hamm.cn
 */
public class AccessUtil {
    /**
     * <h2>获取需要被授权的类型</h2>
     *
     * @param clazz  类
     * @param method 方法
     * @return 需要授权的选项
     */
    public static AccessConfig getWhatNeedAccess(Class<?> clazz, Method method) {
        //默认无标记时，不需要登录和授权
        AccessConfig accessConfig = new AccessConfig();

        //判断类是否标记访问权限
        Permission permissionClass = clazz.getAnnotation(Permission.class);
        if (Objects.nonNull(permissionClass)) {
            //类有AccessRequire标记
            accessConfig.login = permissionClass.login();
            //需要登录时 RBAC选项才能启用
            accessConfig.authorize = permissionClass.login() && permissionClass.authorize();
        }
        //如果方法也标注了 方法将覆盖类的注解配置
        Permission permissionMethod = method.getAnnotation(Permission.class);
        if (Objects.nonNull(permissionMethod)) {
            //方法有AccessRequire标记
            accessConfig.login = permissionMethod.login();
            //需要登录时 RBAC选项才能启用
            accessConfig.authorize = permissionMethod.login() && permissionMethod.authorize();
        }
        return accessConfig;
    }

    /**
     * <h2>获取权限标识</h2>
     *
     * @param clazz  类
     * @param method 方法
     * @return 权限标识
     */
    public static String getPermissionIdentity(Class<?> clazz, Method method) {
        return StringUtils.uncapitalize(clazz.getSimpleName().replaceAll("Controller", "")) +
                "_" +
                method.getName();
    }
}
