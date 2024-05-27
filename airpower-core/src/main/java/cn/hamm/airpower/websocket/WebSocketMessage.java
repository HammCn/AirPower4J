package cn.hamm.airpower.websocket;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>WebSocket消息基类</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
public class WebSocketMessage {
    /**
     * <h2>消息类型</h2>
     */
    private String type;

    /**
     * <h2>消息内容</h2>
     */
    private Object payload;
}