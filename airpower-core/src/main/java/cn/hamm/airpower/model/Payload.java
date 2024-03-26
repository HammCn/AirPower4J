package cn.hamm.airpower.model;

import lombok.Getter;

/**
 * <h1>负载模型</h1>
 *
 * @author hamm
 */
@Getter
public class Payload<T> {
    /**
     * <h2>键名</h2>
     */
    private String key;

    /**
     * <h2>负载的值</h2>
     */
    private T value;

    /**
     * <h2>设置键名</h2>
     *
     * @param key 键名
     * @return 负载实例
     */
    public Payload<T> setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * <h2>设置负载的值</h2>
     *
     * @param value 负载的值
     * @return 负载实例
     */
    public Payload<T> setValue(T value) {
        this.value = value;
        return this;
    }

    /**
     * <h2>创建一个负载实例</h2>
     *
     * @param key   键名
     * @param value 负载的值
     * @return 负载实例
     */
    public static <T> Payload<T> create(String key, T value) {
        return new Payload<T>().setKey(key).setValue(value);
    }
}
