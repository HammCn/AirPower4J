package cn.hamm.airpower.util;

import cn.hamm.airpower.annotation.ApiController;
import cn.hamm.airpower.annotation.Extends;
import cn.hamm.airpower.annotation.Permission;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.enums.Api;
import cn.hamm.airpower.interfaces.IPermission;
import cn.hamm.airpower.model.Access;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <h1>权限处理助手</h1>
 *
 * @author Hamm.cn
 */
@Component
@Slf4j
public class AccessUtil {
    /**
     * <h2>控制器字节码文件路径</h2>
     */
    private static final String CONTROLLER_CLASS_PATH = "/**/*" + Constant.CONTROLLER_SUFFIX + ".class";

    /**
     * <h2>获取需要被授权的类型</h2>
     *
     * @param clazz  类
     * @param method 方法
     * @return 需要授权的选项
     */
    public final @NotNull Access getWhatNeedAccess(@NotNull Class<?> clazz, @NotNull Method method) {
        //默认无标记时，不需要登录和授权
        Access access = new Access();

        //判断类是否标记访问权限
        Permission permissionClass = clazz.getAnnotation(Permission.class);
        if (Objects.nonNull(permissionClass)) {
            //类有AccessRequire标记
            access.setLogin(permissionClass.login());
            //需要登录时 RBAC选项才能启用
            access.setAuthorize(permissionClass.login() && permissionClass.authorize());
        }
        //如果方法也标注了 方法将覆盖类的注解配置
        Permission permissionMethod = method.getAnnotation(Permission.class);
        if (Objects.nonNull(permissionMethod)) {
            //方法有AccessRequire标记
            access.setLogin(permissionMethod.login());
            //需要登录时 RBAC选项才能启用
            access.setAuthorize(permissionMethod.login() && permissionMethod.authorize());
        }
        return access;
    }

    /**
     * <h2>获取权限标识</h2>
     *
     * @param clazz  类
     * @param method 方法
     * @return 权限标识
     */
    public final @NotNull String getPermissionIdentity(@NotNull Class<?> clazz, @NotNull Method method) {
        return StringUtils.uncapitalize(clazz.getSimpleName().replaceAll(Constant.CONTROLLER_SUFFIX, Constant.EMPTY_STRING)) +
                Constant.UNDERLINE +
                method.getName();
    }

    /**
     * <h2>扫描并返回权限列表</h2>
     *
     * @param clazz           入口类
     * @param permissionClass 权限类
     * @param <P>             权限类型
     * @return 权限列表
     */
    public final <P extends IPermission<P>> @NotNull List<P> scanPermission(@NotNull Class<?> clazz, Class<P> permissionClass) {
        return scanPermission(clazz.getPackageName(), permissionClass);
    }

    /**
     * <h2>扫描并返回权限列表</h2>
     *
     * @param packageName     包名
     * @param permissionClass 权限类
     * @param <P>             权限类型
     * @return 权限列表
     */
    public final <P extends IPermission<P>> @NotNull List<P> scanPermission(String packageName, Class<P> permissionClass) {
        List<P> permissions = new ArrayList<>();
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(packageName) + CONTROLLER_CLASS_PATH;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

            for (Resource resource : resources) {
                // 用于读取类信息
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                // 扫描到的class
                String className = metadataReader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);

                ApiController apiController = Utils.getReflectUtil().getAnnotation(ApiController.class, clazz);
                if (Objects.isNull(apiController)) {
                    // 不是rest控制器或者是指定的几个白名单控制器
                    continue;
                }

                String customClassName = Utils.getReflectUtil().getDescription(clazz);
                String identity = clazz.getSimpleName().replaceAll(Constant.CONTROLLER_SUFFIX, Constant.EMPTY_STRING);
                P permission = permissionClass.getConstructor().newInstance();

                permission.setName(customClassName).setIdentity(identity);
                permission.setChildren(new ArrayList<>());

                String apiPath = clazz.getSimpleName().replaceAll(Constant.CONTROLLER_SUFFIX, Constant.EMPTY_STRING) + Constant.UNDERLINE;

                // 取出所有控制器方法
                Method[] methods = clazz.getMethods();

                // 取出控制器类上的Extends注解 如自己没标 则使用父类的
                Extends extendsApi = Utils.getReflectUtil().getAnnotation(Extends.class, clazz);
                for (Method method : methods) {

                    if (Objects.nonNull(extendsApi)) {
                        try {
                            Api current = Utils.getDictionaryUtil().getDictionary(Api.class, Api::getMethodName, method.getName());
                            if (checkApiExcluded(current, extendsApi)) {
                                continue;
                            }
                        } catch (Exception ignored) {
                        }
                    }
                    String subIdentity = getMethodPermissionIdentity(method, apiPath);
                    if (apiPath.equals(subIdentity)) {
                        continue;
                    }
                    String customMethodName = Utils.getReflectUtil().getDescription(method);
                    Access accessConfig = Utils.getAccessUtil().getWhatNeedAccess(clazz, method);
                    if (!accessConfig.isLogin() || !accessConfig.isAuthorize()) {
                        // 这里可以选择是否不读取这些接口的权限，但前端可能需要
                        continue;
                    }
                    P subPermission = permissionClass.getConstructor().newInstance();
                    subPermission.setIdentity(subIdentity).setName(customClassName + Constant.LINE + customMethodName);
                    permission.getChildren().add(subPermission);
                }
                permissions.add(permission);
            }
        } catch (Exception exception) {
            log.error("扫描权限出错", exception);
        }
        return permissions;
    }

    /**
     * <h2>检查Api是否在子控制器中被排除</h2>
     *
     * @param api    Api
     * @param extend 注解
     * @return 检查结果
     */
    private boolean checkApiExcluded(Api api, @NotNull Extends extend) {
        List<Api> excludeList = Arrays.asList(extend.exclude());
        List<Api> includeList = Arrays.asList(extend.value());
        if (excludeList.contains(api)) {
            return true;
        }
        if (includeList.isEmpty()) {
            return false;
        }
        return !includeList.contains(api);
    }

    /**
     * <h2>获取方法权限标识</h2>
     *
     * @param method  方法
     * @param apiPath Api路径
     * @return 权限标识
     */
    private @NotNull String getMethodPermissionIdentity(Method method, String apiPath) {
        RequestMapping requestMapping = Utils.getReflectUtil().getAnnotation(RequestMapping.class, method);
        PostMapping postMapping = Utils.getReflectUtil().getAnnotation(PostMapping.class, method);
        GetMapping getMapping = Utils.getReflectUtil().getAnnotation(GetMapping.class, method);

        if (Objects.isNull(requestMapping) && Objects.isNull(postMapping) && Objects.isNull(getMapping)) {
            return apiPath;
        }
        return apiPath + method.getName();
    }
}
