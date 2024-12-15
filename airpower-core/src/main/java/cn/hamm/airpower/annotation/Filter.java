package cn.hamm.airpower.annotation;

import java.lang.annotation.*;

/**
 * <h1>接口返回过滤器</h1>
 *
 * @author Hamm.cn
 * @apiNote 使用此注解指定过滤器后, 属性上使用 {@link Exclude} 指定了相同过滤器的属性将不会被接口输出
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Filter {
    /**
     * <h3>使用的过滤器</h3>
     */
    Class<?> value();
}
