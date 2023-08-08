package cn.hamm.airpower.config;

/**
 * <h1>全局默认配置文件</h1>
 *
 * @author Hamm
 */
public class GlobalConfig {
    /**
     * <h1>主数据源</h1>
     * <p>
     * 启动多数据源时, 此项将自动作为主数据源
     * </p>
     */
    public static String primaryDataBase = "service";

    /**
     * <h1>数据库默认地址</h1>
     */
    public static String defaultDatabaseHost = "127.0.0.1";

    /**
     * <h1>数据库默认端口</h1>
     */
    public static int defaultDatabasePort = 3306;

    /**
     * <h1>部署API主域名</h1>
     *
     * <p>
     * 如配置了非空字符串, 如 <code>hamm.cn</code>, 则自动启动多数据源：
     * </p>
     * <p>
     * 访问 <code>test.hamm.cn</code> 时, 则自动切到数据源 <code>test</code> 上
     * </p>
     */
    public static String apiRootDomain = "";

    /**
     * <h1>数据库前缀</h1>
     */
    public static String databasePrefix = "tenant_";

    /**
     * <h1>是否开启缓存</h1>
     */
    public static boolean isCacheEnabled = true;

    /**
     * <h1>缓存过期时间</h1>
     */
    public static int cacheExpTime = 3600;

    /**
     * <h1>默认分页条数</h1>
     */
    public static int defaultPageSize = 20;

    /**
     * <h1>默认排序字段</h1>
     */
    public static String defaultSortField = "id";

    /**
     * <h1>默认排序方向</h1>
     */
    public static String defaultSortDirection = "desc";

    /**
     * <h1>身份令牌header的key</h1>
     */
    public static String authorizeHeader = "authorization";

    /**
     * <h1>密码最大长度</h1>
     */
    public static int passwordMaxLength = 16;

    /**
     * <h1>密码最小长度</h1>
     */
    public static int passwordMinLength = 6;
}
