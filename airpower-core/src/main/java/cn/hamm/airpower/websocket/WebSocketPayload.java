package cn.hamm.airpower.websocket;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>WebSocket事件负载</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
public class WebSocketPayload {
    /**
     * <h2>负载类型</h2>
     */
    private String type;

    /**
     * <h2>负载数据</h2>
     */
    private String data;
}