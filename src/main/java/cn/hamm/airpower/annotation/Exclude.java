package cn.hamm.airpower.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>排除字段属性</h1>
 *
 * @author Hamm
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Exclude {
    /**
     * <h2>分组</h2>
     */
    Class<?>[] filters() default {};
}
