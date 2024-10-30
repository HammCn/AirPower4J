package cn.hamm.airpower.interfaces;

import cn.hamm.airpower.exception.ServiceError;
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

    /**
     * <h2>根据父级ID获取所有子节点</h2>
     *
     * @param parentId 父级ID
     * @return 子节点列表
     */
    default List<E> getByParentId(Long parentId) {
        return filter(getEntityInstance().setParentId(parentId));
    }

    /**
     * <h2>删除前确认是否包含子节点数据</h2>
     *
     * @param id 待删除的ID
     */
    default void ensureNoChildrenBeforeDelete(long id) {
        List<E> children = filter(getEntityInstance().setParentId(id));
        ServiceError.FORBIDDEN_DELETE.when(!children.isEmpty(), "无法删除含有下级的数据，请先删除所有下级！");
    }
}
