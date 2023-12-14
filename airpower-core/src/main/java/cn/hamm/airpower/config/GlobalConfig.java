package cn.hamm.airpower.config;

import cn.hamm.airpower.validate.password.Password;
import cn.hamm.airpower.validate.password.PasswordAnnotationValidator;

/**
 * <h1>全局默认配置文件</h1>
 *
 * @author Hamm
 */
@SuppressWarnings("CanBeFinal")
public class GlobalConfig {
    /**
     * 服务全局拦截
     */
    public static boolean isServiceRunning = true;

    /**
     * 多数据源数据库前缀
     */
    public static String databasePrefix = "tenant_";

    /**
     * 是否开启缓存
     */
    public static boolean isCacheEnabled = true;

    /**
     * 缓存过期时间
     */
    public static int cacheExpTime = 3600;

    /**
     * 默认分页条数
     */
    public static int defaultPageSize = 20;

    /**
     * 默认排序字段
     */
    public static String defaultSortField = "id";

    /**
     * 默认排序方向
     */
    public static String defaultSortDirection = "desc";

    /**
     * 身份令牌header的key
     */
    public static String authorizeHeader = "authorization";

    /**
     * 身份令牌有效期
     */
    public static int authorizeExpireTime = 86400;

    /**
     * 多租户的header的key
     */
    public static String tenantHeader = "tenant-code";

    /**
     * 应用版本号header的key
     */
    public static String appVersionHeader = "app-version";

    /**
     * 密码最大长度
     *
     * @see PasswordAnnotationValidator
     * @see Password
     */
    public static int passwordMaxLength = 16;

    /**
     * 密码最小长度
     *
     * @see PasswordAnnotationValidator
     * @see Password
     */
    public static int passwordMinLength = 6;
}
