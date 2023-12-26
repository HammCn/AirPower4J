package cn.hamm.airpower.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>只读字段,不允许控制器修改</h1>
 *
 * @author Hamm
 * @apiNote 标记此注解的属性, 不允许接口传入新增和修改
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadOnly {
}
