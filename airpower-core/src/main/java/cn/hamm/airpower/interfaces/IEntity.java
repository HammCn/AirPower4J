package cn.hamm.airpower.interfaces;

import cn.hamm.airpower.root.RootModel;

/**
 * <h1>标准实体接口</h1>
 *
 * @author Hamm
 */
public interface IEntity<E extends RootModel<E>> {
    /**
     * 获取主键ID
     *
     * @return 主键ID
     */
    Long getId();

    /**
     * 设置实体主键ID
     *
     * @param id 主键ID
     * @return 实体
     */
    @SuppressWarnings("unused")
    E setId(Long id);
}
