package cn.hamm.airpower.root.delegate;

import cn.hamm.airpower.interfaces.ITree;
import cn.hamm.airpower.root.RootEntity;
import cn.hamm.airpower.root.RootRepository;
import cn.hamm.airpower.root.RootService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static cn.hamm.airpower.exception.ServiceError.FORBIDDEN_DELETE;

/**
 * <h1>树结构的服务委托类</h1>
 *
 * @author Hamm.cn
 */
public class TreeServiceDelegate {
    /**
     * <h3>禁止外部实例化</h3>
     */
    @Contract(pure = true)
    private TreeServiceDelegate() {
    }

    /**
     * <h3>获取所有子节点</h3>
     *
     * @param service 服务
     * @param list    树结构列表
     * @return 包含所有直接点的树结构列表
     */
    @Contract("_, _ -> param2")
    public static <
            E extends RootEntity<E> & ITree<E>,
            S extends RootService<E, R>,
            R extends RootRepository<E>
            > @NotNull List<E> getAllChildren(@NotNull S service, @NotNull List<E> list) {
        list.forEach(item -> item.setChildren(getAllChildren(service, findByParentId(service, item.getId()))));
        return list;
    }

    /**
     * <h3>根据父级ID获取所有子节点</h3>
     *
     * @param service  服务
     * @param parentId 父级ID
     * @return 子节点列表
     */
    public static <
            E extends RootEntity<E> & ITree<E>,
            S extends RootService<E, R>,
            R extends RootRepository<E>
            > @NotNull List<E> findByParentId(@NotNull S service, Long parentId) {
        return service.filter(service.getEntityInstance().setParentId(parentId));
    }

    /**
     * <h3>删除前确认是否包含子节点数据</h3>
     *
     * @param service 服务
     * @param id      待删除的ID
     */
    public static <
            E extends RootEntity<E> & ITree<E>,
            S extends RootService<E, R>,
            R extends RootRepository<E>
            > void ensureNoChildrenBeforeDelete(S service, long id) {
        FORBIDDEN_DELETE.when(!findByParentId(service, id).isEmpty(), "无法删除含有下级的数据，请先删除所有下级！");
    }
}
