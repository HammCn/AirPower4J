package cn.hamm.airpower.config;

import cn.hamm.airpower.security.CookieUtil;

/**
 * <h1>Cookie相关配置</h1>
 *
 * @author Hamm
 * @see CookieUtil
 */
@SuppressWarnings("CanBeFinal")
public class CookieConfig {
    /**
     * Cookie的路径
     */
    public static String cookiePath = "/";

    /**
     * 身份验证的Cookie名称
     */
    public static String authCookieName = "authorization-key";

    /**
     * Cookie的HttpOnly配置
     */
    public static boolean isCookieHttpOnly = true;

    /**
     * Cookie有效期
     */
    public static int cookieMaxAge = 86400;

    /**
     * 使用Https方式的安全Cookie
     */
    public static boolean isCookieSecurity = true;
}
