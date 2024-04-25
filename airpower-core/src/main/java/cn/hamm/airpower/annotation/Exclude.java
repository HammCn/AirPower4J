package cn.hamm.airpower.annotation;

import java.lang.annotation.*;

/**
 * <h1>标记字段排除</h1>
 *
 * @author Hamm.cn
 * @apiNote 控制器标记了 {@link Filter} 并指定使用的过滤器后，使用此注解标记了相同装饰器的属性将不会被接口输出。
 * 也可类标记此注解,然后通过 {@link Expose} 输出部分的属性。
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Exclude {
    /**
     * <h2>分组</h2>
     */
    Class<?>[] filters() default {};
}
