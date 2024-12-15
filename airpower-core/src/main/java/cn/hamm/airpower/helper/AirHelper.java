package cn.hamm.airpower.helper;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * <h1>{@code AirPower} 助手类 </h1>
 *
 * @author Hamm.cn
 */
@Component
public class AirHelper {
    /**
     * <h3>{@code Redis} 助手类</h3>
     */
    @Getter
    private static RedisHelper redisHelper;

    /**
     * <h3>邮件相关服务</h3>
     */
    @Getter
    private static EmailHelper emailHelper;

    /**
     * <h3>事务助手类</h3>
     */
    @Getter
    private static TransactionHelper transactionHelper;

    /**
     * <h3>{@code Cookie} 助手类</h3>
     */
    @Getter
    private static CookieHelper cookieHelper;

    /**
     * <h3>{@code MQTT} 助手类</h3>
     */
    @Getter
    private static MqttHelper mqttHelper;

    /**
     * <h3>环境变量</h3>
     */
    @Getter
    private static Environment environment;

    /**
     * <h3>{@code JPA} 实体管理器</h3>
     */
    @Getter
    private static EntityManager entityManager;

    /**
     * <h3>当前的请求对象</h3>
     */
    @Getter
    private static HttpServletRequest request;

    /**
     * <h3>当前的响应对象</h3>
     */
    @Getter
    private static HttpServletResponse response;

    /**
     * <h3>{@code WebSocket} 助手类</h3>
     */
    @Getter
    private static WebsocketHelper websocketHelper;

    @Autowired
    AirHelper(
            RedisHelper redisHelper,
            EmailHelper emailHelper,
            TransactionHelper transactionHelper,
            CookieHelper cookieHelper,
            MqttHelper mqttHelper,
            Environment environment,
            EntityManager entityManager,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            WebsocketHelper websocketHelper
    ) {
        AirHelper.redisHelper = redisHelper;
        AirHelper.emailHelper = emailHelper;
        AirHelper.transactionHelper = transactionHelper;
        AirHelper.cookieHelper = cookieHelper;
        AirHelper.mqttHelper = mqttHelper;
        AirHelper.environment = environment;
        AirHelper.entityManager = entityManager;
        AirHelper.request = httpServletRequest;
        AirHelper.response = httpServletResponse;
        AirHelper.websocketHelper = websocketHelper;
    }

    /**
     * <h3>获取当前的环境变量</h3>
     *
     * @return 当前环境变量
     */
    public static String getCurrentEnvironment() {
        return getEnvironment().getActiveProfiles()[0];
    }
}
