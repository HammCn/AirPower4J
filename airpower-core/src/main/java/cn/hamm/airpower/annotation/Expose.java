package cn.hamm.airpower.annotation;

import java.lang.annotation.*;

/**
 * <h1>暴露字段属性</h1>
 *
 * @author Hamm
 * @apiNote 此注解用于类标记了全部忽略后, 需要对部分字段进行暴露的场景。可参考 {@link Exclude}
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Expose {
    /**
     * 过滤器列表
     */
    Class<?>[] filters() default {};
}
