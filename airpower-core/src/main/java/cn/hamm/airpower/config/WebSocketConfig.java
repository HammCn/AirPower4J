package cn.hamm.airpower.config;

import cn.hamm.airpower.websocket.WebSocketSupport;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * <h1>{@code WebSocket} 配置</h1>
 *
 * @author Hamm
 */
@Component
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties("airpower.websocket")
public class WebSocketConfig {
    /**
     * <h2>{@code PING}</h2>
     */
    private String ping = "PING";

    /**
     * <h2>{@code PONG}</h2>
     */
    private String pong = "PONG";

    /**
     * <h2>{@code WebSocket} 路径</h2>
     */
    private String path = "/websocket";

    /**
     * <h2>{@code WebSocket} 支持方式</h2>
     */
    private WebSocketSupport support = WebSocketSupport.NO;

    /**
     * <h2>发布订阅的频道前缀</h2>
     */
    private String channelPrefix;

    /**
     * <h2>{@code WebSocket} 允许的跨域</h2>
     */
    private String allowedOrigins = "*";
}
