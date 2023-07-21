package cn.hamm.airpower.util;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.root.RootEntity;
import cn.hamm.airpower.root.RootEntityController;
import cn.hamm.airpower.root.RootModel;
import cn.hamm.airpower.security.AccessConfig;
import cn.hamm.airpower.security.AccessUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * <h1>反射工具类</h1>
 *
 * @author Hamm
 */
public class ReflectUtil {
    /**
     * <h1>获取描述</h1>
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
     * <h1>获取描述</h1>
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
     * <h1>获取描述</h1>
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
     * <h1>获取所有模块和接口</h1>
     *
     * @param packageName 包名称
     * @return 模块和接口树
     */
    public static List<Map<String, Object>> getApiTreeList(String packageName) {
        // 遍历所有接口
        List<Map<String, Object>> moduleList = new ArrayList<>();
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(packageName) + "/**/*.class";
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

            for (Resource resource : resources) {
                // 用于读取类信息
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                // 扫描到的class
                String className = metadataReader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);

                RestController restController = clazz.getAnnotation(RestController.class);
                if (Objects.isNull(restController) || RootEntityController.class.getSimpleName().equals(clazz.getSimpleName())) {
                    // 不是rest控制器或者是指定的几个白名单控制器
                    continue;
                }

                String customClassName = ReflectUtil.getDescription(clazz);

                // 读取类的RequestMapping
                RequestMapping requestMappingClass = clazz.getAnnotation(RequestMapping.class);
                String pathClass = "";
                if (Objects.nonNull(requestMappingClass) && requestMappingClass.value().length > 0) {
                    // 标了RequestMapping
                    pathClass = requestMappingClass.value()[0];
                }
                // 取出所有控制器方法
                Method[] methods = clazz.getMethods();
                List<Map<String, String>> apiList = new ArrayList<>();
                for (Method method : methods) {
                    String customMethodName = ReflectUtil.getDescription(method);
                    System.out.println(clazz.getSimpleName() + ":" + method.getName());
                    PostMapping postMappingMethod = method.getAnnotation(PostMapping.class);

                    AccessConfig accessConfig = AccessUtil.getWhatNeedAccess(clazz, method);

                    if (Objects.nonNull(postMappingMethod) && postMappingMethod.value().length > 0) {
                        // 内部类复制final变量
                        final boolean finalMethodNeedLogin = accessConfig.login;
                        final boolean finalMethodNeedRbac = accessConfig.authorize;
                        String finalPathClass = pathClass;
                        apiList.add(new HashMap<>(4) {{
                            put("name", customMethodName);
                            put("path", (!"".equalsIgnoreCase(finalPathClass) ? (finalPathClass + "/") : "") + postMappingMethod.value()[0]);
                            put("needLogin", finalMethodNeedLogin ? "是" : "否");
                            put("needRbac", finalMethodNeedLogin ? (finalMethodNeedRbac ? "是" : "否") : "否");
                        }});
                    }
                }
                if (apiList.size() > 0) {
                    moduleList.add(new HashMap<>(3) {
                        {
                            put("name", customClassName);
                            put("module", clazz.getSimpleName());
                            put("apis", apiList);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moduleList;
    }

    /**
     * <h1>是否是继承自BaseEntity</h1>
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
     * <h1>是否是继承自BaseModel</h1>
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
     * <h1>获取指定类的字段列表</h1>
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
     * <h1>获取类的所有公开属性名称列表</h1>
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
