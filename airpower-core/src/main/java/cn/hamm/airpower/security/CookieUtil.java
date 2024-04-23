package cn.hamm.airpower.security;

import cn.hamm.airpower.config.CookieConfig;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>Cookie助手</h1>
 *
 * @author Hamm.cn
 */
@Component
public class CookieUtil {
    @Autowired
    private CookieConfig cookieConfig;

    /**
     * <h2>获取一个Cookie</h2>
     *
     * @param key   Key
     * @param value Value
     * @return Cookie
     * @see CookieConfig
     */
    public final Cookie getCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(cookieConfig.isCookieHttpOnly());
        cookie.setMaxAge(cookieConfig.getCookieMaxAge());
        cookie.setSecure(cookieConfig.isCookieSecurity());
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
    public final Cookie getAuthorizeCookie(String value) {
        return getCookie(cookieConfig.getAuthCookieName(), value);
    }
}
