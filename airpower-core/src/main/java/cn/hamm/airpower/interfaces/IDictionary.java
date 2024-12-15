package cn.hamm.airpower.interfaces;

import cn.hamm.airpower.util.ReflectUtil;

/**
 * <h1>枚举字典标准接口</h1>
 *
 * @author Hamm.cn
 * @see ReflectUtil
 */
public interface IDictionary {
    /**
     * <h3>获取枚举的 {@code Key}</h3>
     *
     * @return {@code Key}
     */
    int getKey();

    /**
     * <h3>获取枚举的描述</h3>
     *
     * @return 描述
     */
    String getLabel();

    /**
     * <h3>判断 {@code Key} 是否相等</h3>
     *
     * @param key 被判断的 {@code Key}
     * @return 对比结果
     */
    default boolean equalsKey(int key) {
        return getKey() == key;
    }

    /**
     * <h3>判断 {@code Key} 是否不相等</h3>
     *
     * @param key 被判断的 {@code Key}
     * @return 对比结果
     */
    default boolean notEqualsKey(int key) {
        return !equalsKey(key);
    }
}
