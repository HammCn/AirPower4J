package cn.hamm.airpower.util;

import cn.hamm.airpower.websocket.WebsocketUtil;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * <h1>{@code AirPower} 工具包 </h1>
 *
 * @author Hamm.cn
 */
@Component
public class Utils {
    /**
     * <h2>{@code Redis} 工具类</h2>
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
     * <h2>{@code RSA} 工具类</h2>
     */
    @Getter
    private static RsaUtil rsaUtil;

    /**
     * <h2>{@code Cookie} 工具类</h2>
     */
    @Getter
    private static CookieUtil cookieUtil;

    /**
     * <h2>{@code MQTT} 工具类</h2>
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
     * <h2>{@code JPA} 实体管理器</h2>
     */
    @Getter
    private static EntityManager entityManager;

    /**
     * <h2>当前的请求对象</h2>
     */
    @Getter
    private static HttpServletRequest request;

    /**
     * <h2>当前的响应对象</h2>
     */
    @Getter
    private static HttpServletResponse response;

    /**
     * <h2>{@code WebSocket} 工具类</h2>
     */
    @Getter
    private static WebsocketUtil websocketUtil;

    /**
     * <h2>数字工具</h2>
     */
    @Getter
    private static NumberUtil numberUtil;

    /**
     * <h2>字符串工具</h2>
     */
    @Getter
    private static StringUtil stringUtil;

    /**
     * <h2>{@code AES} 工具</h2>
     */
    @Getter
    private static AesUtil aesUtil;

    /**
     * <h2>任务工具</h2>
     */
    @Getter
    private static TaskUtil taskUtil;

    /**
     * <h2>日期时间工具</h2>
     */
    @Getter
    private static DateTimeUtil dateTimeUtil;

    @Autowired
    Utils(
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
            EntityManager entityManager,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            NumberUtil numberUtil,
            StringUtil stringUtil,
            WebsocketUtil websocketUtil,
            AesUtil aesUtil,
            TaskUtil taskUtil,
            DateTimeUtil dateTimeUtil
    ) {
        Utils.redisUtil = redisUtil;
        Utils.emailUtil = emailUtil;
        Utils.transactionUtil = transactionUtil;
        Utils.treeUtil = treeUtil;
        Utils.securityUtil = securityUtil;
        Utils.rsaUtil = rsaUtil;
        Utils.cookieUtil = cookieUtil;
        Utils.mqttUtil = mqttUtil;
        Utils.passwordUtil = passwordUtil;
        Utils.accessUtil = accessUtil;
        Utils.collectionUtil = collectionUtil;
        Utils.dictionaryUtil = dictionaryUtil;
        Utils.randomUtil = randomUtil;
        Utils.reflectUtil = reflectUtil;
        Utils.requestUtil = requestUtil;
        Utils.validateUtil = validateUtil;
        Utils.environment = environment;
        Utils.entityManager = entityManager;
        Utils.request = httpServletRequest;
        Utils.response = httpServletResponse;
        Utils.numberUtil = numberUtil;
        Utils.stringUtil = stringUtil;
        Utils.websocketUtil = websocketUtil;
        Utils.aesUtil = aesUtil;
        Utils.taskUtil = taskUtil;
        Utils.dateTimeUtil = dateTimeUtil;
    }

    /**
     * <h2>获取HttpUtil</h2>
     *
     * @return HttpUtil
     */
    @Contract(" -> new")
    public static @NotNull HttpUtil getHttpUtil() {
        return new HttpUtil();
    }

    /**
     * <h2>获取TokenUtil</h2>
     *
     * @return TokenUtil
     */
    @Contract(" -> new")
    public static @NotNull TokenUtil getTokenUtil() {
        return new TokenUtil();
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
