package cn.hamm.airpower.websocket;

import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.config.Constant;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <h1>{@code WebSocket} 事件</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
public class WebSocketEvent {
    /**
     * <h2>当前事件 {@code ID}</h2>
     */
    private static final AtomicLong CURRENT_EVENT_ID = new AtomicLong(Constant.ZERO_LONG);

    /**
     * <h2>事件 {@code ID}</h2>
     */
    private String id;

    /**
     * <h2>发送方 {@code ID}</h2>
     */
    private long from = Constant.ZERO_LONG;

    /**
     * <h2>接收方 {@code ID}</h2>
     */
    private long to = Constant.ZERO_LONG;

    /**
     * <h2>事件时间戳</h2>
     */
    private Long time;

    /**
     * <h2>事件负载</h2>
     */
    private WebSocketPayload payload;

    /**
     * <h2>创建 {@code WebSocket} 事件</h2>
     *
     * @param payload 负载
     * @return 事件
     */
    public static @NotNull WebSocketEvent create(WebSocketPayload payload) {
        return create().setPayload(payload);
    }

    /**
     * <h2>创建 {@code WebSocket} 事件</h2>
     *
     * @return 事件
     */
    private static @NotNull WebSocketEvent create() {
        WebSocketEvent webSocketEvent = new WebSocketEvent();
        webSocketEvent.resetEvent();
        return webSocketEvent;
    }

    /**
     * <h2>重置事件的 {@code ID} 和事件</h2>
     */
    protected final void resetEvent() {
        time = System.currentTimeMillis();
        id = Base64.getEncoder().encodeToString((String.format(
                "%s-%s-%s",
                Configs.getServiceConfig().getServiceId(),
                CURRENT_EVENT_ID.incrementAndGet(),
                time
        )).getBytes(StandardCharsets.UTF_8));
    }
}