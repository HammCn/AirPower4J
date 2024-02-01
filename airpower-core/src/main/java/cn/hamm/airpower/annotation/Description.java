package cn.hamm.airpower.annotation;

import cn.hamm.airpower.util.ReflectUtil;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <h1>类或属性的文案</h1>
 *
 * @author Hamm
 * @apiNote 配置后可通过 {@link ReflectUtil } 获取
 * @see ReflectUtil#getDescription(Method) 
 * @see ReflectUtil#getDescription(Field) 
 * @see ReflectUtil#getDescription(Class) 
 */
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Description {
    /**
     * 文案
     */
    String value();
}
