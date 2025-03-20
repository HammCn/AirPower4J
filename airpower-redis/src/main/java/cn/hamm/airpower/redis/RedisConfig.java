package cn.hamm.airpower.redis;

import cn.hamm.airpower.core.datetime.DateTimeUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <h1>环境变量配置</h1>
 *
 * @author Hamm.cn
 */
@Component
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties("airpower.redis")
public class RedisConfig {

    /**
     * <h3>缓存过期时间</h3>
     */
    private int cacheExpireSecond = DateTimeUtil.SECOND_PER_MINUTE;
}
