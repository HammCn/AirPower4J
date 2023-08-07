package cn.hamm.airpower.interfaces;

import cn.hamm.airpower.root.RootEntity;

import java.util.List;

/**
 * <h1>标准树接口</h1>
 *
 * @author hamm
 */
public interface ITree<E extends RootEntity<E>> extends IEntity<E> {
    /**
     * <h1>获取树的名称</h1>
     *
     * @return 树名称
     */
    String getName();

    /**
     * <h1>设置树名称</h1>
     *
     * @param name 树名称
     * @return 树实体
     */
    E setName(String name);

    /**
     * <h1>设置父级ID</h1>
     *
     * @param parentId 设置父级ID
     * @return 树实体
     */
    E setParentId(Long parentId);

    /**
     * <h1>获取树的父级ID</h1>
     *
     * @return 父级ID
     */
    Long getParentId();

    /**
     * <h1>设置树的子集列表</h1>
     *
     * @param children 子集
     * @return 树实体
     */
    E setChildren(List<E> children);

    /**
     * <h1>获取树的子集列表</h1>
     *
     * @return 树的子集
     */
    List<E> getChildren();
}
