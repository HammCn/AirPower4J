package cn.hamm.airpower.model;

import lombok.Getter;

/**
 * 负载模型
 *
 * @author hamm
 */
@Getter
public class Payload<T> {
    /**
     * 键名
     */
    private String key;

    /**
     * 负载的值
     */
    private T value;

    /**
     * 设置键名
     *
     * @param key 键名
     * @return 负载实例
     */
    public Payload<T> setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * 设置负载的值
     *
     * @param value 负载的值
     * @return 负载实例
     */
    public Payload<T> setValue(T value) {
        this.value = value;
        return this;
    }

    /**
     * 创建一个负载实例
     *
     * @param key   键名
     * @param value 负载的值
     * @return 负载实例
     */
    public static <T> Payload<T> create(String key, T value) {
        return new Payload<T>().setKey(key).setValue(value);
    }
}
