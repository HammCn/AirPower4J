package cn.hamm.airpower.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>AirPower配置</h1>
 *
 * @author Hamm.cn
 */
@Component
public class Configs {
    /**
     * <h2>全局Cookie配置</h2>
     */
    @Getter
    private static CookieConfig cookieConfig;

    /**
     * <h2>全局配置</h2>
     */
    @Getter
    private static ServiceConfig serviceConfig;

    /**
     * <h2>MQTT配置</h2>
     */
    @Getter
    private static MqttConfig mqttConfig;

    @Autowired
    Configs(
            CookieConfig cookieConfig,
            ServiceConfig serviceConfig,
            MqttConfig mqttConfig
    ) {
        Configs.cookieConfig = cookieConfig;
        Configs.serviceConfig = serviceConfig;
        Configs.mqttConfig = mqttConfig;
    }
}
