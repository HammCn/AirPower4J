package cn.hamm.airpower.util;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * <h1>🇨🇳AirPower工具包 </h1>
 * <hr/>
 * <h3>🔥按 <code>A</code>、<code>I</code>、<code>R</code> 打开新大陆🔥</h3>
 * <hr/>
 *
 * @author 🌏Hamm.cn
 */
@Component
public class AirUtil {
    /**
     * <h2>Redis工具类</h2>
     */
    @Getter
    private static RedisUtil redisUtil;

    /**
     * <h2>邮件相关服务</h2>
     */
    @Getter
    private static EmailUtil emailUtil;

    /**
     * <h2>事务工具类</h2>
     */
    @Getter
    private static TransactionUtil transactionUtil;

    /**
     * <h2>树工具类</h2>
     */
    @Getter
    private static TreeUtil treeUtil;

    /**
     * <h2>安全相关服务</h2>
     */
    @Getter
    private static SecurityUtil securityUtil;

    /**
     * <h2>RSA工具类</h2>
     */
    @Getter
    private static RsaUtil rsaUtil;

    /**
     * <h2>Cookie工具类</h2>
     */
    @Getter
    private static CookieUtil cookieUtil;

    /**
     * <h2>MQTT工具类</h2>
     */
    @Getter
    private static MqttUtil mqttUtil;

    /**
     * <h2>密码工具类</h2>
     */
    @Getter
    private static PasswordUtil passwordUtil;

    /**
     * <h2>权限处理工具类</h2>
     */
    @Getter
    private static AccessUtil accessUtil;

    /**
     * <h2>集合工具类</h2>
     */
    @Getter
    private static CollectionUtil collectionUtil;

    /**
     * <h2>字典工具类</h2>
     */
    @Getter
    private static DictionaryUtil dictionaryUtil;

    /**
     * <h2>随机工具类</h2>
     */
    @Getter
    private static RandomUtil randomUtil;

    /**
     * <h2>反射工具类</h2>
     */
    @Getter
    private static ReflectUtil reflectUtil;

    /**
     * <h2>请求工具类</h2>
     */
    @Getter
    private static RequestUtil requestUtil;

    /**
     * <h2>验证工具类</h2>
     */
    @Getter
    private static ValidateUtil validateUtil;

    /**
     * <h2>环境变量</h2>
     */
    @Getter
    private static Environment environment;

    /**
     * <h2>JPA实体管理器</h2>
     */
    @Getter
    private static EntityManager entityManager;

    @Autowired
    AirUtil(
            RedisUtil redisUtil,
            EmailUtil emailUtil,
            TransactionUtil transactionUtil,
            TreeUtil treeUtil,
            SecurityUtil securityUtil,
            RsaUtil rsaUtil,
            CookieUtil cookieUtil,
            MqttUtil mqttUtil,
            PasswordUtil passwordUtil,
            AccessUtil accessUtil,
            CollectionUtil collectionUtil,
            DictionaryUtil dictionaryUtil,
            RandomUtil randomUtil,
            ReflectUtil reflectUtil,
            RequestUtil requestUtil,
            ValidateUtil validateUtil,
            Environment environment,
            EntityManager entityManager
    ) {
        AirUtil.redisUtil = redisUtil;
        AirUtil.emailUtil = emailUtil;
        AirUtil.transactionUtil = transactionUtil;
        AirUtil.treeUtil = treeUtil;
        AirUtil.securityUtil = securityUtil;
        AirUtil.rsaUtil = rsaUtil;
        AirUtil.cookieUtil = cookieUtil;
        AirUtil.mqttUtil = mqttUtil;
        AirUtil.passwordUtil = passwordUtil;
        AirUtil.accessUtil = accessUtil;
        AirUtil.collectionUtil = collectionUtil;
        AirUtil.dictionaryUtil = dictionaryUtil;
        AirUtil.randomUtil = randomUtil;
        AirUtil.reflectUtil = reflectUtil;
        AirUtil.requestUtil = requestUtil;
        AirUtil.validateUtil = validateUtil;
        AirUtil.environment = environment;
        AirUtil.entityManager = entityManager;
    }

    /**
     * <h2>获取当前的环境变量</h2>
     *
     * @return 当前环境变量
     */
    public static String getCurrentEnvironment() {
        return getEnvironment().getActiveProfiles()[0];
    }
}
