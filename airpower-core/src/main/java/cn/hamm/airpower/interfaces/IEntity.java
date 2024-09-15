package cn.hamm.airpower.interfaces;

/**
 * <h1>标准实体接口</h1>
 *
 * @author Hamm.cn
 */
public interface IEntity<E> {
    /**
     * <h2>获取主键 {@code ID}</h2>
     *
     * @return 主键 {@code ID}
     */
    Long getId();

    /**
     * <h2>设置主键 {@code ID}</h2>
     *
     * @param id 主键 {@code ID}
     * @return 实例
     */
    E setId(Long id);
}
