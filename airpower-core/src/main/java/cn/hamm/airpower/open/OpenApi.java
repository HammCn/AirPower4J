package cn.hamm.airpower.open;

import java.lang.annotation.*;

/**
 * <h1>{@code OpenApi}</h1>
 *
 * @author Hamm.cn
 */
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface OpenApi {
}
