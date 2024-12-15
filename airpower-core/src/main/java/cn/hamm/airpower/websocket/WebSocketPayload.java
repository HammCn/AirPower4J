package cn.hamm.airpower.websocket;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>{@code WebSocket} 事件负载</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
public class WebSocketPayload {
    /**
     * <h3>负载类型</h3>
     */
    private String type = "system";

    /**
     * <h3>负载数据</h3>
     */
    private String data;
}