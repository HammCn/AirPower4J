package cn.hamm.airpower.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * <h1>全局默认配置文件</h1>
 *
 * @author Hamm.cn
 */
@Component
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties("airpower")
public class ServiceConfig {
    /**
     * <h2>多数据源数据库前缀</h2>
     */
    private String databasePrefix = "tenant_";

    /**
     * <h2>是否开启文档</h2>
     */
    private boolean enableDocument = false;

    /**
     * <h2>默认分页条数</h2>
     */
    private int defaultPageSize = 20;

    /**
     * <h2>服务全局拦截</h2>
     */
    private boolean isServiceRunning = true;

    /**
     * <h2>是否开启缓存</h2>
     */
    private boolean cache = false;

    /**
     * <h2>缓存过期时间</h2>
     */
    private int cacheExpireSecond = Constant.SECOND_PER_MINUTE;

    /**
     * <h2>默认排序字段</h2>
     */
    private String defaultSortField = Constant.CREATE_TIME_FIELD;

    /**
     * <h2>默认排序方向</h2>
     */
    private String defaultSortDirection = "desc";

    /**
     * <h2>身份令牌header的key</h2>
     */
    private String authorizeHeader = HttpHeaders.AUTHORIZATION;

    /**
     * <h2>身份令牌有效期</h2>
     */
    private int authorizeExpireTime = Constant.SECOND_PER_DAY;

    /**
     * <h2>多租户的header的key</h2>
     */
    private String tenantHeader = "tenant-code";

    /**
     * <h2>是否开启调试模式</h2>
     *
     * @apiNote 调试模式打开，控制台将输出部分错误堆栈信息等
     */
    private boolean debug = true;

    /**
     * <h2>MQTT配置</h2>
     */
    private MqttConfig mqtt = new MqttConfig();

    /**
     * <h2>Cookie配置</h2>
     */
    private CookieConfig cookie = new CookieConfig();
}
