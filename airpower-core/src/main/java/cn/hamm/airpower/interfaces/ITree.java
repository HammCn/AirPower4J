package cn.hamm.airpower.interfaces;

import cn.hamm.airpower.root.RootEntity;

import java.util.List;

/**
 * <h1>标准树接口</h1>
 *
 * @author Hamm
 */
public interface ITree<E extends RootEntity<E>> extends IEntity<E> {
    /**
     * 获取树的名称
     *
     * @return 树名称
     */
    @SuppressWarnings("unused")
    String getName();

    /**
     * 设置树名称
     *
     * @param name 树名称
     * @return 树实体
     */
    @SuppressWarnings("unused")
    E setName(String name);

    /**
     * 设置父级ID
     *
     * @param parentId 设置父级ID
     * @return 树实体
     */
    @SuppressWarnings("unused")
    E setParentId(Long parentId);

    /**
     * 获取树的父级ID
     *
     * @return 父级ID
     */
    Long getParentId();

    /**
     * 设置树的子集列表
     *
     * @param children 子集
     * @return 树实体
     */
    @SuppressWarnings("UnusedReturnValue")
    E setChildren(List<E> children);

    /**
     * 获取树的子集列表
     *
     * @return 树的子集
     */
    @SuppressWarnings("unused")
    List<E> getChildren();
}
