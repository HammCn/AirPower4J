package cn.hamm.airpower.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>描述信息配置</h1>
 *
 * @author Hamm
 */
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
    /**
     * <h2>描述信息</h2>
     */
    String value();
}
