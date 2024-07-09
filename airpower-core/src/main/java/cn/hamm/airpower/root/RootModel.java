package cn.hamm.airpower.root;

import cn.hamm.airpower.annotation.*;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.interfaces.IAction;
import cn.hamm.airpower.util.CollectionUtil;
import cn.hamm.airpower.util.ReflectUtil;
import cn.hamm.airpower.util.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    @JsonIgnore
    private final ReflectUtil reflectUtil;

    @Contract(pure = true)
    public RootModel() {
        reflectUtil = Utils.getReflectUtil();
    }

    /**
     * <h2>忽略只读字段</h2>
     */
    public final void ignoreReadOnlyFields() {
        Utils.getReflectUtil().getFieldList(this.getClass()).stream()
                .filter(field -> Objects.nonNull(Utils.getReflectUtil().getAnnotation(ReadOnly.class, field)))
                .forEach(field -> Utils.getReflectUtil().clearFieldValue(this, field));
    }

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
     * <h2>过滤和脱敏</h2>
     *
     * @param filterClass   过滤器类
     * @param isDesensitize 是否需要脱敏
     * @return 实体
     * @see #filterAndDesensitize(Filter, boolean)
     * @see #desensitize(Field)
     * @see #filter(Class)
     */
    public final M filterAndDesensitize(@NotNull Class<?> filterClass, boolean isDesensitize) {
        Class<M> clazz = (Class<M>) this.getClass();
        List<Field> allFields = reflectUtil.getFieldList(clazz);
        Exclude exclude = clazz.getAnnotation(Exclude.class);
        // 类中没有标排除 则所有字段全暴露 走黑名单
        boolean isExpose = Objects.nonNull(exclude) && Arrays.asList(exclude.filters()).contains(filterClass);
        BiConsumer<@NotNull Field, @NotNull Class<?>> task = isExpose ? this::exposeBy : this::excludeBy;
        Consumer<@NotNull Field> desensitize = this::desensitize;
        allFields.forEach(field -> {
            if (!Void.class.equals(filterClass)) {
                task.accept(field, filterClass);
                filterFieldPayload(field, isDesensitize);
            }
            if (isDesensitize) {
                desensitize.accept(field);
            }
        });
        return (M) this;
    }

    /**
     * <h2>脱敏字段</h2>
     *
     * @return 实体
     * @see #filterAndDesensitize(Class, boolean)
     * @see #filterAndDesensitize(Filter, boolean)
     * @see #filter(Class)
     */
    public final M deserialize() {
        return filterAndDesensitize(Void.class, true);
    }

    /**
     * <h2>过滤字段</h2>
     *
     * @param filterClass 过滤器
     * @return 实体
     * @see #filterAndDesensitize(Class, boolean)
     * @see #filterAndDesensitize(Filter, boolean)
     * @see #desensitize(Field)
     */
    public final M filter(Class<?> filterClass) {
        return filterAndDesensitize(filterClass, false);
    }

    /**
     * <h2>过滤和脱敏</h2>
     *
     * @param filter        过滤器注解
     * @param isDesensitize 是否需要脱敏
     * @return 实体
     * @see #filterAndDesensitize(Class, boolean)
     * @see #filter(Class)
     * @see #desensitize(Field)
     */
    public final M filterAndDesensitize(@Nullable Filter filter, boolean isDesensitize) {
        if (Objects.isNull(filter)) {
            return filterAndDesensitize(Void.class, isDesensitize);
        }
        return filterAndDesensitize(filter.value(), isDesensitize);
    }

    /**
     * <h2>通过指定的过滤器排除字段</h2>
     *
     * @param field       字段
     * @param filterClass 过滤器
     */
    private void excludeBy(@NotNull Field field, @NotNull Class<?> filterClass) {
        Class<?>[] excludeClasses = null;
        final String fieldGetter = Constant.GET + StringUtils.capitalize(field.getName());
        try {
            Method getMethod = getClass().getMethod(fieldGetter);
            Exclude methodExclude = reflectUtil.getAnnotation(Exclude.class, getMethod);
            if (Objects.nonNull(methodExclude)) {
                // 属性的Getter上标记了排除
                excludeClasses = methodExclude.filters();
            }
        } catch (NoSuchMethodException exception) {
            log.error(exception.getMessage(), exception);
        }
        if (Objects.isNull(excludeClasses)) {
            Exclude fieldExclude = reflectUtil.getAnnotation(Exclude.class, field);
            if (Objects.isNull(fieldExclude)) {
                // 属性Getter没标记 也没有属性本身标记 则暴露
                return;
            }
            // 属性Getter没标记 但是属性本身标记了
            excludeClasses = fieldExclude.filters();
        }

        boolean isNeedClear = true;
        if (excludeClasses.length > 0) {
            isNeedClear = Arrays.asList(excludeClasses).contains(filterClass);
        }
        if (isNeedClear) {
            reflectUtil.clearFieldValue(this, field);
        }
    }

    /**
     * <h2>通过指定的过滤器暴露字段</h2>
     *
     * @param field       字段
     * @param filterClass 过滤器
     */
    private void exposeBy(@NotNull Field field, @NotNull Class<?> filterClass) {
        Class<?>[] exposeClasses = null;
        final String fieldGetter = Constant.GET + StringUtils.capitalize(field.getName());
        try {
            Method getMethod = getClass().getMethod(fieldGetter);
            Expose methodExpose = reflectUtil.getAnnotation(Expose.class, getMethod);
            if (Objects.nonNull(methodExpose)) {
                // 属性的Getter标记了暴露
                exposeClasses = methodExpose.filters();
            }
            // 属性的Getter没有标记
        } catch (NoSuchMethodException exception) {
            log.error(exception.getMessage(), exception);
        }
        if (Objects.isNull(exposeClasses)) {
            Expose fieldExpose = reflectUtil.getAnnotation(Expose.class, field);
            if (Objects.isNull(fieldExpose)) {
                // 属性以及Getter都没有标记暴露 则排除
                reflectUtil.clearFieldValue(this, field);
                return;
            }
            exposeClasses = fieldExpose.filters();
        }
        if (exposeClasses.length == 0) {
            // 虽然标记但未指定过滤器 所有场景都暴露
            return;
        }
        boolean isExpose = Arrays.asList(exposeClasses).contains(filterClass);
        if (!isExpose) {
            // 当前场景不在标记的暴露场景中 则排除
            reflectUtil.clearFieldValue(this, field);
        }
    }

    /**
     * <h2>挂载数据的Payload过滤</h2>
     *
     * @param field         字段
     * @param isDesensitize 是否需要脱敏
     */
    private void filterFieldPayload(@NotNull Field field, boolean isDesensitize) {
        Payload payload = reflectUtil.getAnnotation(Payload.class, field);
        if (Objects.isNull(payload)) {
            return;
        }
        Object fieldValue = reflectUtil.getFieldValue(this, field);
        Collection<RootModel<?>> collection;
        if (fieldValue instanceof Collection<?>) {
            Class<?> fieldClass = field.getType();
            CollectionUtil collectionUtil = Utils.getCollectionUtil();
            collection = collectionUtil.getCollectWithoutNull(
                    (Collection<RootModel<?>>) fieldValue, fieldClass
            );
            collection.forEach(item -> item.filterAndDesensitize(WhenPayLoad.class, isDesensitize));
            reflectUtil.setFieldValue(this, field, collection);
            return;
        }
        if (Objects.isNull(fieldValue)) {
            return;
        }
        reflectUtil.setFieldValue(this, field,
                ((RootModel<?>) fieldValue).filterAndDesensitize(WhenPayLoad.class, isDesensitize)
        );
    }

    /**
     * <h2>字段脱敏</h2>
     *
     * @param field 字段
     */
    private void desensitize(@NotNull Field field) {
        Desensitize desensitize = reflectUtil.getAnnotation(Desensitize.class, field);
        if (Objects.isNull(desensitize)) {
            return;
        }
        Object value = reflectUtil.getFieldValue(this, field);
        if (Objects.isNull(value)) {
            return;
        }
        if (!(value instanceof String valueString)) {
            return;
        }
        reflectUtil.setFieldValue(this, field,
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
