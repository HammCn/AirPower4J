package cn.hamm.airpower.util;

import cn.hamm.airpower.annotation.Permission;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.model.Access;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <h1>权限处理助手</h1>
 *
 * @author Hamm.cn
 */
@Component
public class AccessUtil {
    /**
     * <h2>获取需要被授权的类型</h2>
     *
     * @param clazz  类
     * @param method 方法
     * @return 需要授权的选项
     */
    public final @NotNull Access getWhatNeedAccess(@NotNull Class<?> clazz, @NotNull Method method) {
        //默认无标记时，不需要登录和授权
        Access access = new Access();

        //判断类是否标记访问权限
        Permission permissionClass = clazz.getAnnotation(Permission.class);
        if (Objects.nonNull(permissionClass)) {
            //类有AccessRequire标记
            access.setLogin(permissionClass.login());
            //需要登录时 RBAC选项才能启用
            access.setAuthorize(permissionClass.login() && permissionClass.authorize());
        }
        //如果方法也标注了 方法将覆盖类的注解配置
        Permission permissionMethod = method.getAnnotation(Permission.class);
        if (Objects.nonNull(permissionMethod)) {
            //方法有AccessRequire标记
            access.setLogin(permissionMethod.login());
            //需要登录时 RBAC选项才能启用
            access.setAuthorize(permissionMethod.login() && permissionMethod.authorize());
        }
        return access;
    }

    /**
     * <h2>获取权限标识</h2>
     *
     * @param clazz  类
     * @param method 方法
     * @return 权限标识
     */
    public final @NotNull String getPermissionIdentity(@NotNull Class<?> clazz, @NotNull Method method) {
        return StringUtils.uncapitalize(clazz.getSimpleName().replaceAll("Controller", Constant.EMPTY_STRING)) +
                "_" +
                method.getName();
    }
}
