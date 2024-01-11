package cn.hamm.airpower.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * <h1>MQTT助手类</h1>
 *
 * @author Hamm
 */
@Configuration
public class MqttHelper {
    /**
     * 用户
     */
    @Value("${spring.mqtt.user}")
    private String user;

    /**
     * 密码
     */
    @Value("${spring.mqtt.pass}")
    private String pass;

    /**
     * 地址
     */
    @Value("${spring.mqtt.host}")
    private String host;

    /**
     * 端口
     */
    @Value("${spring.mqtt.port}")
    private String port;


    /**
     * 创建MQTT客户端
     *
     * @return 配置
     * @throws MqttException 异常
     */
    public MqttClient createClient() throws MqttException {
        return createClient(UUID.randomUUID().toString());
    }

    /**
     * 创建MQTT客户端
     *
     * @param id 客户端ID
     * @return 配置
     * @throws MqttException 异常
     */
    public MqttClient createClient(String id) throws MqttException {
        return new MqttClient(
                "tcp://" + host + ":" + port,
                id,
                new MemoryPersistence()
        );
    }


    /**
     * 创建配置
     *
     * @return 配置
     */
    public MqttConnectOptions createOption() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(user);
        options.setPassword(pass.toCharArray());
        options.setConnectionTimeout(30);
        options.setKeepAliveInterval(10);
        return options;
    }

    /**
     * 发送消息
     *
     * @param topic   主题
     * @param message 消息内容
     */
    public void publish(String topic, String message) throws MqttException {
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
        } catch (MqttException ignored) {
        }
    }
}
