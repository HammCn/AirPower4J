package cn.hamm.airpower.websocket;

/**
 * <h1><code>WebSocket</code> 支持</h1>
 *
 * @author Hamm.cn
 */
public enum WebSocketSupport {
    /**
     * <h2><code>Redis</code></h2>
     */
    REDIS,

    /**
     * <h2><code>MQTT</code></h2>
     */
    MQTT,

    /**
     * <h2>不支持</h2>
     */
    NO,
}
