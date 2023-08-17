package cn.hamm.airpower.config;

/**
 * <h1>全局默认配置文件</h1>
 *
 * @author Hamm
 */
public class GlobalConfig {
    /**
     * <h2>主数据源</h2>
     * <p>
     * 启动多数据源时, 此项将自动作为主数据源
     * </p>
     */
    public static String primaryDataBase = "service";

    /**
     * <h2>数据库默认地址</h2>
     */
    public static String defaultDatabaseHost = "127.0.0.1";

    /**
     * <h2>数据库默认端口</h2>
     */
    public static int defaultDatabasePort = 3306;

    /**
     * <h2>部署API主域名</h2>
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
     * <h2>密码最大长度</h2>
     */
    public static int passwordMaxLength = 16;

    /**
     * <h2>密码最小长度</h2>
     */
    public static int passwordMinLength = 6;

    /**
     * <h2>统一登录的路径</h2>
     *
     * 如果需要Oauth2登录,则请将前后端部署到一个站点下, 且按下面的流程反向代理
     * <li>反向代理 oauth2 路径到后端项目</li>
     * <li>反向代理 api   路径到后端项目,且去除 api</li>
     */
    public static String loginPath = "http://localhost:3000/login";
}
