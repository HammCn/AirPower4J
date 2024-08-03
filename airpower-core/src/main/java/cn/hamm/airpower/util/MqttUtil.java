package cn.hamm.airpower.util;

import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.config.MessageConstant;
import cn.hamm.airpower.config.MqttConfig;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * <h1>{@code MQTT} 助手类</h1>
 *
 * @author Hamm.cn
 */
@Configuration
@Slf4j
public class MqttUtil {

    /**
     * <h2>创建 {@code MQTT} 客户端</h2>
     *
     * @return 配置
     * @throws MqttException 异常
     */
    public @NotNull MqttClient createClient() throws MqttException {
        return createClient(UUID.randomUUID().toString());
    }

    /**
     * <h2>创建 {@code MQTT} 客户端</h2>
     *
     * @param id 客户端 {@code ID}
     * @return 配置
     * @throws MqttException 异常
     */
    public @NotNull MqttClient createClient(String id) throws MqttException {
        MqttConfig mqttConfig = Configs.getMqttConfig();
        return new MqttClient(
                "tcp://" + mqttConfig.getHost() + ":" + mqttConfig.getPort(),
                id,
                new MemoryPersistence()
        );
    }

    /**
     * <h2>创建配置</h2>
     *
     * @return 配置
     */
    public MqttConnectOptions createOption() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        MqttConfig mqttConfig = Configs.getMqttConfig();
        options.setUserName(mqttConfig.getUser());
        options.setPassword(mqttConfig.getPass().toCharArray());
        options.setConnectionTimeout(30);
        options.setKeepAliveInterval(10);
        return options;
    }

    /**
     * <h2>发送消息</h2>
     *
     * @param topic   主题
     * @param message 消息内容
     */
    public void publish(@NotNull String topic, @NotNull String message) throws MqttException {
        MqttClient client = createClient();
        client.connect(createOption());
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBytes());
        mqttMessage.setQos(0);
        MqttTopic mqttTopic = client.getTopic(topic);
        MqttDeliveryToken token;
        try {
            token = mqttTopic.publish(mqttMessage);
            token.waitForCompletion();
            client.disconnect();
            client.close();
        } catch (MqttException exception) {
            log.error(MessageConstant.EXCEPTION_WHEN_MQTT_PUBLISH, exception);
        }
    }
}
