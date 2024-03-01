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
 * @author Hamm
 * @see CookieUtil
 */
@Component
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties("airpower.cookie")
public class CookieConfig {
    /**
     * Cookie的路径
     */
    private String cookiePath = "/";

    /**
     * 身份验证的Cookie名称
     */
    private String authCookieName = "authorization-key";

    /**
     * Cookie的HttpOnly配置
     */
    private boolean cookieHttpOnly = true;

    /**
     * Cookie有效期
     */
    private int cookieMaxAge = Constant.SECOND_PER_DAY;

    /**
     * 使用Https方式的安全Cookie
     */
    private boolean cookieSecurity = true;
}
