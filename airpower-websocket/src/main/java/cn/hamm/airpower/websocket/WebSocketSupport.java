package cn.hamm.airpower.websocket;

/**
 * <h1>{@code WebSocket} 支持</h1>
 *
 * @author Hamm.cn
 */
public enum WebSocketSupport {
    /**
     * <h3>{@code Redis}</h3>
     */
    REDIS,

    /**
     * <h3>{@code MQTT}</h3>
     */
    MQTT,

    /**
     * <h3>不支持</h3>
     */
    NO,
}
