package cn.hamm.airpower.config;

/**
 * <h1>CookieConfig</h1>
 *
 * @author Hamm
 */
public class CookieConfig {

    /**
     * <h2>Cookie的路径</h2>
     */
    public static String cookiePath = "/";

    /**
     * <h2>身份验证的Cookie名称</h2>
     */
    public static String authCookieName = "authorization-key";

    /**
     * <h2>Cookie的HttpOnly配置</h2>
     */
    public static boolean isCookieHttpOnly = true;

    /**
     * <h2>Cookie有效期</h2>
     */
    public static int cookieMaxAge = 86400;

    /**
     * <h2>使用Https方式的安全Cookie</h2>
     */
    public static boolean isCookieSecurity = true;
}
