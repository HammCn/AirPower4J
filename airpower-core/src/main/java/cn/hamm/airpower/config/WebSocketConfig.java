package cn.hamm.airpower.config;

import cn.hamm.airpower.websocket.WebSocketSupport;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static cn.hamm.airpower.websocket.WebSocketSupport.NO;

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
     * <h3>{@code PING}</h3>
     */
    private String ping = "PING";

    /**
     * <h3>{@code PONG}</h3>
     */
    private String pong = "PONG";

    /**
     * <h3>{@code WebSocket} 路径</h3>
     */
    private String path = "/websocket";

    /**
     * <h3>{@code WebSocket} 支持方式</h3>
     */
    private WebSocketSupport support = NO;

    /**
     * <h3>发布订阅的频道前缀</h3>
     */
    private String channelPrefix;

    /**
     * <h3>{@code WebSocket} 允许的跨域</h3>
     */
    private String allowedOrigins = "*";
}
