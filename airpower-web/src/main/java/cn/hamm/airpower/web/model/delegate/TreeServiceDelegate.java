package cn.hamm.airpower.web.model.delegate;

import cn.hamm.airpower.core.model.tree.ITree;
import cn.hamm.airpower.web.model.RootEntity;
import cn.hamm.airpower.web.model.RootRepository;
import cn.hamm.airpower.web.model.RootService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cn.hamm.airpower.core.exception.ServiceError.FORBIDDEN_DELETE;

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

    /**
     * <h3>获取指定父ID下的所有子ID</h3>
     *
     * @param parentId    父ID
     * @param service     服务类
     * @param entityClass 实体类
     * @param <T>         实体类型
     * @return ID集合
     */
    public static <T extends RootEntity<T> & ITree<T>> @NotNull Set<Long> getChildrenIdList(
            long parentId,
            @NotNull RootService<T, ?> service,
            @NotNull Class<T> entityClass
    ) {
        Set<Long> list = new HashSet<>();
        getChildrenIdList(parentId, service, entityClass, list);
        return list;
    }

    /**
     * <h3>获取指定父ID下的所有子ID</h3>
     *
     * @param parentId    父ID
     * @param service     服务类
     * @param entityClass 实体类型
     * @param list        集合
     * @param <T>         实体类型
     */
    public static <T extends RootEntity<T> & ITree<T>> void getChildrenIdList(
            long parentId,
            @NotNull RootService<T, ?> service,
            @NotNull Class<T> entityClass,
            @NotNull Set<Long> list
    ) {
        T parent = service.get(parentId);
        list.add(parent.getId());
        List<T> children;
        try {
            children = service.filter(entityClass.getConstructor().newInstance().setParentId(parent.getId()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        children.forEach(child -> getChildrenIdList(child.getId(), service, entityClass, list));
    }
}
