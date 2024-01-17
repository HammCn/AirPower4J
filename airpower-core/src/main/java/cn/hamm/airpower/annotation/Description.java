package cn.hamm.airpower.annotation;

import java.lang.annotation.*;

/**
 * <h1>类或属性的文案</h1>
 *
 * @author Hamm
 */
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Description {
    /**
     * 文案
     */
    String value();
}
