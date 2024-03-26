package cn.hamm.airpower.interfaces;

/**
 * <h1>标准实体接口</h1>
 *
 * @author Hamm
 */
public interface IEntity<E extends IEntity<E>> {
    /**
     * <h2>获取主键ID</h2>
     *
     * @return 主键ID
     */
    Long getId();

    /**
     * <h2>设置实体主键ID</h2>
     *
     * @param id 主键ID
     * @return 实体
     */
    E setId(Long id);
}
