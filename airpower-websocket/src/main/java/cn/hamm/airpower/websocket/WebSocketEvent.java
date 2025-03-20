package cn.hamm.airpower.websocket;

import cn.hamm.airpower.core.util.RandomUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * <h1>{@code WebSocket} 事件</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
public class WebSocketEvent {
    /**
     * <h3>事件 {@code ID}</h3>
     */
    private String id;

    /**
     * <h3>发送方 {@code ID}</h3>
     */
    private Long from;

    /**
     * <h3>接收方 {@code ID}</h3>
     */
    private Long to;

    /**
     * <h3>事件时间戳</h3>
     */
    private Long time;

    /**
     * <h3>事件负载</h3>
     */
    private WebSocketPayload payload;

    /**
     * <h3>创建 {@code WebSocket} 事件</h3>
     *
     * @param payload 负载
     * @return 事件
     */
    public static @NotNull WebSocketEvent create(WebSocketPayload payload) {
        return create().setPayload(payload);
    }

    /**
     * <h3>创建 {@code WebSocket} 事件</h3>
     *
     * @return 事件
     */
    @Contract(" -> new")
    private static @NotNull WebSocketEvent create() {
        return new WebSocketEvent().resetEvent();
    }

    /**
     * <h3>重置事件的 {@code ID} 和事件</h3>
     */
    @Contract(" -> this")
    protected final WebSocketEvent resetEvent() {
        time = System.currentTimeMillis();
        id = Base64.getEncoder().encodeToString((String.format(
                "%s-%s",
                time,
                RandomUtil.randomString(6)
        )).getBytes(StandardCharsets.UTF_8));
        return this;
    }
}