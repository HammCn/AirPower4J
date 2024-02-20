package cn.hamm.airpower.util;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.root.RootController;
import cn.hamm.airpower.root.RootEntity;
import cn.hamm.airpower.root.RootModel;
import org.springframework.web.bind.annotation.PostMapping;

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
     * 获取方法的PostMapping
     *
     * @param method 方法
     * @return PostMapping
     */
    public static PostMapping getPostMapping(Method method) {
        return getPostMapping(method, method.getDeclaringClass());
    }

    /**
     * 获取描述
     * <code>Description("用户")</code>
     *
     * @param clazz 类
     * @return 描述
     */
    public static String getDescription(Class<?> clazz) {
        Description description = clazz.getAnnotation(Description.class);
        if (Objects.nonNull(description)) {
            return description.value();
        }
        if (clazz.getName().equals(RootController.class.getName()) || clazz.getName().equals(Object.class.getName())) {
            return clazz.getSimpleName();
        }
        Class<?> superClass = clazz.getSuperclass();
        return getDescription(superClass);
    }


    /**
     * 获取描述
     * <code>Description("用户接口")</code>
     *
     * @param method 方法
     * @return 描述
     */
    public static String getDescription(Method method) {
        return getDescription(method, method.getDeclaringClass());
    }

    /**
     * 获取描述
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
     * 是否是继承自BaseEntity
     *
     * @param clazz 类
     * @return 布尔
     */
    @SuppressWarnings("unused")
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
     * 是否是继承自BaseModel
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
     * 获取指定类的字段列表
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
     * 获取类的所有公开属性名称列表
     *
     * @param clazz 类
     * @return 属性名数组
     */
    @SuppressWarnings("unused")
    public static List<String> getFieldNameList(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }


    /**
     * 获取方法的PostMapping
     *
     * @param method 方法
     * @param clazz  所在类
     * @return PostMapping
     */
    private static PostMapping getPostMapping(Method method, Class<?> clazz) {
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (Objects.nonNull(postMapping)) {
            return postMapping;
        }
        if (clazz.getName().equals(RootController.class.getName()) || clazz.getName().equals(Object.class.getName())) {
            return null;
        }
        Class<?> superClass = clazz.getSuperclass();
        try {
            Method superMethod = superClass.getMethod(method.getName(), RootEntity.class);
            return getPostMapping(superMethod, superClass);
        } catch (NoSuchMethodException e) {
            superClass = superClass.getSuperclass();
            return getPostMapping(method, superClass);
        }
    }

    /**
     * 获取描述
     * <code>Description("用户接口")</code>
     *
     * @param method 方法
     * @param clazz  所在类
     * @return 描述
     */
    private static String getDescription(Method method, Class<?> clazz) {
        Description description = method.getAnnotation(Description.class);
        if (Objects.nonNull(description)) {
            return description.value();
        }
        if (clazz.getName().equals(RootController.class.getName()) || clazz.getName().equals(Object.class.getName())) {
            return method.getName();
        }
        Class<?> superClass = clazz.getSuperclass();
        try {
            Method superMethod = superClass.getMethod(method.getName(), RootEntity.class);
            return getDescription(superMethod, superClass);
        } catch (NoSuchMethodException e) {
            superClass = superClass.getSuperclass();
            return getDescription(method, superClass);
        }
    }
}
