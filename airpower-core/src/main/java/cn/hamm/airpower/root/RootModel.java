package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.*;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.interfaces.IAction;
import cn.hamm.airpower.util.Utils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
            log.error(exception.getMessage(), exception);
            throw new ServiceException(exception);
        }
    }

    /**
     * <h2>通过指定的过滤器来过滤响应数据</h2>
     *
     * @param filterClass 过滤器类
     * @return 实体
     */
    public final M filter(@NotNull Class<?> filterClass) {
        Class<M> clazz = (Class<M>) this.getClass();
        List<Field> allFields = Utils.getReflectUtil().getFieldList(clazz);
        Exclude exclude = clazz.getAnnotation(Exclude.class);
        // 类中没有标排除 则所有字段全暴露 走黑名单
        BiConsumer<@NotNull Field, @NotNull Class<?>> task = Objects.nonNull(exclude) ? this::exposeBy : this::excludeBy;
        Consumer<@NotNull Field> desensitize = this::desensitize;
        allFields.forEach(field -> {
            if (!Void.class.equals(filterClass)) {
                task.accept(field, filterClass);
            }
            desensitize.accept(field);
        });
        return (M) this;
    }

    /**
     * <h2>通过指定的过滤注解来过滤数据</h2>
     *
     * @param filter 过滤器注解
     * @return 实体
     */
    public final M filter(@Nullable Filter filter) {
        if (Objects.isNull(filter)) {
            return filter(Void.class);
        }
        return filter(filter.value());
    }

    /**
     * <h2>通过指定的过滤器排除字段</h2>
     *
     * @param field       字段
     * @param filterClass 过滤器
     */
    private void excludeBy(@NotNull Field field, @NotNull Class<?> filterClass) {
        Exclude fieldExclude = Utils.getReflectUtil().getAnnotation(Exclude.class, field);
        if (Objects.isNull(fieldExclude)) {
            filterFieldPayload(field);
            return;
        }
        Class<?>[] excludeClasses = fieldExclude.filters();

        boolean isNeedClear = true;
        if (excludeClasses.length > 0) {
            isNeedClear = Arrays.asList(excludeClasses).contains(filterClass);
        }
        if (isNeedClear) {
            Utils.getReflectUtil().clearFieldValue(this, field);
        }
        //如果是挂载数据
        filterFieldPayload(field);
    }

    /**
     * <h2>通过指定的过滤器暴露字段</h2>
     *
     * @param field       字段
     * @param filterClass 过滤器
     */
    private void exposeBy(@NotNull Field field, @NotNull Class<?> filterClass) {
        Expose fieldExpose = Utils.getReflectUtil().getAnnotation(Expose.class, field);
        if (Objects.isNull(fieldExpose)) {
            // 没有标记 则直接移除掉
            Utils.getReflectUtil().clearFieldValue(this, field);
            filterFieldPayload(field);
            return;
        }
        Class<?>[] exposeClasses = fieldExpose.filters();

        if (exposeClasses.length > 0) {
            // 标记了暴露
            boolean isExpose = Arrays.asList(exposeClasses).contains(filterClass);
            if (!isExpose) {
                Utils.getReflectUtil().clearFieldValue(this, field);
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
        Payload payload = Utils.getReflectUtil().getAnnotation(Payload.class, field);
        if (Objects.isNull(payload)) {
            return;
        }
        Object fieldValue = Utils.getReflectUtil().getFieldValue(this, field);
        Collection<RootModel<?>> collection;
        if (fieldValue instanceof Collection<?>) {
            Class<?> fieldClass = field.getType();
            collection = Utils.getCollectionUtil().getCollectWithoutNull(
                    (Collection<RootModel<?>>) fieldValue, fieldClass
            );
            collection.forEach(item -> item.filter(WhenPayLoad.class));
            Utils.getReflectUtil().setFieldValue(this, field, collection);
            return;
        }
        if (Objects.nonNull(fieldValue)) {
            Utils.getReflectUtil().setFieldValue(this, field,
                    ((RootModel<?>) fieldValue).filter(WhenPayLoad.class)
            );
        }
    }

    /**
     * <h2>字段脱敏</h2>
     *
     * @param field 字段
     */
    private void desensitize(@NotNull Field field) {
        Desensitize desensitize = Utils.getReflectUtil().getAnnotation(Desensitize.class, field);
        if (Objects.isNull(desensitize)) {
            return;
        }
        Object value = Utils.getReflectUtil().getFieldValue(this, field);
        if (Objects.isNull(value)) {
            return;
        }
        if (value instanceof String valueString) {
            Utils.getReflectUtil().setFieldValue(this, field,
                    Utils.getStringUtil().desensitize(
                            valueString,
                            desensitize.value(),
                            desensitize.head(),
                            desensitize.tail(),
                            desensitize.symbol()
                    )
            );
        }
    }
}
