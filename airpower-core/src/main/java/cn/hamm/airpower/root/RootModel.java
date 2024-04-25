package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.Exclude;
import cn.hamm.airpower.annotation.Expose;
import cn.hamm.airpower.annotation.Payload;
import cn.hamm.airpower.config.MessageConstant;
import cn.hamm.airpower.enums.Result;
import cn.hamm.airpower.exception.ResultException;
import cn.hamm.airpower.interfaces.IAction;
import cn.hamm.airpower.util.AirUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

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
     * <h2>复制一个新对象</h2>
     *
     * @return 返回实例
     */
    public final @NotNull M copy() {
        try {
            M target = (M) getClass().getConstructor().newInstance();
            BeanUtils.copyProperties(this, target);
            return target;
        } catch (Exception exception) {
            log.error(MessageConstant.EXCEPTION_WHEN_CREATE_INSTANCE, exception);
            throw new ResultException(Result.ERROR.getCode(), exception.getMessage());
        }
    }

    /**
     * <h2>通过指定的过滤器来过滤响应数据</h2>
     *
     * @param filter 过滤器
     * @return 实体
     */
    public final M filterResponseDataBy(@NotNull Class<?> filter) {
        Class<M> clazz = (Class<M>) this.getClass();
        List<Field> allFields = AirUtil.getReflectUtil().getFieldList(clazz);
        Exclude exclude = clazz.getAnnotation(Exclude.class);
        // 类中没有标排除 则所有字段全暴露 走黑名单
        BiConsumer<Class<?>, Field> task = Objects.nonNull(exclude) ? this::exposeBy : this::excludeBy;
        allFields.forEach(field -> task.accept(filter, field));
        return (M) this;
    }

    /**
     * <h2>通过指定的过滤器排除字段</h2>
     *
     * @param filter 过滤器
     * @param field  字段
     */
    private void excludeBy(@NotNull Class<?> filter, @NotNull Field field) {
        Exclude fieldExclude = AirUtil.getReflectUtil().getAnnotation(Exclude.class, field);
        if (Objects.isNull(fieldExclude)) {
            filterFieldPayload(field);
            return;
        }
        Class<?>[] excludeClasses = fieldExclude.filters();

        boolean isNeedClear = true;
        if (excludeClasses.length > 0) {
            isNeedClear = Arrays.asList(excludeClasses).contains(filter);
        }
        if (isNeedClear) {
            AirUtil.getReflectUtil().clearFieldValue(this, field);
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
    private void exposeBy(@NotNull Class<?> filter, @NotNull Field field) {
        Expose fieldExpose = AirUtil.getReflectUtil().getAnnotation(Expose.class, field);
        if (Objects.isNull(fieldExpose)) {
            // 没有标记 则直接移除掉
            AirUtil.getReflectUtil().clearFieldValue(this, field);
            filterFieldPayload(field);
            return;
        }
        Class<?>[] exposeClasses = fieldExpose.filters();

        if (exposeClasses.length > 0) {
            // 标记了暴露
            boolean isExpose = Arrays.asList(exposeClasses).contains(filter);
            if (!isExpose) {
                AirUtil.getReflectUtil().clearFieldValue(this, field);
            }
        }
        filterFieldPayload(field);
    }

    /**
     * <h2>挂载数据的Payload过滤</h2>
     *
     * @param field 字段
     */
    private void filterFieldPayload(@NotNull Field field) {
        Payload payload = AirUtil.getReflectUtil().getAnnotation(Payload.class, field);
        if (Objects.isNull(payload)) {
            return;
        }
        Object fieldValue = AirUtil.getReflectUtil().getFieldValue(this, field);
        Collection<RootModel<?>> collection;
        if (fieldValue instanceof Collection<?>) {
            Class<?> fieldClass = field.getType();
            collection = AirUtil.getCollectionUtil().getCollectWithoutNull(
                    (Collection<RootModel<?>>) fieldValue, fieldClass
            );
            collection.forEach(item -> item.filterResponseDataBy(WhenPayLoad.class));
            AirUtil.getReflectUtil().setFieldValue(this, field, collection);
            return;
        }
        if (Objects.nonNull(fieldValue)) {
            AirUtil.getReflectUtil().setFieldValue(this, field,
                    ((RootModel<?>) fieldValue).filterResponseDataBy(WhenPayLoad.class)
            );
        }
    }
}
