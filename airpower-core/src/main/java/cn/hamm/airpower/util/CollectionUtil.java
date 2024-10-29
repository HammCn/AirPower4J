package cn.hamm.airpower.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * <h1>内置的集合工具类</h1>
 *
 * @author Hamm.cn
 */
public class CollectionUtil {
    /**
     * <h2>禁止外部实例化</h2>
     */
    @Contract(pure = true)
    private CollectionUtil() {
    }

    /**
     * <h2>获取集合中的 {@code 非null} 元素</h2>
     *
     * @param list       原始集合
     * @param fieldClass 数据类型
     * @param <T>        数据类型
     * @return 处理后的集合
     */
    public static @NotNull <T> Collection<T> getCollectWithoutNull(Collection<T> list, Class<?> fieldClass) {
        if (Objects.equals(Set.class, fieldClass)) {
            return Objects.isNull(list) ? new HashSet<>() : list;
        }
        return Objects.isNull(list) ? new ArrayList<>() : list;
    }
}
