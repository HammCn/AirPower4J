package cn.hamm.airpower.util;

import cn.hamm.airpower.config.AirConfig;
import cn.hamm.airpower.config.CookieConfig;
import jakarta.servlet.http.Cookie;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * <h1>Cookie助手</h1>
 *
 * @author Hamm.cn
 */
@Component
public class CookieUtil {

    /**
     * <h2>获取一个Cookie</h2>
     *
     * @param key   Key
     * @param value Value
     * @return Cookie
     * @see CookieConfig
     */
    public final @NotNull Cookie getCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(AirConfig.getCookieConfig().isCookieHttpOnly());
        cookie.setMaxAge(AirConfig.getCookieConfig().getCookieMaxAge());
        cookie.setSecure(AirConfig.getCookieConfig().isCookieSecurity());
        cookie.setPath(cookie.getPath());
        return cookie;
    }

    /**
     * <h2>获取一个身份验证的Cookie</h2>
     *
     * @param value 身份串的值
     * @return Cookie
     * @see CookieConfig
     */
    public final @NotNull Cookie getAuthorizeCookie(String value) {
        return getCookie(AirConfig.getCookieConfig().getAuthCookieName(), value);
    }
}
