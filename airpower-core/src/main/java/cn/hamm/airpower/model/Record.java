package cn.hamm.airpower.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录模型
 *
 * @author hamm
 */
@Getter
public class Record<K, V> {
    /**
     * 键名
     */
    private K key;

    /**
     * 值内容
     */
    private V value;


    /**
     * 设置键名
     *
     * @param key 键名
     * @return Record
     */
    public Record<K, V> setKey(K key) {
        this.key = key;
        return this;
    }

    /**
     * 设置负载的值
     *
     * @param value 值内容
     * @return Record
     */
    public Record<K, V> setValue(V value) {
        this.value = value;
        return this;
    }

    /**
     * 创建一个Record
     *
     * @param key   键名
     * @param value 值内容
     * @return Record
     */
    public static <K, V> Record<K, V> create(K key, V value) {
        return new Record<K, V>().setKey(key).setValue(value);
    }

    /**
     * 创建一个Record
     *
     * @param <K> 键名
     * @param <V> 值内容
     * @return Record
     */
    public static <K, V> Record<K, V> create() {
        return create(null, null);
    }

    /**
     * 创建一个Record列表
     *
     * @param <K> 键名 类型
     * @param <V> 值内容类型
     * @return Record列表
     */
    public static <K, V> List<Record<K, V>> createList() {
        return new ArrayList<>();
    }
}
