package cn.hamm.airpower.annotation;

import cn.hamm.airpower.enums.Api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>继承的接口(白名单优先)</h1>
 *
 * <li>如不标记此注解,则默认将所有基类接口继承</li>
 * <li>如标记白名单,则需写全需要继承的接口</li>
 * <li>如标记黑名单,则只需要写不继承的接口</li>
 *
 * @author Hamm.cn
 * @see Api
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Extends {
    /**
     * <h2>白名单</h2>
     */
    Api[] value() default {};

    /**
     * <h2>黑名单</h2>
     */
    Api[] exclude() default {};
}
