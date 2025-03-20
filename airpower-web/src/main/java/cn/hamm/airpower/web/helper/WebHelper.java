package cn.hamm.airpower.web.helper;

import cn.hamm.airpower.core.helper.EmailHelper;
import cn.hamm.airpower.redis.RedisHelper;
import cn.hamm.airpower.web.config.CookieConfig;
import cn.hamm.airpower.web.config.WebConfig;
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
public class WebHelper {
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
     * <h3>全局 {@code Cookie} 配置</h3>
     */
    @Getter
    private static CookieConfig cookieConfig;

    /**
     * <h3>全局配置</h3>
     */
    @Getter
    private static WebConfig webConfig;

    @Autowired
    WebHelper(
            RedisHelper redisHelper,
            EmailHelper emailHelper,
            TransactionHelper transactionHelper,
            CookieHelper cookieHelper,
            Environment environment,
            EntityManager entityManager,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            CookieConfig cookieConfig,
            WebConfig webConfig
    ) {
        WebHelper.redisHelper = redisHelper;
        WebHelper.emailHelper = emailHelper;
        WebHelper.transactionHelper = transactionHelper;
        WebHelper.cookieHelper = cookieHelper;
        WebHelper.environment = environment;
        WebHelper.entityManager = entityManager;
        WebHelper.request = httpServletRequest;
        WebHelper.response = httpServletResponse;
        WebHelper.cookieConfig = cookieConfig;
        WebHelper.webConfig = webConfig;
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