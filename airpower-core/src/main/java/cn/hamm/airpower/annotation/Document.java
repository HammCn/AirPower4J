package cn.hamm.airpower.annotation;

import cn.hamm.airpower.util.ReflectUtil;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <h1>文档说明</h1>
 *
 * @author Hamm.cn
 * @apiNote 配置后可通过 {@link ReflectUtil } 获取
 * @see ReflectUtil#getDocument(Method)
 * @see ReflectUtil#getDocument(Field)
 * @see ReflectUtil#getDocument(Class)
 */
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Document {
    /**
     * <h2>文档说明</h2>
     */
    String value();
}
