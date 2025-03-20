package cn.hamm.airpower.web.open;

/**
 * <h1>开放应用的 {@code Service} 接口</h1>
 *
 * @author Hamm.cn
 * @apiNote 请确保你的开放应用的 {@code Service} 实现了此接口
 */
public interface IOpenAppService {
    /**
     * <h3>通过应用的 {@code AppKey} 查一个应用</h3>
     *
     * @param appKey {@code AppKey}
     * @return 应用
     */
    IOpenApp getByAppKey(String appKey);
}
