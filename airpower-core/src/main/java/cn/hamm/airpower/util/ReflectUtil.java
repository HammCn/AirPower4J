package cn.hamm.airpower.util;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.annotation.Document;
import cn.hamm.airpower.root.RootController;
import cn.hamm.airpower.root.RootEntity;
import cn.hamm.airpower.root.RootModel;

import java.lang.annotation.Annotation;
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
     * <h2>创建一个特殊的空实体</h2>
     *
     * @param clazz 类型
     * @param <T>   类型
     * @return 空实体
     */
    public static <T extends RootModel<T>> T createNull(Class<T> clazz) {
        try {
            T instance = clazz.getConstructor().newInstance();
            Field field = clazz.getField("nullModel");
            field.setAccessible(true);
            field.set(instance, true);
            field.setAccessible(false);
            return instance;
        } catch (Exception e) {
            return null;
        }
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
     */
    public static String getDescription(Class<?> clazz) {
        Description description = clazz.getAnnotation(Description.class);
        if (Objects.nonNull(description)) {
            return description.value();
        }
        if (isTheRootClass(clazz)) {
            return clazz.getSimpleName();
        }
        Class<?> superClass = clazz.getSuperclass();
        return getDescription(superClass);
    }

    /**
     * <h2>获取方法描述</h2>
     *
     * @param method 方法
     * @return 描述
     */
    public static String getDescription(Method method) {
        return getDescription(method, method.getDeclaringClass());
    }

    /**
     * <h2>获取字段描述</h2>
     *
     * @param field 字段
     * @return 描述
     */
    public static String getDescription(Field field) {
        Description description = field.getAnnotation(Description.class);
        return Objects.isNull(description) ? field.getName() : description.value();
    }

    /**
     * <h2>获取类的文档</h2>
     *
     * @param clazz 类
     * @return 文档
     */
    public static String getDocument(Class<?> clazz) {
        Document document = clazz.getAnnotation(Document.class);
        if (Objects.nonNull(document)) {
            return document.value();
        }
        if (isTheRootClass(clazz)) {
            return "";
        }
        Class<?> superClass = clazz.getSuperclass();
        return getDocument(superClass);
    }

    /**
     * <h2>获取方法的文档</h2>
     *
     * @param method 方法
     * @return 文档
     */
    public static String getDocument(Method method) {
        return getDocument(method, method.getDeclaringClass());
    }


    /**
     * <h2>获取字段的文档</h2>
     *
     * @param field 字段
     * @return 文档
     */
    public static String getDocument(Field field) {
        Document document = field.getAnnotation(Document.class);
        if (Objects.nonNull(document)) {
            return document.value();
        }
        return "";
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
     * <h2>递归获取方法的注解</h2>
     *
     * @param <A>             注解泛型
     * @param annotationClass 注解类
     * @param method          方法
     * @param currentClass    所在类
     * @return PostMapping
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
        try {
            Method superMethod = superClass.getMethod(method.getName(), method.getParameterTypes());
            return getAnnotation(annotationClass, superMethod, superClass);
        } catch (NoSuchMethodException e) {
            superClass = superClass.getSuperclass();
            if (Objects.isNull(superClass)) {
                return null;
            }
            return getAnnotation(annotationClass, method, superClass);
        }
    }

    /**
     * <h2>递归获取方法的描述</h2>
     *
     * @param method       方法
     * @param currentClass 所在类
     * @return 描述
     */
    private static String getDescription(Method method, Class<?> currentClass) {
        Description description = method.getAnnotation(Description.class);
        if (Objects.nonNull(description)) {
            return description.value();
        }
        if (isTheRootClass(currentClass)) {
            return method.getName();
        }
        Class<?> superClass = currentClass.getSuperclass();
        if (Objects.isNull(superClass)) {
            return method.getName();
        }
        try {
            Method superMethod = superClass.getMethod(method.getName(), method.getParameterTypes());
            return getDescription(superMethod, superClass);
        } catch (NoSuchMethodException e) {
            superClass = superClass.getSuperclass();
            return getDescription(method, superClass);
        }
    }

    /**
     * <h2>递归获取方法的描述</h2>
     *
     * @param method       方法
     * @param currentClass 所在类
     * @return 描述
     */
    private static String getDocument(Method method, Class<?> currentClass) {
        Document document = method.getAnnotation(Document.class);
        if (Objects.nonNull(document)) {
            return document.value();
        }
        if (isTheRootClass(currentClass)) {
            return "";
        }
        Class<?> superClass = currentClass.getSuperclass();
        if (Objects.isNull(superClass)) {
            return "";
        }
        try {
            Method superMethod = superClass.getMethod(method.getName(), method.getParameterTypes());
            return getDocument(superMethod, superClass);
        } catch (NoSuchMethodException e) {
            superClass = superClass.getSuperclass();
            return getDocument(method, superClass);
        }
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

}
