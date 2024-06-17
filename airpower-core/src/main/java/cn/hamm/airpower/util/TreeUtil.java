package cn.hamm.airpower.util;

import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.interfaces.ITree;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <h1>树结构处理助手</h1>
 *
 * @author Hamm.cn
 */
@Component
public class TreeUtil {
    /**
     * <h2>生成树结构</h2>
     *
     * @param list 原始数据列表
     * @param <E>  泛型
     * @return 树结构数组
     */
    public final <E extends ITree<E>> List<E> buildTreeList(List<E> list) {
        return buildTreeList(list, Constant.ZERO_LONG);
    }

    /**
     * <h2>生成树结构</h2>
     *
     * @param list     原始数据列表
     * @param parentId 父级ID
     * @param <E>      泛型
     * @return 数结构数组
     */
    private <E extends ITree<E>> List<E> buildTreeList(@NotNull List<E> list, Long parentId) {
        return list.stream()
                .filter(item -> parentId.equals(item.getParentId()))
                .peek(item -> {
                    List<E> children = buildTreeList(list, item.getId());
                    item.setChildren(children);
                })
                .toList();
    }
}
