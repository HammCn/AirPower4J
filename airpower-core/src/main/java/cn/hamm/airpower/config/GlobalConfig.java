package cn.hamm.airpower.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <h1>全局默认配置文件</h1>
 *
 * @author Hamm
 */
@Component
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties("airpower")
public class GlobalConfig {
    /**
     * 默认分页条数
     */
    private int defaultPageSize = 20;

    /**
     * 多数据源数据库前缀
     */
    public static String databasePrefix = "tenant_";

    /**
     * 服务全局拦截
     */
    private boolean isServiceRunning = true;

    /**
     * 是否开启缓存
     */
    private boolean cache = false;

    /**
     * 缓存过期时间
     */
    private int cacheExpireSecond = Constant.SECOND_PER_MINUTE;

    /**
     * 默认排序字段
     */
    private String defaultSortField = Constant.CREATE_TIME_FIELD;

    /**
     * 默认排序方向
     */
    private String defaultSortDirection = Constant.SORT_DESC;

    /**
     * 身份令牌header的key
     */
    private String authorizeHeader = "authorization";

    /**
     * 身份令牌有效期
     */
    private int authorizeExpireTime = Constant.SECOND_PER_DAY;

    /**
     * 多租户的header的key
     */
    private String tenantHeader = "tenant-code";

    /**
     * 是否开启调试模式
     *
     * @apiNote 调试模式打开，控制台将输出部分错误堆栈信息等
     */
    private boolean debug = true;

    /**
     * MQTT配置
     */
    private MqttConfig mqtt = new MqttConfig();

    /**
     * Cookie配置
     */
    private CookieConfig cookie = new CookieConfig();
}
