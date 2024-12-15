package cn.hamm.airpower.util;

import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.interfaces.ITree;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * <h1>树结构处理工具类</h1>
 *
 * @author Hamm.cn
 */
public class TreeUtil {
    /**
     * <h3>禁止外部实例化</h3>
     */
    @Contract(pure = true)
    private TreeUtil() {
    }

    /**
     * <h3>生成树结构</h3>
     *
     * @param list 原始数据列表
     * @param <E>  泛型
     * @return 树结构数组
     */
    public static <E extends ITree<E>> List<E> buildTreeList(List<E> list) {
        return buildTreeList(list, Constant.ZERO_LONG);
    }

    /**
     * <h3>生成树结构</h3>
     *
     * @param list     原始数据列表
     * @param parentId 父级 {@code ID}
     * @param <E>      泛型
     * @return 数结构数组
     */
    private static <E extends ITree<E>> List<E> buildTreeList(@NotNull List<E> list, Long parentId) {
        return list.stream()
                .filter(item -> Objects.equals(parentId, item.getParentId()))
                .peek(item -> item.setChildren(
                        buildTreeList(list, item.getId())
                ))
                .toList();
    }
}
