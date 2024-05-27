package cn.hamm.airpower.websocket;

import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.config.Constant;
import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <h1>WebSocket事件</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
public class WebSocketEvent {
    /**
     * <h2>当前事件ID</h2>
     */
    private static final AtomicLong CURRENT_EVENT_ID = new AtomicLong(Constant.ZERO_LONG);

    /**
     * <h2>事件ID</h2>
     */
    private String id;

    /**
     * <h2>事件时间戳</h2>
     */
    private Long time;

    /**
     * <h2>事件负载</h2>
     */
    private WebSocketPayload payload;

    /**
     * <h2>构造方法</h2>
     */
    private WebSocketEvent() {
        long time = System.currentTimeMillis();
        this.id = Base64.getEncoder().encodeToString((String.format(
                "%s-%s-%s",
                Configs.getServiceConfig().getServiceId(),
                CURRENT_EVENT_ID.incrementAndGet(),
                time
        )).getBytes(StandardCharsets.UTF_8));
        this.time = time;
    }

    /**
     * <h2>创建WebSocket事件</h2>
     *
     * @param payload 负载
     * @return 事件
     */
    static WebSocketEvent create(WebSocketPayload payload) {
        return new WebSocketEvent().setPayload(payload);
    }
}