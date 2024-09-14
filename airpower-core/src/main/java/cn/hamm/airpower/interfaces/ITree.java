package cn.hamm.airpower.interfaces;

import java.util.List;

/**
 * <h1>标准树接口</h1>
 *
 * @author Hamm.cn
 */
public interface ITree<E extends ITree<E>> extends IEntity {
    /**
     * <h2>获取树的名称</h2>
     *
     * @return 树名称
     */
    String getName();

    /**
     * <h2>设置树名称</h2>
     *
     * @param name 树名称
     * @return 树实体
     */
    E setName(String name);

    /**
     * <h2>获取树的父级 {@code ID}</h2>
     *
     * @return 父级 {@code ID}
     */
    Long getParentId();

    /**
     * <h2>设置父级 {@code ID}</h2>
     *
     * @param parentId 设置父级 {@code ID}
     * @return 树实体
     */
    E setParentId(Long parentId);

    /**
     * <h2>获取树的子集列表</h2>
     *
     * @return 树的子集
     */
    List<E> getChildren();

    /**
     * <h2>设置树的子集列表</h2>
     *
     * @param children 子集
     * @return 树实体
     */
    @SuppressWarnings("UnusedReturnValue")
    E setChildren(List<E> children);
}
