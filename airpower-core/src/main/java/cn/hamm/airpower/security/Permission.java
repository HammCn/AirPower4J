package cn.hamm.airpower.security;

import java.lang.annotation.*;

/**
 * <h1>是否需要登录和授权</h1>
 *
 * @author Hamm
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Permission {
    /**
     * 需要登录
     */
    boolean login() default true;

    /**
     * 需要授权
     */
    boolean authorize() default true;
}
