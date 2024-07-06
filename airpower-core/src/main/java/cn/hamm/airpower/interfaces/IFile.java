package cn.hamm.airpower.interfaces;

/**
 * <h1>标准文件接口</h1>
 *
 * @author Hamm.cn
 */
public interface IFile<E extends IFile<E>> extends IEntity<E> {
    /**
     * <h2>获取文件的 <code>URL</code></h2>
     *
     * @return <code>URL</code>
     */
    String getUrl();

    /**
     * <h2>设置文件的 <code>URL</code></h2>
     *
     * @param url <code>URL</code>
     * @return 实体
     */
    E setUrl(String url);
}
