package cn.hamm.airpower.result;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

/**
 * <h1>返回数据的接口</h1>
 *
 * @author Hamm.cn
 */
public interface IResult {
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
    private ResultException create() {
        return new ResultException(getCode(), getMessage());
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
     * <h2>当满足条件时抛出异常</h2>
     *
     * @param condition 条件
     */
    default void when(boolean condition) {
        when(condition, getMessage());
    }

    /**
     * <h2>当满足条件时抛出异常</h2>
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
     * <h2>当满足条件时抛出异常</h2>
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
     * <h2>当为null时抛出异常</h2>
     *
     * @param obj 被验证的数据
     */
    @Contract("null -> fail")
    default void whenNull(Object obj) {
        whenNull(obj, getMessage());
    }

    /**
     * <h2>当为null时抛出异常</h2>
     *
     * @param obj     被验证的数据
     * @param message 返回信息
     */
    @Contract("null, _ -> fail")
    default void whenNull(Object obj, String message) {
        when(Objects.isNull(obj), message);
    }

    /**
     * <h2>当两者相同时抛出异常</h2>
     *
     * @param obj1 被验证的数据
     * @param obj2 被验证的数据
     */
    default void whenEquals(Object obj1, Object obj2) {
        whenEquals(obj1, obj2, getMessage());
    }

    /**
     * <h2>当两者相同时抛出异常</h2>
     *
     * @param obj1    被验证的数据
     * @param obj2    被验证的数据
     * @param message 返回信息
     */
    default void whenEquals(Object obj1, Object obj2, String message) {
        when(obj1.equals(obj2), message);
    }

    /**
     * <h2>当两个字符串相同时抛出异常</h2>
     *
     * @param str1 被验证的数据
     * @param str2 被验证的数据
     */
    default void whenEquals(String str1, String str2) {
        whenEquals(str1, str2, getMessage());
    }

    /**
     * <h2>当两个字符串相同时抛出异常</h2>
     *
     * @param str1    被验证的数据
     * @param str2    被验证的数据
     * @param message 返回信息
     */
    default void whenEquals(String str1, String str2, String message) {
        when(str1.equals(str2), message);
    }

    /**
     * <h2>当两个字符串忽略大小写相同时抛出异常</h2>
     *
     * @param str1 被验证的数据
     * @param str2 被验证的数据
     */
    default void whenEqualsIgnoreCase(String str1, String str2) {
        whenEqualsIgnoreCase(str1, str2, getMessage());
    }

    /**
     * <h2>当两个字符串忽略大小写相同时抛出异常</h2>
     *
     * @param str1    被验证的数据
     * @param str2    被验证的数据
     * @param message 返回信息
     */
    default void whenEqualsIgnoreCase(String str1, String str2, String message) {
        when(str1.equalsIgnoreCase(str2), message);
    }

    /**
     * <h2>当两者不相同时抛出异常</h2>
     *
     * @param obj1 被验证的数据
     * @param obj2 被验证的数据
     */
    default void whenNotEquals(Object obj1, Object obj2) {
        whenNotEquals(obj1, obj2, getMessage());
    }

    /**
     * <h2>当两者不相同时抛出异常</h2>
     *
     * @param obj1    被验证的数据
     * @param obj2    被验证的数据
     * @param message 返回信息
     */
    default void whenNotEquals(Object obj1, Object obj2, String message) {
        when(!obj1.equals(obj2), message);
    }

    /**
     * <h2>当两个字符串不相同时抛出异常</h2>
     *
     * @param str1 被验证的数据
     * @param str2 被验证的数据
     */
    default void whenNotEquals(String str1, String str2) {
        whenNotEquals(str1, str2, getMessage());
    }

    /**
     * <h2>当两个字符串不相同时抛出异常</h2>
     *
     * @param str1    被验证的数据
     * @param str2    被验证的数据
     * @param message 返回信息
     */
    default void whenNotEquals(String str1, String str2, String message) {
        when(!str1.equals(str2), message);
    }

    /**
     * <h2>当两个字符串忽略大小写还不相同时抛出异常</h2>
     *
     * @param str1 被验证的数据
     * @param str2 被验证的数据
     */
    default void whenNotEqualsIgnoreCase(String str1, String str2) {
        whenNotEqualsIgnoreCase(str1, str2, getMessage());
    }

    /**
     * <h2>当两个字符串忽略大小写还不相同时抛出异常</h2>
     *
     * @param str1    被验证的数据
     * @param str2    被验证的数据
     * @param message 返回信息
     */
    default void whenNotEqualsIgnoreCase(String str1, String str2, String message) {
        when(!str1.equalsIgnoreCase(str2), message);
    }

    /**
     * <h2>当为null或空字符串时抛出异常</h2>
     *
     * @param obj 被验证的数据
     */
    default void whenEmpty(Object obj) {
        whenEmpty(obj, getMessage());
    }

    /**
     * <h2>当为null或空字符串时抛出异常</h2>
     *
     * @param obj     被验证的数据
     * @param message 返回信息
     */
    default void whenEmpty(Object obj, String message) {
        when(Objects.isNull(obj) || "".equalsIgnoreCase(obj.toString()), message);
    }

    /**
     * <h2>当不为null时抛出异常</h2>
     *
     * @param obj 被验证的数据
     */
    default void whenNotNull(Object obj) {
        whenNotNull(obj, getMessage());
    }

    /**
     * <h2>当不为null时抛出异常</h2>
     *
     * @param obj     被验证的数据
     * @param message 返回信息
     */
    default void whenNotNull(Object obj, String message) {
        when(!Objects.isNull(obj), message);
    }
}
