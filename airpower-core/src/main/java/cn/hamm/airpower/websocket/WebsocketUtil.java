package cn.hamm.airpower.websocket;

import cn.hamm.airpower.mqtt.MqttHelper;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>WebsocketUtil</h1>
 *
 * @author Hamm
 */
@Component
@SuppressWarnings("unused")
public class WebsocketUtil {
    @Autowired
    private MqttHelper mqttHelper;

    /**
     * 给所有人发消息
     *
     * @param message 消息内容
     * @throws MqttException 异常
     */
    public void sendToAll(String message) throws MqttException {
        mqttHelper.publish(WebsocketConfig.channelAll, message);
    }

    /**
     * 给指定用户发消息
     *
     * @param userId  用户ID
     * @param message 消息内容
     * @throws MqttException 异常
     */
    public void sendToUser(Long userId, String message) throws MqttException {
        mqttHelper.publish(WebsocketConfig.channelUserPrefix + userId.toString(), message);
    }
}
