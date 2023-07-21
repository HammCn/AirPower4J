package cn.hamm.airpower.response;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>接口返回过滤器</h1>
 *
 * @author Hamm
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseFilter {
    /**
     * <h1>过滤器声明类 When***</h1>
     */
    Class<?> value();
}
