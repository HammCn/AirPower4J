package cn.hamm.airpower.interfaces;

import cn.hamm.airpower.root.RootModel;

/**
 * <h1>标准实体接口</h1>
 *
 * @author Hamm
 */
public interface IEntity<E extends RootModel<E>> {
    /**
     * <h1>获取主键ID</h1>
     *
     * @return 主键ID
     */
    Long getId();

    /**
     * <h1>设置实体主键ID</h1>
     *
     * @param id 主键ID
     * @return 实体
     */
    E setId(Long id);
}
