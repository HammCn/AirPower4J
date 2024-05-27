package cn.hamm.airpower.websocket;

import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.model.Json;
import cn.hamm.airpower.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <h1>WebSocket Handler</h1>
 *
 * @author Hamm
 */
@Component
@Slf4j
public class WebsocketHandler extends TextWebSocketHandler implements MessageListener {
    /**
     * <h2>Redis连接工厂</h2>
     */
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * <h2>订阅全频道</h2>
     */
    public static String CHANNEL_ALL = "WEBSOCKET_ALL";

    /**
     * <h2>订阅用户频道前缀</h2>
     */
    public static String CHANNEL_USER_PREFIX = "WEBSOCKET_USER_";

    /**
     * <h2>收到Websocket消息时</h2>
     *
     * @param session     会话
     * @param textMessage 文本消息
     */
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NotNull TextMessage textMessage) {
        final String message = textMessage.getPayload();
        if (Configs.getWebsocketConfig().getPing().equals(message)) {
            try {
                WebSocketEvent<WebSocketMessage> webSocketEvent = new WebSocketEvent<>().setEvent(Configs.getWebsocketConfig().getPong());
                session.sendMessage(new TextMessage(Json.toString(webSocketEvent)));
            } catch (IOException e) {
                log.error("发送Websocket消息失败: {}", e.getMessage());
            }
        }
    }

    /**
     * <h2>连接就绪后监听队列</h2>
     *
     * @param session 会话
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        if (Objects.isNull(session.getUri())) {
            return;
        }
        String accessToken = session.getUri().getQuery();
        if (Objects.isNull(accessToken)) {
            log.warn("没有传入AccessToken 即将关闭连接");
            closeConnection(session);
            return;
        }
        long userId = Utils.getSecurityUtil().getIdFromAccessToken(accessToken);
        switch (Configs.getWebsocketConfig().getSupport()) {
            case REDIS:
                startRedisListener(session, userId);
                break;
            case MQTT:
                startMqttListener(session, userId);
                break;
            default:
                throw new RuntimeException("WebSocket暂不支持");
        }
    }

    /**
     * <h2>处理监听到的频道消息</h2>
     *
     * @param message 消息
     * @param session 连接
     */
    private void onChannelMessage(@NotNull String message, @NonNull WebSocketSession session) {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (Exception exception) {
            log.error("消息发送失败", exception);
        }
    }

    /**
     * <h2>开始监听Redis消息</h2>
     *
     * @param session 连接
     * @param userId  用户ID
     */
    private void startRedisListener(@NotNull WebSocketSession session, long userId) {
        final String personalChannel = CHANNEL_USER_PREFIX + userId;
        redisConnectionFactory.getConnection().subscribe(
                (message, pattern) -> onChannelMessage(new String(message.getBody(), StandardCharsets.UTF_8), session),
                CHANNEL_ALL.getBytes(StandardCharsets.UTF_8),
                personalChannel.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * <h2>开始监听MQTT消息</h2>
     *
     * @param session WebSocket会话
     * @param userId  用户ID
     */
    private void startMqttListener(@NotNull WebSocketSession session, long userId) {
        try (MqttClient mqttClient = Utils.getMqttUtil().createClient()) {
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) {
                    synchronized (session) {
                        onChannelMessage(new String(mqttMessage.getPayload(), StandardCharsets.UTF_8), session);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
            mqttClient.connect(Utils.getMqttUtil().createOption());
            final String personalChannel = CHANNEL_USER_PREFIX + userId;
            String[] topics = {CHANNEL_ALL, personalChannel};
            mqttClient.subscribe(topics);
        } catch (MqttException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
    }

    /**
     * <h2>关闭连接</h2>
     *
     * @param session 会话
     */
    private void closeConnection(@NotNull WebSocketSession session) {
        try {
            session.close();
        } catch (IOException e) {
            log.error("关闭Websocket失败");
        }
    }
}
