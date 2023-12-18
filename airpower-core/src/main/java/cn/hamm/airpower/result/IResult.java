package cn.hamm.airpower.result;

import java.util.Objects;

/**
 * <h1>返回数据的接口</h1>
 *
 * @author Hamm
 */
@SuppressWarnings("unused")
public interface IResult {
    /**
     * 获取错误代码
     *
     * @return 错误代码
     */
    int getCode();

    /**
     * 获取返回信息
     *
     * @return 返回信息
     */
    String getMessage();

    /**
     * 创建一个自定义异常
     *
     * @return 异常
     */
    private ResultException create() {
        return new ResultException(getCode(), getMessage());
    }


    /**
     * 抛出异常
     */
    default void show() {
        show(getMessage());
    }

    /**
     * 抛出异常
     *
     * @param message 返回信息
     */
    default void show(String message) {
        show(message, null);
    }

    /**
     * 抛出异常
     *
     * @param message 返回信息
     * @param data    返回数据
     */
    default void show(String message, Object data) {
        throw create().setMessage(message).setData(data);
    }

    /**
     * 当满足条件时抛出异常
     *
     * @param condition 条件
     */
    default void when(boolean condition) {
        when(condition, getMessage());
    }

    /**
     * 当满足条件时抛出异常
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
     * 当满足条件时抛出异常
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
     * 当为null时抛出异常
     *
     * @param obj 被验证的数据
     */
    default void whenNull(Object obj) {
        whenNull(obj, getMessage());
    }

    /**
     * 当为null时抛出异常
     *
     * @param obj     被验证的数据
     * @param message 返回信息
     */
    default void whenNull(Object obj, String message) {
        when(Objects.isNull(obj), message);
    }

    /**
     * 当两者相同时抛出异常
     *
     * @param obj1 被验证的数据
     * @param obj2 被验证的数据
     */
    default void whenEquals(Object obj1, Object obj2) {
        whenEquals(obj1, obj2, getMessage());
    }

    /**
     * 当两者相同时抛出异常
     *
     * @param obj1    被验证的数据
     * @param obj2    被验证的数据
     * @param message 返回信息
     */
    default void whenEquals(Object obj1, Object obj2, String message) {
        when(obj1.equals(obj2), message);
    }

    /**
     * 当两个字符串相同时抛出异常
     *
     * @param str1 被验证的数据
     * @param str2 被验证的数据
     */
    default void whenEquals(String str1, String str2) {
        whenEquals(str1, str2, getMessage());
    }

    /**
     * 当两个字符串相同时抛出异常
     *
     * @param str1    被验证的数据
     * @param str2    被验证的数据
     * @param message 返回信息
     */
    default void whenEquals(String str1, String str2, String message) {
        when(str1.equals(str2), message);
    }

    /**
     * 当两个字符串忽略大小写相同时抛出异常
     *
     * @param str1 被验证的数据
     * @param str2 被验证的数据
     */
    default void whenEqualsIgnoreCase(String str1, String str2) {
        whenEqualsIgnoreCase(str1, str2, getMessage());
    }

    /**
     * 当两个字符串忽略大小写相同时抛出异常
     *
     * @param str1    被验证的数据
     * @param str2    被验证的数据
     * @param message 返回信息
     */
    default void whenEqualsIgnoreCase(String str1, String str2, String message) {
        when(str1.equalsIgnoreCase(str2), message);
    }

    /**
     * 当两者不相同时抛出异常
     *
     * @param obj1 被验证的数据
     * @param obj2 被验证的数据
     */
    default void whenNotEquals(Object obj1, Object obj2) {
        whenNotEquals(obj1, obj2, getMessage());
    }

    /**
     * 当两者不相同时抛出异常
     *
     * @param obj1    被验证的数据
     * @param obj2    被验证的数据
     * @param message 返回信息
     */
    default void whenNotEquals(Object obj1, Object obj2, String message) {
        when(!obj1.equals(obj2), message);
    }

    /**
     * 当两个字符串不相同时抛出异常
     *
     * @param str1 被验证的数据
     * @param str2 被验证的数据
     */
    default void whenNotEquals(String str1, String str2) {
        whenNotEquals(str1, str2, getMessage());
    }

    /**
     * 当两个字符串不相同时抛出异常
     *
     * @param str1    被验证的数据
     * @param str2    被验证的数据
     * @param message 返回信息
     */
    default void whenNotEquals(String str1, String str2, String message) {
        when(!str1.equals(str2), message);
    }

    /**
     * 当两个字符串忽略大小写还不相同时抛出异常
     *
     * @param str1 被验证的数据
     * @param str2 被验证的数据
     */
    default void whenNotEqualsIgnoreCase(String str1, String str2) {
        whenNotEqualsIgnoreCase(str1, str2, getMessage());
    }

    /**
     * 当两个字符串忽略大小写还不相同时抛出异常
     *
     * @param str1    被验证的数据
     * @param str2    被验证的数据
     * @param message 返回信息
     */
    default void whenNotEqualsIgnoreCase(String str1, String str2, String message) {
        when(!str1.equalsIgnoreCase(str2), message);
    }

    /**
     * 当为null或空字符串时抛出异常
     *
     * @param obj 被验证的数据
     */
    default void whenEmpty(Object obj) {
        whenEmpty(obj, getMessage());
    }

    /**
     * 当为null或空字符串时抛出异常
     *
     * @param obj     被验证的数据
     * @param message 返回信息
     */
    default void whenEmpty(Object obj, String message) {
        when(Objects.isNull(obj) || "".equalsIgnoreCase(obj.toString()), message);
    }

    /**
     * 当不为null时抛出异常
     *
     * @param obj 被验证的数据
     */
    default void whenNotNull(Object obj) {
        whenNotNull(obj, getMessage());
    }

    /**
     * 当不为null时抛出异常
     *
     * @param obj     被验证的数据
     * @param message 返回信息
     */
    default void whenNotNull(Object obj, String message) {
        when(!Objects.isNull(obj), message);
    }
}
