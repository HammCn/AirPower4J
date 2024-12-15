package cn.hamm.airpower.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <h1>{@code MQTT} 配置类</h1>
 *
 * @author Hamm.cn
 */
@Component
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties("airpower.mqtt")
public class MqttConfig {
    /**
     * <h3>用户</h3>
     */
    private String user = Constant.EMPTY_STRING;

    /**
     * <h3>密码</h3>
     */
    private String pass = Constant.EMPTY_STRING;

    /**
     * <h3>地址</h3>
     */
    private String host = Constant.LOCAL_IP_ADDRESS;

    /**
     * <h3>端口</h3>
     */
    private String port = "1883";
}
