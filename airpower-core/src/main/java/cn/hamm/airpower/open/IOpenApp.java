package cn.hamm.airpower.open;

/**
 * <h1>开放应用实体接口</h1>
 *
 * @author Hamm.cn
 * @apiNote 请确保你的应用实体类实现了此接口
 */
public interface IOpenApp {
    /**
     * <h2>获取应用的AppKey</h2>
     */
    String getAppKey();

    /**
     * <h2>获取应用的AppSecret</h2>
     */
    String getAppSecret();

    /**
     * <h2>获取应用的加密算法</h2>
     */
    Integer getArithmetic();

    /**
     * <h2>获取应用的私钥</h2>
     */
    String getPrivateKey();

    /**
     * <h2>获取应用的公钥</h2>
     */
    String getPublicKey();
}
