package cn.hamm.airpower.websocket;

import cn.hamm.airpower.mqtt.MqttHelper;
import cn.hamm.airpower.security.SecurityUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
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

import java.io.IOException;
import java.util.Objects;

/**
 * <h1>WebSocket Handler</h1>
 *
 * @author hamm
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
@Slf4j
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
     */
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NotNull TextMessage textMessage) {
        String message = textMessage.getPayload();
        if (WebsocketConfig.ping.equals(message)) {
            try {
                session.sendMessage(new TextMessage(WebsocketConfig.pong));
            } catch (IOException e) {
                log.error("发送Websocket消息失败: " + e.getMessage());
            }
        }
    }

    /**
     * 连接就绪后监听队列
     *
     * @param session 会话
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        if (Objects.isNull(session.getUri())) {
            return;
        }
        try {
            String accessToken = session.getUri().getQuery();
            if (Objects.isNull(accessToken)) {
                log.warn("没有传入AccessToken 即将关闭连接");
                try {
                    session.close();
                } catch (IOException e) {
                    log.error("关闭Websocket失败");
                }
                return;
            }
            Long userId = securityUtil.getUserIdFromAccessToken(accessToken);
            MqttClient mqttClient = mqttHelper.createClient();
            mqttClient.setCallback(new MqttCallback() {
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
        } catch (MqttException e) {
            try {
                session.close();
            } catch (IOException ioException) {
                log.error("关闭Websocket失败");
            }
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        try {
            session.close();
        } catch (IOException e) {
            log.error("关闭Websocket失败");
        }
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
    }
}
