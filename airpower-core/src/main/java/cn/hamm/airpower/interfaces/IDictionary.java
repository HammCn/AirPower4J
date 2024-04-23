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
     * <h2>获取枚举的key</h2>
     *
     * @return key
     */
    int getKey();

    /**
     * <h2>获取枚举的描述</h2>
     *
     * @return 描述
     */
    String getLabel();

    /**
     * <h2>判断Key是否相等</h2>
     *
     * @param key 被判断的Key
     * @return 对比结果
     */
    default boolean equalsKey(int key) {
        return this.getKey() == key;
    }
}
