package cn.hamm.airpower.config;

/**
 * <h1>全局默认配置文件</h1>
 *
 * @author Hamm
 */
public class GlobalConfig {
    /**
     * <h2>服务全局拦截</h2>
     */
    public static boolean isServiceRunning = true;

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
     * <h2>身份令牌有效期</h2>
     */
    public static int authorizeExpTime = 86400;

    /**
     * <h2>多租户的header的key</h2>
     */
    public static String tenantHeader = "tenant-code";

    /**
     * <h2>应用版本号header的key</h2>
     */
    public static String appVersionHeader = "app-version";

    /**
     * <h2>密码最大长度</h2>
     */
    public static int passwordMaxLength = 16;

    /**
     * <h2>密码最小长度</h2>
     */
    public static int passwordMinLength = 6;
}
