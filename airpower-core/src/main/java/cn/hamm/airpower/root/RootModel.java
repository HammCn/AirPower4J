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
    public final M copy() {
        try {
            M target = (M) getClass().getConstructor().newInstance();
            BeanUtils.copyProperties(this, target);
            return target;
        } catch (Exception exception) {
            log.error("复制到实例失败", exception);
            throw new ResultException(Result.ERROR.getCode(), exception.getMessage());
        }
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
    private void excludeBy(Class<?> filter, @NotNull Field field) {
        Exclude fieldExclude = field.getAnnotation(Exclude.class);
        if (Objects.isNull(fieldExclude)) {
            filterFieldPayload(field);
            return;
        }
        Class<?>[] excludeClasses = fieldExclude.filters();

        boolean isNeedClear = true;
        if (excludeClasses.length > 0) {
            isNeedClear = Arrays.stream(excludeClasses)
                    .anyMatch(excludeClass ->
                            !Void.class.equals(filter) && filter.equals(excludeClass)
                    );
        }
        if (isNeedClear) {
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
    private void exposeBy(Class<?> filter, @NotNull Field field) {
        Expose fieldExpose = field.getAnnotation(Expose.class);
        if (Objects.isNull(fieldExpose)) {
            // 没有标记 则直接移除掉
            ReflectUtil.clearFieldValue(this, field);
            filterFieldPayload(field);
            return;
        }
        Class<?>[] exposeClasses = fieldExpose.filters();

        if (exposeClasses.length > 0) {
            boolean isExpose = Arrays.stream(exposeClasses).anyMatch(
                    // Void 或者标记了暴露
                    exposeClass -> Void.class.equals(filter) || filter.equals(exposeClass)
            );
            if (!isExpose) {
                ReflectUtil.clearFieldValue(this, field);
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
            ReflectUtil.setFieldValue(this, field,
                    ((RootModel<?>) fieldValue).filterResponseDataBy(WhenPayLoad.class)
            );
        }
    }
}
