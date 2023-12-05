package cn.hamm.airpower.annotation;

import cn.hamm.airpower.response.Filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>标记字段排除</h1>
 *
 * @author Hamm
 * @apiNote 控制器标记了 {@link Filter} 并指定使用的过滤器后，使用此注解标记了相同装饰器的属性将不会被接口输出。
 * 也可类标记此注解,然后通过 {@link Expose} 输出部分的属性。
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Exclude {
    /**
     * 分组
     */
    Class<?>[] filters() default {};
}
