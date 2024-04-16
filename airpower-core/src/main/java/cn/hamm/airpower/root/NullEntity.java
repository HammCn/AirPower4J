package cn.hamm.airpower.root;

import cn.hamm.airpower.interfaces.IEntity;

/**
 * <h1>空实体</h1>
 *
 * @author Hamm
 */
public class NullEntity extends RootEntity<NullEntity> {
    /**
     * <h2>创建一个空实体</h2>
     *
     * @param <T> 实体类型
     * @return 空实体
     */
    @SuppressWarnings("unchecked")
    public static <T extends IEntity<T>> T createNull(Class<T> clazz) {
        return (T) new NullEntity();
    }
}
