package cn.hamm.airpower.config;

import cn.hamm.airpower.helper.CookieHelper;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <h1>{@code Cookie} 相关配置</h1>
 *
 * @author Hamm.cn
 * @see CookieHelper
 */
@Component
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties("airpower.cookie")
public class CookieConfig {
    /**
     * <h2>{@code Cookie} 的路径</h2>
     */
    private String cookiePath = Constant.SLASH;

    /**
     * <h2>身份验证的 {@code Cookie} 名称</h2>
     */
    private String authCookieName = "authorization-key";

    /**
     * <h2>{@code Cookie} 的 {@code HttpOnly} 配置</h2>
     */
    private boolean cookieHttpOnly = true;

    /**
     * <h2>{@code Cookie} 有效期</h2>
     */
    private int cookieMaxAge = Constant.SECOND_PER_DAY;

    /**
     * <h2>使用 {@code Https} 方式的安全 {@code Cookie}</h2>
     */
    private boolean cookieSecurity = true;
}
