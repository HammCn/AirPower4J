package cn.hamm.airpower.annotation;

import cn.hamm.airpower.root.RootModel;

import java.lang.annotation.*;

/**
 * <h1>只读字段</h1>
 *
 * @author Hamm.cn
 * @apiNote 可在控制器调用 {@code Service} 前先调用 {@link RootModel#ignoreReadOnlyFields()} } 将标记了此注解的字段移除
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReadOnly {
}
