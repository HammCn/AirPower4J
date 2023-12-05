package cn.hamm.airpower.websocket;

import cn.hamm.airpower.mqtt.MqttHelper;
import cn.hamm.airpower.security.SecurityUtil;
import lombok.SneakyThrows;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

/**
 * <h1>WebSocket Handler</h1>
 *
 * @author hamm
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
public class WebsocketHandler extends TextWebSocketHandler implements MessageListener {
    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private MqttHelper mqttHelper;

    /**
     * 收到Websocket消息时
     *
     * @param session     会话
     * @param textMessage 文本消息
     * @throws Exception 异常
     */
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NotNull TextMessage textMessage) throws Exception {
        String message = textMessage.getPayload();
        if (WebsocketConfig.ping.equals(message)) {
            session.sendMessage(new TextMessage(WebsocketConfig.pong));
        }
    }

    /**
     * 连接就绪后监听队列
     *
     * @param session 会话
     * @throws Exception 异常
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        if (Objects.isNull(session.getUri())) {
            return;
        }
        try {
            String accessToken = session.getUri().getQuery();
            if (Objects.isNull(accessToken)) {
                session.close();
                return;
            }
            Long userId = securityUtil.getUserIdFromAccessToken(accessToken);
            MqttClient mqttClient = mqttHelper.createClient();
            mqttClient.setCallback(new MqttCallback() {
                @SneakyThrows
                @Override
                public void connectionLost(Throwable throwable) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    synchronized (session) {
                        session.sendMessage(new TextMessage(mqttMessage.getPayload()));
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
            mqttClient.connect(mqttHelper.createOption());
            String[] topics = {WebsocketConfig.channelAll, WebsocketConfig.channelUserPrefix + userId};
            mqttClient.subscribe(topics);
        } catch (Exception e) {
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        session.close();
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
    }
}
