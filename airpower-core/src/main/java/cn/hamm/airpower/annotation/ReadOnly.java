package cn.hamm.airpower.annotation;

import cn.hamm.airpower.root.RootEntity;
import cn.hamm.airpower.root.RootService;

import java.lang.annotation.*;

/**
 * <h1>只读字段</h1>
 *
 * @author Hamm.cn
 * @apiNote 可在控制器调用Service前先调用 {@link RootService#ignoreReadOnlyFields(RootEntity)} 将标记了此注解的字段移除
 */
@SuppressWarnings("JavadocReference")
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReadOnly {
}
