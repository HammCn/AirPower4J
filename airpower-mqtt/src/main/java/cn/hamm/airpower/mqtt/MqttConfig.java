package cn.hamm.airpower.mqtt;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static cn.hamm.airpower.core.constant.Constant.STRING_EMPTY;
import static cn.hamm.airpower.core.request.RequestUtil.LOCAL_IP_ADDRESS;


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
    private String user = STRING_EMPTY;

    /**
     * <h3>密码</h3>
     */
    private String pass = STRING_EMPTY;

    /**
     * <h3>地址</h3>
     */
    private String host = LOCAL_IP_ADDRESS;

    /**
     * <h3>端口</h3>
     */
    private String port = "1883";
}
