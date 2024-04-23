package cn.hamm.airpower.security;

import java.lang.annotation.*;

/**
 * <h1>是否需要登录和授权</h1>
 *
 * @author Hamm.cn
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Permission {
    /**
     * <h2>需要登录</h2>
     */
    boolean login() default true;

    /**
     * <h2>需要授权</h2>
     */
    boolean authorize() default true;
}
