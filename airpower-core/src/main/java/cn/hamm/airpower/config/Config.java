package cn.hamm.airpower.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>AirPower提供的配置项</h1>
 *
 * @author Hamm.cn
 */
@Component
public class Config {
    /**
     * <h2>全局Cookie配置</h2>
     */
    @Getter
    private static CookieConfig cookieConfig;

    /**
     * <h2>全局配置</h2>
     */
    @Getter
    private static GlobalConfig globalConfig;

    /**
     * <h2>MQTT配置</h2>
     */
    @Getter
    private static MqttConfig mqttConfig;

    @Autowired
    Config(
            CookieConfig cookieConfig,
            GlobalConfig globalConfig,
            MqttConfig mqttConfig
    ) {
        Config.cookieConfig = cookieConfig;
        Config.globalConfig = globalConfig;
        Config.mqttConfig = mqttConfig;
    }
}
