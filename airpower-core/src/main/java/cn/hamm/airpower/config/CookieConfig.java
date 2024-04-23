package cn.hamm.airpower.config;

import cn.hamm.airpower.security.CookieUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <h1>Cookie相关配置</h1>
 *
 * @author Hamm.cn
 * @see CookieUtil
 */
@Component
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties("airpower.cookie")
public class CookieConfig {
    /**
     * <h2>Cookie的路径</h2>
     */
    private String cookiePath = "/";

    /**
     * <h2>身份验证的Cookie名称</h2>
     */
    private String authCookieName = "authorization-key";

    /**
     * <h2>Cookie的HttpOnly配置</h2>
     */
    private boolean cookieHttpOnly = true;

    /**
     * <h2>Cookie有效期</h2>
     */
    private int cookieMaxAge = Constant.SECOND_PER_DAY;

    /**
     * <h2>使用Https方式的安全Cookie</h2>
     */
    private boolean cookieSecurity = true;
}
