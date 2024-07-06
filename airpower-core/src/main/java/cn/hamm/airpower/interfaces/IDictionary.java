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
     * <h2>获取枚举的 <code>Key</code></h2>
     *
     * @return <code>Key</code>
     */
    int getKey();

    /**
     * <h2>获取枚举的描述</h2>
     *
     * @return 描述
     */
    String getLabel();

    /**
     * <h2>判断 <code>Key</code> 是否相等</h2>
     *
     * @param key 被判断的 <code>Key</code>
     * @return 对比结果
     */
    default boolean equalsKey(int key) {
        return this.getKey() == key;
    }

    /**
     * <h2>判断 <code>Key</code> 是否不相等</h2>
     *
     * @param key 被判断的 <code>Key</code>
     * @return 对比结果
     */
    default boolean notEqualsKey(int key) {
        return !equalsKey(key);
    }
}
