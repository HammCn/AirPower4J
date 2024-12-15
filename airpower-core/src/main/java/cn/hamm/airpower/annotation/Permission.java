package cn.hamm.airpower.annotation;

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
     * <h3>需要登录</h3>
     */
    boolean login() default true;

    /**
     * <h3>需要授权</h3>
     */
    boolean authorize() default true;
}
