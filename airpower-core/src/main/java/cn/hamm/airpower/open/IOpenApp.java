package cn.hamm.airpower.open;

/**
 * <h1>开放应用实体接口</h1>
 *
 * @author Hamm.cn
 * @apiNote 请确保你的应用实体类实现了此接口
 */
public interface IOpenApp {
    /**
     * <h2>获取应用的 {@code AppKey}</h2>
     *
     * @return {@code AppKey}
     */
    String getAppKey();

    /**
     * <h2>获取应用的 {@code AppSecret}</h2>
     *
     * @return {@code AppSecret}
     */
    String getAppSecret();

    /**
     * <h2>获取应用的加密算法</h2>
     *
     * @return 算法
     */
    Integer getArithmetic();

    /**
     * <h2>获取应用的私钥</h2>
     *
     * @return 私钥
     */
    String getPrivateKey();

    /**
     * <h2>获取应用的公钥</h2>
     *
     * @return 公钥
     */
    String getPublicKey();

    /**
     * <h2>获取IP白名单列表</h2>
     *
     * @return IP白名单
     */
    String getIpWhiteList();

    /**
     * <h2>是否禁用</h2>
     *
     * @return 是否禁用
     */
    Boolean getIsDisabled();
}
