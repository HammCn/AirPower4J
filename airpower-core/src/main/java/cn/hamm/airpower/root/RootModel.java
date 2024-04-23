package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.Exclude;
import cn.hamm.airpower.annotation.Expose;
import cn.hamm.airpower.annotation.Payload;
import cn.hamm.airpower.interfaces.IAction;
import cn.hamm.airpower.result.Result;
import cn.hamm.airpower.result.ResultException;
import cn.hamm.airpower.util.CollectionUtil;
import cn.hamm.airpower.util.ReflectUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <h1>数据根模型</h1>
 *
 * @author Hamm.cn
 */
@Getter
@Slf4j
@EqualsAndHashCode
@SuppressWarnings("unchecked")
public class RootModel<M extends RootModel<M>> implements IAction {
    /**
     * <h2>复制实例到新的实例</h2>
     *
     * @param clazz 目标类
     * @param <T>   返回类型
     * @return 返回实例
     */
    public final <T> T copyTo(Class<T> clazz) {
        try {
            T target = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(this, target);
            return target;
        } catch (Exception exception) {
            log.error("复制到实例失败", exception);
            throw new ResultException(Result.ERROR.getCode(), exception.getMessage());
        }
    }

    /**
     * <h2>排除传入的字段列表</h2>
     *
     * @param fieldNames 字段列表
     * @return 实体
     */
    public final M exclude(String... fieldNames) {
        List<String> list = new ArrayList<>(fieldNames.length);
        Collections.addAll(list, fieldNames);
        return exclude(list);
    }

    /**
     * <h2>排除传入的字段列表</h2>
     *
     * @param fieldNames 字段列表
     * @return 实体
     */
    public final M exclude(List<String> fieldNames) {
        List<Field> fieldList = ReflectUtil.getFieldList(this.getClass());
        for (Field field : fieldList) {
            for (String fieldName : fieldNames) {
                if (field.getName().equals(fieldName)) {
                    ReflectUtil.clearFieldValue(this, field);
                    break;
                }
            }
        }
        return (M) this;
    }

    /**
     * <h2>只暴露传入的字段列表</h2>
     *
     * @param fieldNames 字段列表
     * @return 实体
     */
    public final M expose(String... fieldNames) {
        List<Field> fieldList = ReflectUtil.getFieldList(this.getClass());
        for (Field field : fieldList) {
            boolean needReturn = false;
            for (String fieldName : fieldNames) {
                if (field.getName().equals(fieldName)) {
                    needReturn = true;
                    break;
                }
            }
            if (!needReturn) {
                ReflectUtil.clearFieldValue(this, field);
            }
        }
        return (M) this;
    }

    /**
     * <h2>通过指定的过滤器来过滤响应数据</h2>
     *
     * @param filter 过滤器
     * @return 实体
     */
    public final M filterResponseDataBy(Class<?> filter) {
        Class<M> clazz = (Class<M>) this.getClass();
        List<Field> allFields = ReflectUtil.getFieldList(clazz);

        Exclude exclude = clazz.getAnnotation(Exclude.class);
        if (Objects.nonNull(exclude)) {
            // 整个类过滤 判断哪些字段走白名单
            allFields.forEach(field -> exposeBy(filter, field));
            return (M) this;
        }
        // 类中没有标排除 则所有字段全暴露 走黑名单
        allFields.forEach(field -> excludeBy(filter, field));

        return (M) this;
    }

    /**
     * <h2>通过指定的过滤器排除字段</h2>
     *
     * @param filter 过滤器
     * @param field  字段
     */
    private void excludeBy(Class<?> filter, Field field) {
        Exclude fieldExclude = field.getAnnotation(Exclude.class);
        if (Objects.isNull(fieldExclude)) {
            filterFieldPayload(field);
            return;
        }
        Class<?>[] excludeClasses = fieldExclude.filters();
        if (excludeClasses.length == 0) {
            // 字段标记排除 但没有指定场景 则所有场景都排除
            ReflectUtil.clearFieldValue(this, field);
            //如果是挂载数据
            filterFieldPayload(field);
            return;
        }

        boolean isExclude = false;
        // 标了指定场景排除
        for (Class<?> excludeClass : excludeClasses) {
            if (!Void.class.equals(filter) && filter.equals(excludeClass)) {
                // 响应场景也被标在排除场景列表中
                isExclude = true;
                break;
            }
        }
        if (isExclude) {
            ReflectUtil.clearFieldValue(this, field);
        }
        //如果是挂载数据
        filterFieldPayload(field);
    }

    /**
     * <h2>通过指定的过滤器暴露字段</h2>
     *
     * @param filter 过滤器
     * @param field  字段
     */
    private void exposeBy(Class<?> filter, Field field) {
        Expose fieldExpose = field.getAnnotation(Expose.class);
        if (Objects.isNull(fieldExpose)) {
            // 没有标记 则直接移除掉
            ReflectUtil.clearFieldValue(this, field);
            filterFieldPayload(field);
            return;
        }
        boolean isExpose = false;
        Class<?>[] exposeClasses = fieldExpose.filters();

        if (exposeClasses.length == 0) {
            // 没有指定暴露的过滤器
            filterFieldPayload(field);
            return;
        }
        // 标了指定场景暴露
        for (Class<?> exposeClass : exposeClasses) {
            if (Void.class.equals(filter) || filter.equals(exposeClass)) {
                // 响应场景也被标在暴露场景列表中
                isExpose = true;
                break;
            }
        }
        if (!isExpose) {
            ReflectUtil.clearFieldValue(this, field);
        }
        filterFieldPayload(field);
    }

    /**
     * <h2>挂载数据的Payload过滤</h2>
     *
     * @param field 字段
     */
    private void filterFieldPayload(Field field) {
        Payload payload = field.getAnnotation(Payload.class);
        if (Objects.isNull(payload)) {
            return;
        }
        Object fieldValue = ReflectUtil.getFieldValue(this, field);
        Collection<RootModel<?>> collection;
        if (fieldValue instanceof Collection<?>) {
            Class<?> fieldClass = field.getType();
            collection = CollectionUtil.getCollectWithoutNull((Collection<RootModel<?>>) fieldValue, fieldClass);
            collection.forEach(item -> item.filterResponseDataBy(WhenPayLoad.class));
            ReflectUtil.setFieldValue(this, field, collection);
            return;
        }
        if (Objects.nonNull(fieldValue)) {
            ReflectUtil.setFieldValue(this, field, ((RootModel<?>) fieldValue).filterResponseDataBy(WhenPayLoad.class));
        }
    }
}
