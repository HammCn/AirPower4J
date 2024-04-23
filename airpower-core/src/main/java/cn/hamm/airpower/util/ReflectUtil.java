package cn.hamm.airpower.util;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.annotation.Document;
import cn.hamm.airpower.interfaces.IDictionary;
import cn.hamm.airpower.root.RootController;
import cn.hamm.airpower.root.RootEntity;
import cn.hamm.airpower.root.RootModel;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * <h1>反射工具类</h1>
 *
 * @author Hamm.cn
 * @see IDictionary
 */
@Slf4j
public class ReflectUtil {
    /**
     * <h2>获取对象指定属性的值</h2>
     *
     * @param object 对象
     * @param field  属性
     * @return 值
     */
    public static @Nullable Object getFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException exception) {
            log.error("反射获取值失败", exception);
            return null;
        } finally {
            field.setAccessible(false);
        }
    }

    /**
     * <h2>设置对象指定属性的值</h2>
     *
     * @param object 对象
     * @param field  属性
     * @param value  值
     */
    public static void setFieldValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException exception) {
            log.error("反射设置值失败", exception);
        } finally {
            field.setAccessible(false);
        }
    }

    /**
     * <h2>清空对象指定属性的值</h2>
     *
     * @param object 对象
     * @param field  属性
     */
    public static void clearFieldValue(Object object, Field field) {
        setFieldValue(object, field, null);
    }

    /**
     * <h2>判断是否是根类</h2>
     *
     * @param clazz 类
     * @return 判断结果
     */
    public static boolean isTheRootClass(Class<?> clazz) {
        return clazz.getName().equals(RootController.class.getName()) ||
                clazz.getName().equals(RootEntity.class.getName()) ||
                clazz.getName().equals(Object.class.getName());
    }

    /**
     * <h2>获取指定方法的注解</h2>
     *
     * @param annotationClass 注解类
     * @param method          方法
     * @param <A>             泛型
     * @return 注解
     */
    public static <A extends Annotation> A getAnnotation(Class<A> annotationClass, Method method) {
        return getAnnotation(annotationClass, method, method.getDeclaringClass());
    }

    /**
     * <h2>获取指定类的注解</h2>
     *
     * @param annotationClass 注解类
     * @param clazz           类
     * @param <A>             泛型
     * @return 注解
     */
    public static <A extends Annotation> A getAnnotation(Class<A> annotationClass, Class<?> clazz) {
        A annotation = clazz.getAnnotation(annotationClass);
        if (Objects.nonNull(annotation)) {
            return annotation;
        }
        if (isTheRootClass(clazz)) {
            return null;
        }
        Class<?> superClass = clazz.getSuperclass();
        return getAnnotation(annotationClass, superClass);
    }

    /**
     * <h2>获取字段的注解</h2>
     *
     * @param annotationClass 注解类
     * @param field           字段
     * @param <A>             泛型
     * @return 注解
     */
    public static <A extends Annotation> A getAnnotation(Class<A> annotationClass, Field field) {
        return field.getAnnotation(annotationClass);
    }

    /**
     * <h2>获取类描述</h2>
     *
     * @param clazz 类
     * @return 描述
     * @see Description
     */
    public static String getDescription(Class<?> clazz) {
        Description description = ReflectUtil.getAnnotation(Description.class, clazz);
        return Objects.isNull(description) ? clazz.getSimpleName() : description.value();
    }

    /**
     * <h2>获取方法描述</h2>
     *
     * @param method 方法
     * @return 描述
     * @see Description
     */
    public static String getDescription(Method method) {
        Description description = getAnnotation(Description.class, method, method.getDeclaringClass());
        return Objects.isNull(description) ? method.getName() : description.value();
    }

    /**
     * <h2>获取字段描述</h2>
     *
     * @param field 字段
     * @return 描述
     * @see Description
     */
    public static String getDescription(Field field) {
        Description description = getAnnotation(Description.class, field);
        return Objects.isNull(description) ? field.getName() : description.value();
    }

    /**
     * <h2>获取类的文档</h2>
     *
     * @param clazz 类
     * @return 文档
     * @see Document
     */
    public static String getDocument(Class<?> clazz) {
        Document document = getAnnotation(Document.class, clazz);
        return Objects.isNull(document) ? "" : document.value();
    }

    /**
     * <h2>获取方法的文档</h2>
     *
     * @param method 方法
     * @return 文档
     * @see Document
     */
    public static String getDocument(Method method) {
        Document document = getAnnotation(Document.class, method);
        return Objects.isNull(document) ? "" : document.value();
    }


    /**
     * <h2>获取字段的文档</h2>
     *
     * @param field 字段
     * @return 文档
     * @see Document
     */
    public static String getDocument(Field field) {
        Document document = getAnnotation(Document.class, field);
        return Objects.isNull(document) ? "" : document.value();
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
        if (clazz.getName().equalsIgnoreCase(RootEntity.class.getName())) {
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
        if (clazz.getName().equalsIgnoreCase(RootModel.class.getName())) {
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
        if (isTheRootClass(clazz)) {
            return fieldList;
        }
        // 处理父类字段
        Class<?> superClass = clazz.getSuperclass();
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

    /**
     * <h2>递归获取方法的注解</h2>
     *
     * @param <A>             注解泛型
     * @param annotationClass 注解类
     * @param method          方法
     * @param currentClass    所在类
     * @return 装配的注解
     */
    private static <A extends Annotation> A getAnnotation(Class<A> annotationClass, Method method, Class<?> currentClass) {
        A annotation = method.getAnnotation(annotationClass);
        if (Objects.nonNull(annotation)) {
            return annotation;
        }
        if (isTheRootClass(currentClass)) {
            return null;
        }
        Class<?> superClass = currentClass.getSuperclass();
        if (Objects.isNull(superClass)) {
            return null;
        }
        List<Method> superMethods = Arrays.stream(superClass.getMethods()).toList();
        Method superMethod = superMethods.stream().filter(m -> m.getName().equals(method.getName())).findFirst().orElse(null);
        if (Objects.isNull(superMethod)) {
            return null;
        }
        return getAnnotation(annotationClass, superMethod, superClass);
    }
}
