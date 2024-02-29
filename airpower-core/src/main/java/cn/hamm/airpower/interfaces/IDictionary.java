package cn.hamm.airpower.interfaces;

/**
 * <h1>枚举字典标准接口</h1>
 *
 * @author Hamm
 */
public interface IDictionary {
    /**
     * 获取枚举的key
     *
     * @return key
     */
    @SuppressWarnings("unused")
    int getKey();

    /**
     * 获取枚举的描述
     *
     * @return 描述
     */
    @SuppressWarnings("unused")
    String getLabel();

    /**
     * 判断Key是否相等
     *
     * @param key 被判断的Key
     * @return 对比结果
     */
    default boolean equalsKey(int key) {
        return this.getKey() == key;
    }
}
