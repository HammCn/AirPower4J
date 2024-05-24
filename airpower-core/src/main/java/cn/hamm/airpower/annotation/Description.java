package cn.hamm.airpower.annotation;

import cn.hamm.airpower.util.ReflectUtil;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <h1>类或属性的文案</h1>
 *
 * @author Hamm.cn
 * @apiNote 配置后可通过 {@link ReflectUtil } 获取
 * @see ReflectUtil#getDescription(Method)
 * @see ReflectUtil#getDescription(Field)
 * @see ReflectUtil#getDescription(Class)
 */
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Description {
    /**
     * <h2>描述文案</h2>
     *
     * @apiNote 将显示在错误信息、验证信息、文档等处
     */
    String value();
}
