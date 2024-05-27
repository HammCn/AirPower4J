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
public class WebSocketEvent<T extends WebSocketMessage> {
    /**
     * <h2>当前事件ID</h2>
     */
    private static final AtomicLong CURRENT_EVENT_ID = new AtomicLong(Constant.ZERO_LONG);

    /**
     * <h2>事件ID</h2>
     */
    private String id;

    /**
     * <h2>事件名称</h2>
     */
    private String event = "message";

    /**
     * <h2>消息产生时间</h2>
     */
    private Long time;

    /**
     * <h2>消息对象</h2>
     */
    private T data;

    WebSocketEvent() {
        long time = System.currentTimeMillis();
        this.id = Base64.getEncoder().encodeToString((String.format(
                "%s-%s-%s",
                Configs.getServiceConfig().getServiceId(),
                CURRENT_EVENT_ID.incrementAndGet(),
                time
        )).getBytes(StandardCharsets.UTF_8));
        this.time = time;
    }
}