package cn.hamm.airpower.config;

/**
 * <h1>全局默认配置文件</h1>
 *
 * @author Hamm
 */
public class GlobalConfig {
    /**
     * <h2>数据库前缀</h2>
     */
    public static String databasePrefix = "tenant_";

    /**
     * <h2>是否开启缓存</h2>
     */
    public static boolean isCacheEnabled = true;

    /**
     * <h2>缓存过期时间</h2>
     */
    public static int cacheExpTime = 3600;

    /**
     * <h2>默认分页条数</h2>
     */
    public static int defaultPageSize = 20;

    /**
     * <h2>默认排序字段</h2>
     */
    public static String defaultSortField = "id";

    /**
     * <h2>默认排序方向</h2>
     */
    public static String defaultSortDirection = "desc";

    /**
     * <h2>身份令牌header的key</h2>
     */
    public static String authorizeHeader = "authorization";

    /**
     * <h2>多租户的header的key</h2>
     */
    public static String tenantHeader = "tenant-code";

    /**
     * <h2>密码最大长度</h2>
     */
    public static int passwordMaxLength = 16;

    /**
     * <h2>密码最小长度</h2>
     */
    public static int passwordMinLength = 6;

    /**
     * <h2>统一登录的路径</h2>
     * <p>
     * 如果需要Oauth2登录,则请将前后端部署到一个站点下, 且按下面的流程反向代理
     * <li>反向代理 oauth2 路径到后端项目</li>
     * <li>反向代理 api   路径到后端项目,且去除 api</li>
     */
    public static String loginPath = "http://localhost:3000/login";
}
