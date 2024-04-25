package cn.hamm.airpower.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>🇨🇳AirPower配置类</h1>
 * <hr/>
 * <h3>🔥按 <code>A</code>、<code>I</code>、<code>R</code> 打开新大陆🔥</h3>
 * <hr/>
 *
 * @author Hamm.cn
 */
@Component
public class AirConfig {
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
    AirConfig(
            CookieConfig cookieConfig,
            GlobalConfig globalConfig,
            MqttConfig mqttConfig
    ) {
        AirConfig.cookieConfig = cookieConfig;
        AirConfig.globalConfig = globalConfig;
        AirConfig.mqttConfig = mqttConfig;
    }
}
