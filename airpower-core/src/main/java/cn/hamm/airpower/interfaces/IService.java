package cn.hamm.airpower.interfaces;

import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.model.Sort;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * <h1>Service接口</h1>
 *
 * @author Hamm.cn
 */
public interface IService<E extends IEntity<E>> {
    /**
     * <h2>根据过滤条件获取数据</h2>
     *
     * @param filter 过滤条件
     * @return 过滤后的列表数据
     */
    default List<E> filter(E filter) {
        return filter(filter, null);
    }

    /**
     * <h2>过滤数据</h2>
     *
     * @param filter 全匹配过滤器
     * @param sort   排序
     * @return List数据
     */
    List<E> filter(E filter, Sort sort);

    /**
     * <h2>获取实体类</h2>
     *
     * @return 实体类
     */
    default Class<E> getEntityClass() {
        //noinspection unchecked
        return (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * <h2>获取实体类实例</h2>
     *
     * @return 实体类实例
     */
    default E getEntityInstance() {
        try {
            return getEntityClass().getConstructor().newInstance();
        } catch (Exception exception) {
            throw new ServiceException(exception.getMessage());
        }
    }
}
