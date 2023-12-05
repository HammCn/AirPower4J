package cn.hamm.airpower.annotation;

import cn.hamm.airpower.response.Filter;
import cn.hamm.airpower.root.RootEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>标记是挂载数据</h1>
 *
 * @author Hamm
 * @apiNote 标记此注解的属性将在控制器被标记 {@link Filter} 并指定过滤器为 {@link RootEntity.WhenPayLoad} 时不输出。
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Payload {
}
