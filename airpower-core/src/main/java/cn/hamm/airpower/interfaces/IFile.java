package cn.hamm.airpower.interfaces;

/**
 * <h1>标准文件接口</h1>
 *
 * @author Hamm
 */
public interface IFile<E extends IFile<E>> extends IEntity<E> {
    /**
     * <h2>获取文件的URL</h2>
     *
     * @return URL
     */
    String getUrl();

    /**
     * <h2>设置文件的URL</h2>
     *
     * @param url URL
     * @return 实体
     */
    E setUrl(String url);
}
