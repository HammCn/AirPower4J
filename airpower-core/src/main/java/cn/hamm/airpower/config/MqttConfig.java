package cn.hamm.airpower.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * MQTT配置类
 *
 * @author Hamm
 */
@Component
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties("airpower.mqtt")
public class MqttConfig {
    /**
     * 用户
     */
    private String user = "";

    /**
     * 密码
     */
    private String pass = "";

    /**
     * 地址
     */
    private String host = "127.0.0.1";

    /**
     * 端口
     */
    private String port = "1883";
}
