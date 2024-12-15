package cn.hamm.airpower.open;

/**
 * <h1>开放应用实体接口</h1>
 *
 * @author Hamm.cn
 * @apiNote 请确保你的应用实体类实现了此接口
 */
public interface IOpenApp {
    /**
     * <h3>获取应用的 {@code AppKey}</h3>
     *
     * @return {@code AppKey}
     */
    String getAppKey();

    /**
     * <h3>获取应用的 {@code AppSecret}</h3>
     *
     * @return {@code AppSecret}
     */
    String getAppSecret();

    /**
     * <h3>获取应用的加密算法</h3>
     *
     * @return 算法
     */
    Integer getArithmetic();

    /**
     * <h3>获取应用的私钥</h3>
     *
     * @return 私钥
     */
    String getPrivateKey();

    /**
     * <h3>获取应用的公钥</h3>
     *
     * @return 公钥
     */
    String getPublicKey();

    /**
     * <h3>获取IP白名单列表</h3>
     *
     * @return IP白名单
     */
    String getIpWhiteList();

    /**
     * <h3>是否禁用</h3>
     *
     * @return 是否禁用
     */
    Boolean getIsDisabled();
}
