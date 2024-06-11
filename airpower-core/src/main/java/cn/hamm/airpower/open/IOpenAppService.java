package cn.hamm.airpower.open;

/**
 * <h1>开放应用的Service接口</h1>
 *
 * @author Hamm.cn
 * @apiNote 请确保你的开放应用的Service实现了此接口
 */
public interface IOpenAppService {
    /**
     * <h2>通过应用的AppKey查一个应用</h2>
     *
     * @param appKey AppKey
     * @return 应用
     */
    IOpenApp getByAppKey(String appKey);
}
