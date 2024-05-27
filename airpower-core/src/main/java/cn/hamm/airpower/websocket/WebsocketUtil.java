package cn.hamm.airpower.websocket;

import cn.hamm.airpower.config.Configs;
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
     * <h2>给所有人发消息</h2>
     *
     * @param message 消息内容
     */
    public final @NotNull WebSocketEvent<WebSocketMessage> sendToAll(WebSocketMessage message) {
        return publish(WebsocketHandler.CHANNEL_ALL, message);
    }

    /**
     * <h2>给指定用户发消息</h2>
     *
     * @param userId  用户ID
     * @param message 消息内容
     */
    public final @NotNull WebSocketEvent<WebSocketMessage> sendToUser(long userId, WebSocketMessage message) {
        return publish(WebsocketHandler.CHANNEL_USER_PREFIX + userId, message);
    }

    /**
     * <h2>发布消息</h2>
     *
     * @param channel 频道
     * @param message 消息
     */
    private @NotNull WebSocketEvent<WebSocketMessage> publish(String channel, WebSocketMessage message) {
        final String channelPrefix = Configs.getWebsocketConfig().getChannelPrefix();
        if (Objects.isNull(channelPrefix) || !StringUtils.hasText(channelPrefix)) {
            throw new ServiceException("没有配置 airpower.websocket.channelPrefix, 无法启动WebSocket服务");
        }
        WebSocketEvent<WebSocketMessage> webSocketEvent = new WebSocketEvent<>();
        webSocketEvent.setData(message);
        switch (Configs.getWebsocketConfig().getSupport()) {
            case REDIS:
                Utils.getRedisUtil().publish(channel, Json.toString(webSocketEvent));
                break;
            case MQTT:
                try {
                    Utils.getMqttUtil().publish(WebsocketHandler.CHANNEL_ALL, Json.toString(webSocketEvent));
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
