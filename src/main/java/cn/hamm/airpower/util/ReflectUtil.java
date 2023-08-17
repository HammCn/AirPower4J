package cn.hamm.airpower.util;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.root.RootEntity;
import cn.hamm.airpower.root.RootModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * <h1>反射工具类</h1>
 *
 * @author Hamm
 */
public class ReflectUtil {
    /**
     * <h2>获取描述</h2>
     * <code>Description("用户")</code>
     *
     * @param clazz 类
     * @return 描述
     */
    public static String getDescription(Class<?> clazz) {
        Description description = clazz.getAnnotation(Description.class);
        return Objects.isNull(description) ? clazz.getSimpleName() : description.value();
    }

    /**
     * <h2>获取描述</h2>
     * <code>Description("用户接口")</code>
     *
     * @param method 方法
     * @return 描述
     */
    public static String getDescription(Method method) {
        Description description = method.getAnnotation(Description.class);
        return Objects.isNull(description) ? method.getName() : description.value();
    }

    /**
     * <h2>获取描述</h2>
     * <code>Description("昵称")</code>
     *
     * @param field 字段
     * @return 描述
     */
    public static String getDescription(Field field) {
        Description description = field.getAnnotation(Description.class);
        return Objects.isNull(description) ? field.getName() : description.value();
    }

    /**
     * <h2>是否是继承自BaseEntity</h2>
     *
     * @param clazz 类
     * @return 布尔
     */
    public static boolean isEntity(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return false;
        }
        if (clazz.getSimpleName().equalsIgnoreCase(RootEntity.class.getSimpleName())) {
            return true;
        }
        return isEntity(clazz.getSuperclass());
    }

    /**
     * <h2>是否是继承自BaseModel</h2>
     *
     * @param clazz 类
     * @return 布尔
     */
    public static boolean isModel(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return false;
        }
        if (clazz.getSimpleName().equalsIgnoreCase(RootModel.class.getSimpleName())) {
            return true;
        }
        return isModel(clazz.getSuperclass());
    }

    /**
     * <h2>获取指定类的字段列表</h2>
     *
     * @param clazz 类
     * @return 字段数组
     */
    public static List<Field> getFieldList(Class<?> clazz) {
        List<Field> fieldList = new LinkedList<>();
        if (Objects.isNull(clazz)) {
            return fieldList;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 过滤静态属性 或 过滤transient 关键字修饰的属性
            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                fieldList.add(field);
            }
        }
        // 处理父类字段
        Class<?> superClass = clazz.getSuperclass();
        if (Objects.isNull(superClass) || Object.class.equals(superClass)) {
            return fieldList;
        }
        fieldList.addAll(ReflectUtil.getFieldList(superClass));
        return fieldList;
    }

    /**
     * <h2>获取类的所有公开属性名称列表</h2>
     *
     * @param clazz 类
     * @return 属性名数组
     */
    public static List<String> getFieldNameList(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }
}
