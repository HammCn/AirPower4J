package cn.hamm.airpower.interfaces;

import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.exception.ServiceException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * <h1>响应结果</h1>
 *
 * @author Hamm.cn
 */
public interface IException {
    /**
     * <h2>获取错误代码</h2>
     *
     * @return 错误代码
     */
    int getCode();

    /**
     * <h2>获取返回信息</h2>
     *
     * @return 返回信息
     */
    String getMessage();

    /**
     * <h2>创建一个自定义异常</h2>
     *
     * @return 异常
     */
    @Contract(" -> new")
    private @NotNull ServiceException create() {
        return new ServiceException(getCode(), getMessage());
    }

    /**
     * <h2>抛出异常</h2>
     */
    default void show() {
        show(getMessage());
    }

    /**
     * <h2>抛出异常</h2>
     *
     * @param message 返回信息
     */
    default void show(String message) {
        show(message, null);
    }

    /**
     * <h2>抛出异常</h2>
     *
     * @param message 返回信息
     * @param data    返回数据
     */
    default void show(String message, Object data) {
        throw create().setMessage(message).setData(data);
    }

    /**
     * <h2>当 {@code 满足条件} 时抛出异常</h2>
     *
     * @param condition 条件
     */
    default void when(boolean condition) {
        when(condition, getMessage());
    }

    /**
     * <h2>当 {@code 满足条件} 时抛出异常</h2>
     *
     * @param condition 条件
     * @param message   返回信息
     */
    default void when(boolean condition, String message) {
        if (condition) {
            show(message);
        }
    }

    /**
     * <h2>当 {@code 满足条件} 时抛出异常</h2>
     *
     * @param condition 条件
     * @param message   返回信息
     * @param data      数据
     */
    default void when(boolean condition, String message, Object data) {
        if (condition) {
            show(message, data);
        }
    }

    /**
     * <h2>当为 {@code null} 时抛出异常</h2>
     *
     * @param obj 被验证的数据
     */
    @Contract("null -> fail")
    default void whenNull(Object obj) {
        whenNull(obj, getMessage());
    }

    /**
     * <h2>当为 {@code null} 时抛出异常</h2>
     *
     * @param obj     被验证的数据
     * @param message 返回信息
     */
    @Contract("null, _ -> fail")
    default void whenNull(Object obj, String message) {
        when(Objects.isNull(obj), message);
    }

    /**
     * <h2>当 {@code 两者相同} 时抛出异常</h2>
     *
     * @param obj1 被验证的数据
     * @param obj2 被验证的数据
     */
    default void whenEquals(Object obj1, Object obj2) {
        whenEquals(obj1, obj2, getMessage());
    }

    /**
     * <h2>当 {@code 两者相同} 时抛出异常</h2>
     *
     * @param obj1    被验证的数据
     * @param obj2    被验证的数据
     * @param message 返回信息
     */
    default void whenEquals(@NotNull Object obj1, Object obj2, String message) {
        when(obj1.equals(obj2), message);
    }

    /**
     * <h2>当 {@code 两个字符串相同} 时抛出异常</h2>
     *
     * @param str1 被验证的数据
     * @param str2 被验证的数据
     */
    default void whenEquals(String str1, String str2) {
        whenEquals(str1, str2, getMessage());
    }

    /**
     * <h2>当 {@code 两个字符串相同} 时抛出异常</h2>
     *
     * @param str1    被验证的数据
     * @param str2    被验证的数据
     * @param message 返回信息
     */
    default void whenEquals(@NotNull String str1, String str2, String message) {
        when(str1.equals(str2), message);
    }

    /**
     * <h2>当 {@code 两个字符串忽略大小写相同} 时抛出异常</h2>
     *
     * @param str1 被验证的数据
     * @param str2 被验证的数据
     */
    default void whenEqualsIgnoreCase(String str1, String str2) {
        whenEqualsIgnoreCase(str1, str2, getMessage());
    }

    /**
     * <h2>当 {@code 两个字符串忽略大小写相同} 时抛出异常</h2>
     *
     * @param str1    被验证的数据
     * @param str2    被验证的数据
     * @param message 返回信息
     */
    default void whenEqualsIgnoreCase(@NotNull String str1, String str2, String message) {
        when(str1.equalsIgnoreCase(str2), message);
    }

    /**
     * <h2>当 <s>{@code 两者不相同}</s> 时抛出异常</h2>
     *
     * @param obj1 被验证的数据
     * @param obj2 被验证的数据
     */
    default void whenNotEquals(Object obj1, Object obj2) {
        whenNotEquals(obj1, obj2, getMessage());
    }

    /**
     * <h2>当 <s>{@code 两者不相同}</s> 时抛出异常</h2>
     *
     * @param obj1    被验证的数据
     * @param obj2    被验证的数据
     * @param message 返回信息
     */
    default void whenNotEquals(@NotNull Object obj1, Object obj2, String message) {
        when(!obj1.equals(obj2), message);
    }

    /**
     * <h2>当 <s>{@code 两个字符串不相同}</s> 时抛出异常</h2>
     *
     * @param str1 被验证的数据
     * @param str2 被验证的数据
     */
    default void whenNotEquals(String str1, String str2) {
        whenNotEquals(str1, str2, getMessage());
    }

    /**
     * <h2>当 <s>{@code 两个字符串不相同}</s> 时抛出异常</h2>
     *
     * @param str1    被验证的数据
     * @param str2    被验证的数据
     * @param message 返回信息
     */
    default void whenNotEquals(@NotNull String str1, String str2, String message) {
        when(!str1.equals(str2), message);
    }

    /**
     * <h2>当 <s>{@code 两个字符串忽略大小写还不相同}</s> 时抛出异常</h2>
     *
     * @param str1 被验证的数据
     * @param str2 被验证的数据
     */
    default void whenNotEqualsIgnoreCase(String str1, String str2) {
        whenNotEqualsIgnoreCase(str1, str2, getMessage());
    }

    /**
     * <h2>当 <s>{@code 两个字符串忽略大小写还不相同}</s> 时抛出异常</h2>
     *
     * @param str1    被验证的数据
     * @param str2    被验证的数据
     * @param message 返回信息
     */
    default void whenNotEqualsIgnoreCase(@NotNull String str1, String str2, String message) {
        when(!str1.equalsIgnoreCase(str2), message);
    }

    /**
     * <h2>当为 {@code null或空字符串} 时抛出异常</h2>
     *
     * @param obj 被验证的数据
     */
    default void whenEmpty(Object obj) {
        whenEmpty(obj, getMessage());
    }

    /**
     * <h2>当为 {@code null或空字符串} 时抛出异常</h2>
     *
     * @param obj     被验证的数据
     * @param message 返回信息
     */
    default void whenEmpty(Object obj, String message) {
        when(Objects.isNull(obj) || Constant.EMPTY_STRING.equalsIgnoreCase(obj.toString()), message);
    }

    /**
     * <h2>当 <s>{@code 不为null}</s> 时抛出异常</h2>
     *
     * @param obj 被验证的数据
     */
    default void whenNotNull(Object obj) {
        whenNotNull(obj, getMessage());
    }

    /**
     * <h2>当 <s>{@code 不为null}</s> 时抛出异常</h2>
     *
     * @param obj     被验证的数据
     * @param message 返回信息
     */
    default void whenNotNull(Object obj, String message) {
        when(!Objects.isNull(obj), message);
    }
}
