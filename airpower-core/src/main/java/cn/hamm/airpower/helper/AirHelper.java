package cn.hamm.airpower.helper;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * <h1>{@code AirPower} 工具包 </h1>
 *
 * @author Hamm.cn
 */
@Component
public class AirHelper {
    /**
     * <h2>{@code Redis} 工具类</h2>
     */
    @Getter
    private static RedisHelper redisHelper;

    /**
     * <h2>邮件相关服务</h2>
     */
    @Getter
    private static EmailHelper emailHelper;

    /**
     * <h2>事务工具类</h2>
     */
    @Getter
    private static TransactionHelper transactionHelper;

    /**
     * <h2>{@code Cookie} 工具类</h2>
     */
    @Getter
    private static CookieHelper cookieHelper;

    /**
     * <h2>{@code MQTT} 工具类</h2>
     */
    @Getter
    private static MqttHelper mqttHelper;

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
     * <h2>获取当前的环境变量</h2>
     *
     * @return 当前环境变量
     */
    public static String getCurrentEnvironment() {
        return getEnvironment().getActiveProfiles()[0];
    }
}
