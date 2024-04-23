package cn.hamm.airpower.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <h1>MQTT配置类</h1>
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
     * <h2>用户</h2>
     */
    private String user = "";

    /**
     * <h2>密码</h2>
     */
    private String pass = "";

    /**
     * <h2>地址</h2>
     */
    private String host = Constant.LOCAL_IP_ADDRESS;

    /**
     * <h2>端口</h2>
     */
    private String port = "1883";
}
