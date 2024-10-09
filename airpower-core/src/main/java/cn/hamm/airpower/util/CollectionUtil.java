package cn.hamm.airpower.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <h1>内置的集合工具类</h1>
 *
 * @author Hamm.cn
 */
@Component
public class CollectionUtil {
    /**
     * <h2>获取集合中的 {@code 非null} 元素</h2>
     *
     * @param list       原始集合
     * @param fieldClass 数据类型
     * @param <T>        数据类型
     * @return 处理后的集合
     */
    public final @NotNull <T> Collection<T> getCollectWithoutNull(Collection<T> list, Class<?> fieldClass) {
        if (Objects.equals(Set.class, fieldClass)) {
            return Objects.isNull(list) ? new HashSet<>() : list;
        }
        return Objects.isNull(list) ? new ArrayList<>() : list;
    }
}
