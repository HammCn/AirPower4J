package cn.hamm.airpower.security;

import cn.hamm.airpower.config.CookieConfig;
import jakarta.servlet.http.Cookie;


/**
 * <h1>Cookie助手</h1>
 *
 * @author Hamm
 */
public class CookieUtil {
    /**
     * 获取一个Cookie
     *
     * @param key   Key
     * @param value Value
     * @return Cookie
     * @see CookieConfig
     */
    public static Cookie getCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(CookieConfig.isCookieHttpOnly);
        cookie.setMaxAge(CookieConfig.cookieMaxAge);
        cookie.setSecure(CookieConfig.isCookieSecurity);
        cookie.setPath(CookieConfig.cookiePath);
        return cookie;
    }

    /**
     * 获取一个身份验证的Cookie
     *
     * @param value 身份串的值
     * @return Cookie
     * @see CookieConfig
     */
    public static Cookie getAuthorizeCookie(String value) {
        return getCookie(CookieConfig.authCookieName, value);
    }
}
