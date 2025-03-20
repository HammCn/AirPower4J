package cn.hamm.airpower.core.file;

import cn.hamm.airpower.core.model.IEntity;

/**
 * <h1>标准文件接口</h1>
 *
 * @author Hamm.cn
 */
public interface IFile<E extends IFile<E>> extends IEntity<E> {
    /**
     * <h3>获取文件的 {@code URL}</h3>
     *
     * @return {@code URL}
     */
    String getUrl();

    /**
     * <h3>设置文件的 {@code URL}</h3>
     *
     * @param url {@code URL}
     * @return 实体
     */
    E setUrl(String url);
}
