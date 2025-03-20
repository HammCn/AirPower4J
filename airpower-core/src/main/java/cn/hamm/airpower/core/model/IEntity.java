package cn.hamm.airpower.core.model;

/**
 * <h1>标准实体接口</h1>
 *
 * @author Hamm.cn
 */
public interface IEntity<E extends IEntity<E>> {
    /**
     * <h3>获取主键 {@code ID}</h3>
     *
     * @return 主键 {@code ID}
     */
    Long getId();

    /**
     * <h3>设置主键 {@code ID}</h3>
     *
     * @param id 主键 {@code ID}
     * @return 实体
     */
    E setId(Long id);
}
