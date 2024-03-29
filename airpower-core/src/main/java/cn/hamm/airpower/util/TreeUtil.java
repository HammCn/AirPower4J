package cn.hamm.airpower.util;

import cn.hamm.airpower.interfaces.ITree;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>树结构处理助手</h1>
 *
 * @author Hamm
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
    public <E extends ITree<E>> List<E> buildTreeList(List<E> list) {
        return buildTreeList(list, 0L);
    }

    /**
     * <h2>生成树结构</h2>
     *
     * @param list     原始数据列表
     * @param parentId 父级ID
     * @param <E>      泛型
     * @return 数结构数组
     */
    private <E extends ITree<E>> List<E> buildTreeList(List<E> list, Long parentId) {
        List<E> eList = new ArrayList<>();
        for (E e : list) {
            if (parentId.equals(e.getParentId())) {
                List<E> children = buildTreeList(list, e.getId());
                e.setChildren(children);
                eList.add(e);
            }
        }
        return eList;
    }
}
