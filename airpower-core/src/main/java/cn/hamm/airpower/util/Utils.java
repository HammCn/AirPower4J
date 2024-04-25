package cn.hamm.airpower.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>Utils包</h1>
 *
 * @author Hamm
 */
@Component
public class Utils {
    /**
     * <h2>Redis工具类</h2>
     */
    @Getter
    private static RedisUtil<?> redisUtil;

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

    @Autowired
    Utils(
            RedisUtil<?> redisUtil,
            EmailUtil emailUtil,
            TransactionUtil transactionUtil,
            TreeUtil treeUtil,
            SecurityUtil securityUtil,
            RsaUtil rsaUtil,
            CookieUtil cookieUtil,
            MqttUtil mqttUtil
    ) {
        Utils.redisUtil = redisUtil;
        Utils.emailUtil = emailUtil;
        Utils.transactionUtil = transactionUtil;
        Utils.treeUtil = treeUtil;
        Utils.securityUtil = securityUtil;
        Utils.rsaUtil = rsaUtil;
        Utils.cookieUtil = cookieUtil;
        Utils.mqttUtil = mqttUtil;
    }
}
