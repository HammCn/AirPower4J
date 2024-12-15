package cn.hamm.airpower.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>{@code AirPower} 配置</h1>
 *
 * @author Hamm.cn
 */
@Component
public class Configs {
    /**
     * <h3>全局 {@code Cookie} 配置</h3>
     */
    @Getter
    private static CookieConfig cookieConfig;

    /**
     * <h3>全局配置</h3>
     */
    @Getter
    private static ServiceConfig serviceConfig;

    /**
     * <h3>{@code MQTT} 配置</h3>
     */
    @Getter
    private static MqttConfig mqttConfig;

    /**
     * <h3>{@code WebSocket} 配置</h3>
     */
    @Getter
    private static WebSocketConfig websocketConfig;

    @Autowired
    Configs(
            CookieConfig cookieConfig,
            ServiceConfig serviceConfig,
            MqttConfig mqttConfig,
            WebSocketConfig websocketConfig
    ) {
        Configs.cookieConfig = cookieConfig;
        Configs.serviceConfig = serviceConfig;
        Configs.mqttConfig = mqttConfig;
        Configs.websocketConfig = websocketConfig;
    }
}
