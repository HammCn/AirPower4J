package cn.hamm.airpower.interfaces;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <h1>树结构的Service</h1>
 *
 * @author Hamm.cn
 */
public interface IServiceTree<E extends ITree<E>> extends IService<E> {

    /**
     * <h2>获取所有子节点</h2>
     *
     * @param list 树结构列表
     * @return 包含所有直接点的树结构列表
     */
    default List<E> getAllChildren(@NotNull List<E> list) {
        list.forEach(item -> {
            List<E> children = filter(getEntityInstance().setParentId(item.getId()));
            item.setChildren(getAllChildren(children));
        });
        return list;
    }
}
