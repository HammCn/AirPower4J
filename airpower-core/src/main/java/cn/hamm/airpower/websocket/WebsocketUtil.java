package cn.hamm.airpower.websocket;

import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.config.WebSocketConfig;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.model.Json;
import cn.hamm.airpower.util.MqttUtil;
import cn.hamm.airpower.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * <h1>WebsocketUtil</h1>
 *
 * @author Hamm
 */
@Slf4j
@Component
public class WebsocketUtil {
    @Autowired
    private WebSocketConfig websocketConfig;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MqttUtil mqttUtil;

    /**
     * <h2>发布事件负载</h2>
     *
     * @param payload 事件负载
     */
    public final void publish(WebSocketPayload payload) {
        publishToChannel(WebSocketHandler.CHANNEL_ALL, payload);
    }

    /**
     * <h2>发布事件负载到指定的用户</h2>
     *
     * @param userId  目标用户 {@code ID}
     * @param payload 事件负载
     */
    public final void publishToUser(long userId, WebSocketPayload payload) {
        publishToChannel(WebSocketHandler.CHANNEL_USER_PREFIX + userId, payload);
    }

    /**
     * <h2>发布事件负载到指定的频道</h2>
     *
     * @param channel 频道
     * @param payload 负载
     */
    public final void publishToChannel(String channel, WebSocketPayload payload) {
        final String channelPrefix = websocketConfig.getChannelPrefix();
        if (Objects.isNull(channelPrefix) || !StringUtils.hasText(channelPrefix)) {
            throw new ServiceException("没有配置 airpower.websocket.channelPrefix, 无法启动WebSocket服务");
        }
        final String targetChannel = channelPrefix + Constant.UNDERLINE + channel;
        final WebSocketEvent event = WebSocketEvent.create(payload);
        log.info("发布消息到频道 {} : {}", targetChannel, Json.toString(event));
        try {
            switch (websocketConfig.getSupport()) {
                case REDIS -> redisUtil.publish(targetChannel, Json.toString(event));
                case MQTT -> mqttUtil.publish(targetChannel, Json.toString(event));
                default -> throw new RuntimeException("WebSocket暂不支持");
            }
        } catch (MqttException e) {
            throw new RuntimeException("发布消息失败", e);
        }
    }
}
