package cn.hamm.airpower.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>暴露字段属性</h1>
 *
 * @author Hamm
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Expose {
    /**
     * <h1>分组</h1>
     */
    Class<?>[] filters() default {};
}
