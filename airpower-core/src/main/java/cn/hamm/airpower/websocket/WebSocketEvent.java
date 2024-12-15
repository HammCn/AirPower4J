package cn.hamm.airpower.websocket;

import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.config.Constant;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
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
     * <h3>当前事件 {@code ID}</h3>
     */
    private static final AtomicLong CURRENT_EVENT_ID = new AtomicLong(Constant.ZERO_LONG);

    /**
     * <h3>事件 {@code ID}</h3>
     */
    private String id;

    /**
     * <h3>发送方 {@code ID}</h3>
     */
    private long from = Constant.ZERO_LONG;

    /**
     * <h3>接收方 {@code ID}</h3>
     */
    private long to = Constant.ZERO_LONG;

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
                "%s-%s-%s",
                Configs.getServiceConfig().getServiceId(),
                CURRENT_EVENT_ID.incrementAndGet(),
                time
        )).getBytes(StandardCharsets.UTF_8));
        return this;
    }
}