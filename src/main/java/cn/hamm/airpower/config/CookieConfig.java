package cn.hamm.airpower.config;

/**
 * @author Hamm https://hamm.cn
 */
public class CookieConfig {

    /**
     * <h1>Cookie的路径</h1>
     */
    public static String cookiePath = "/";

    /**
     * <h1>身份验证的Cookie名称</h1>
     */
    public static String authCookieName = "authorization-key";

    /**
     * <h1>Cookie的HttpOnly配置</h1>
     */
    public static boolean isCookieHttpOnly = true;

    /**
     * <h1>Cookie有效期</h1>
     */
    public static int cookieMaxAge = 86400;

    /**
     * <h1>使用Https方式的安全Cookie</h1>
     */
    public static boolean isCookieSecurity = true;
}
