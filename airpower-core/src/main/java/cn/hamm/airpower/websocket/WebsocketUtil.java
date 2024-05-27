package cn.hamm.airpower.websocket;

import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.model.Json;
import cn.hamm.airpower.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.jetbrains.annotations.NotNull;
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
    /**
     * <h2>发布事件负载</h2>
     *
     * @param payload 事件负载
     */
    public final @NotNull WebSocketEvent publish(WebSocketPayload payload) {
        return publishToChannel(WebSocketHandler.CHANNEL_ALL, payload);
    }

    /**
     * <h2>发布事件负载到指定的用户</h2>
     *
     * @param userId  用户ID
     * @param payload 事件负载
     */
    public final @NotNull WebSocketEvent publishToUser(long userId, WebSocketPayload payload) {
        return publishToChannel(WebSocketHandler.CHANNEL_USER_PREFIX + userId, payload);
    }

    /**
     * <h2>发布事件负载到指定的频道</h2>
     *
     * @param channel 频道
     * @param payload 事件负载
     */
    public final @NotNull WebSocketEvent publishToChannel(String channel, WebSocketPayload payload) {
        final String channelPrefix = Configs.getWebsocketConfig().getChannelPrefix();
        if (Objects.isNull(channelPrefix) || !StringUtils.hasText(channelPrefix)) {
            throw new ServiceException("没有配置 airpower.websocket.channelPrefix, 无法启动WebSocket服务");
        }
        WebSocketEvent webSocketEvent = WebSocketEvent.create(payload);
        switch (Configs.getWebsocketConfig().getSupport()) {
            case REDIS:
                Utils.getRedisUtil().publish(channelPrefix + Constant.UNDERLINE + channel, Json.toString(webSocketEvent));
                break;
            case MQTT:
                try {
                    Utils.getMqttUtil().publish(channelPrefix + Constant.UNDERLINE + WebSocketHandler.CHANNEL_ALL, Json.toString(webSocketEvent));
                } catch (MqttException e) {
                    throw new RuntimeException("发布消息失败", e);
                }
                break;
            default:
                throw new RuntimeException("WebSocket暂不支持");
        }
        return webSocketEvent;
    }
}
