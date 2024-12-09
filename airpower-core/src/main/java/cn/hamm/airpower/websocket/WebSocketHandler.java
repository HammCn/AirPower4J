package cn.hamm.airpower.websocket;

import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.config.WebSocketConfig;
import cn.hamm.airpower.exception.ServiceError;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.helper.MqttHelper;
import cn.hamm.airpower.model.Json;
import cn.hamm.airpower.util.AccessTokenUtil;
import cn.hamm.airpower.util.TaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h1>WebSocket Handler</h1>
 *
 * @author Hamm
 */
@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler implements MessageListener {
    /**
     * <h2>订阅全频道</h2>
     */
    public static final String CHANNEL_ALL = "WEBSOCKET_ALL";

    /**
     * <h2>订阅用户频道前缀</h2>
     */
    public static final String CHANNEL_USER_PREFIX = "WEBSOCKET_USER_";

    /**
     * <h2>{@code Redis} 连接列表</h2>
     */
    protected final ConcurrentHashMap<String, RedisConnection> redisConnectionHashMap = new ConcurrentHashMap<>();

    /**
     * <h2>{@code MQTT} 客户端列表</h2>
     */
    protected final ConcurrentHashMap<String, MqttClient> mqttClientHashMap = new ConcurrentHashMap<>();

    /**
     * <h2>用户 {@code ID} 列表</h2>
     */
    protected final ConcurrentHashMap<String, Long> userIdHashMap = new ConcurrentHashMap<>();
    /**
     * <h2>{@code WebSocket}配置</h2>
     */
    @Autowired
    protected WebSocketConfig webSocketConfig;
    /**
     * <h2>{@code Redis} 连接工厂</h2>
     */
    @Autowired
    protected RedisConnectionFactory redisConnectionFactory;

    @Autowired
    protected MqttHelper mqttHelper;

    /**
     * <h2>收到 {@code Websocket} 消息时</h2>
     *
     * @param session     会话
     * @param textMessage 文本消息
     */
    @Override
    protected final void handleTextMessage(@NonNull WebSocketSession session, @NotNull TextMessage textMessage) {
        final String message = textMessage.getPayload();
        if (webSocketConfig.getPing().equalsIgnoreCase(message)) {
            try {
                session.sendMessage(new TextMessage(webSocketConfig.getPong()));
            } catch (Exception e) {
                log.error("发送Websocket消息失败: {}", e.getMessage());
            }
            return;
        }
        WebSocketPayload webSocketPayload = Json.parse(message, WebSocketPayload.class);
        onWebSocketPayload(webSocketPayload, session);
    }

    /**
     * <h2>发送 {@code Websocket} 事件负载</h2>
     *
     * @param session          会话
     * @param webSocketPayload 事件负载
     */
    protected final void sendWebSocketPayload(@NotNull WebSocketSession session,
                                              @NotNull WebSocketPayload webSocketPayload) {
        try {
            session.sendMessage(new TextMessage(Json.toString(WebSocketEvent.create(webSocketPayload))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <h2>当 {@code WebSocket} 负载到达时</h2>
     *
     * @param webSocketPayload 负载对象
     */
    public void onWebSocketPayload(@NotNull WebSocketPayload webSocketPayload, @NotNull WebSocketSession session) {
        log.info("负载类型: {}, 负载内容: {}", webSocketPayload.getType(), webSocketPayload.getData());
    }

    /**
     * <h2>连接就绪后监听队列</h2>
     *
     * @param session 会话
     */
    @Override
    public final void afterConnectionEstablished(@NonNull WebSocketSession session) {
        if (Objects.isNull(session.getUri())) {
            return;
        }
        String accessToken = session.getUri().getQuery();
        if (Objects.isNull(accessToken)) {
            log.warn("没有传入AccessToken 即将关闭连接");
            closeConnection(session);
            return;
        }
        long userId = AccessTokenUtil.create()
                .getPayloadId(accessToken, Configs.getServiceConfig().getAccessTokenSecret());
        switch (webSocketConfig.getSupport()) {
            case REDIS -> startRedisListener(session, userId);
            case MQTT -> startMqttListener(session, userId);
            case NO -> {
            }
            default -> throw new RuntimeException("WebSocket暂不支持");
        }
        userIdHashMap.put(session.getId(), userId);
        TaskUtil.run(() -> afterConnectSuccess(session));
    }

    /**
     * <h2>连接成功后置方法</h2>
     *
     * @param session 会话
     */
    protected void afterConnectSuccess(@NonNull WebSocketSession session) {

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
     * <h2>开始监听 {@code Redis} 消息</h2>
     *
     * @param session {@code WebSocket} 会话
     * @param userId  用户 {@code ID}
     */
    private void startRedisListener(@NotNull WebSocketSession session, long userId) {
        final String personalChannel = getRealChannel(CHANNEL_USER_PREFIX + userId);
        RedisConnection redisConnection = redisConnectionFactory.getConnection();
        redisConnectionHashMap.put(session.getId(), redisConnection);
        redisConnection.subscribe((message, pattern) -> {
                    synchronized (session) {
                        onChannelMessage(new String(message.getBody(), StandardCharsets.UTF_8), session);
                    }
                },
                getRealChannel(CHANNEL_ALL).getBytes(StandardCharsets.UTF_8),
                personalChannel.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * <h2>开始监听 {@code MQTT} 消息</h2>
     *
     * @param session {@code WebSocket} 会话
     * @param userId  用户 {@code ID}
     */
    private void startMqttListener(@NotNull WebSocketSession session, long userId) {
        try (MqttClient mqttClient = mqttHelper.createClient()) {
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
            mqttClient.connect(mqttHelper.createOption());
            final String personalChannel = CHANNEL_USER_PREFIX + userId;
            String[] topics = {CHANNEL_ALL, personalChannel};
            mqttClient.subscribe(topics);
            mqttClientHashMap.put(session.getId(), mqttClient);
        } catch (MqttException e) {
            throw new ServiceException(e);
        }
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

    @Contract(pure = true)
    @Override
    public final void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        try {
            String sessionId = session.getId();
            if (Objects.nonNull(redisConnectionHashMap.get(sessionId))) {
                redisConnectionHashMap.remove(sessionId).close();
            }
            if (Objects.nonNull(mqttClientHashMap.get(sessionId))) {
                mqttClientHashMap.remove(sessionId).close();
            }
            if (Objects.nonNull(userIdHashMap.get(sessionId))) {
                userIdHashMap.remove(sessionId);
            }
            TaskUtil.run(() -> {
                afterDisconnect(session);
            });
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    /**
     * <h2>断开连接后置方法</h2>
     *
     * @param session 会话
     */
    protected void afterDisconnect(@NonNull WebSocketSession session) {

    }

    @Contract(pure = true)
    @Override
    public final void onMessage(@NotNull Message message, byte[] pattern) {
    }

    /**
     * <h2>{@code REDIS} 订阅</h2>
     *
     * @param channel 传入的频道
     * @param session {@code WebSocket} 会话
     */
    protected final void redisSubscribe(@NotNull String channel, WebSocketSession session) {
        log.info("REDIS开始订阅频道: {}", getRealChannel(channel));
        getRedisSubscription(session).subscribe(getRealChannel(channel).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * <h2>{@code MQTT} 订阅</h2>
     *
     * @param channel 传入的频道
     * @param session {@code WebSocket} 会话
     */
    protected final void mqttSubscribe(String channel, WebSocketSession session) {
        log.info("MQTT开始订阅频道: {}", getRealChannel(channel));
        try {
            getMqttClient(session).subscribe(getRealChannel(channel));
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <h2>获取真实的频道</h2>
     *
     * @param channel 传入的频道
     * @return 带前缀的真实频道
     */
    @Contract(pure = true)
    protected final @NotNull String getRealChannel(String channel) {
        return webSocketConfig.getChannelPrefix() + Constant.UNDERLINE + channel;
    }

    /**
     * <h2>{@code Redis} 取消订阅</h2>
     *
     * @param channel 传入的频道
     * @param session {@code WebSocket} 会话
     */
    protected final void redisUnSubscribe(@NotNull String channel, WebSocketSession session) {
        log.info("REDIS取消订阅频道: {}", getRealChannel(channel));
        getRedisSubscription(session).unsubscribe(getRealChannel(channel).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * <h2>{@code MQTT} 取消订阅</h2>
     *
     * @param channel 传入的频道
     * @param session {@code WebSocket} 会话
     */
    protected final void mqttUnSubscribe(String channel, WebSocketSession session) {
        log.info("MQTT取消订阅频道: {}", getRealChannel(channel));
        try {
            getMqttClient(session).unsubscribe(getRealChannel(channel));
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <h2>获取 {@code MQTT} 客户端</h2>
     *
     * @param session {@code WebSocket} 会话
     * @return {@code MQTT} 客户端
     */
    protected final MqttClient getMqttClient(@NotNull WebSocketSession session) {
        MqttClient mqttClient = mqttClientHashMap.get(session.getId());
        ServiceError.WEBSOCKET_ERROR.whenNull(mqttClient, "mqttClient is null");
        return mqttClient;
    }

    /**
     * <h2>获取 {@code Redis} 订阅</h2>
     *
     * @param session {@code WebSocket} 会话
     * @return {@code Redis} 订阅
     */
    protected final Subscription getRedisSubscription(@NotNull WebSocketSession session) {
        RedisConnection redisConnection = redisConnectionHashMap.get(session.getId());
        ServiceError.WEBSOCKET_ERROR.whenNull(redisConnection, "redisConnection is null");
        Subscription subscription = redisConnection.getSubscription();
        ServiceError.WEBSOCKET_ERROR.whenNull(subscription, "subscription is null");
        return subscription;
    }
}
