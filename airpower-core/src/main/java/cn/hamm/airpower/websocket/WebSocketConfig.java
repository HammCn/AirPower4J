package cn.hamm.airpower.websocket;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * <h1>WebSocket配置</h1>
 *
 * @author Hamm
 */
@Component
@Data
@Accessors(chain = true)
@Configuration
@ConfigurationProperties("airpower.websocket")
public class WebSocketConfig implements WebSocketConfigurer {
    /**
     * <h2>PING</h2>
     */
    private String ping = "ping";

    /**
     * <h2>PONG</h2>
     */
    private String pong = "pong";

    /**
     * <h2>Websocket 路径</h2>
     */
    private String path = "/websocket";

    /**
     * <h2>WebSocket支持方式</h2>
     */
    private WebSocketSupport support = WebSocketSupport.NO;

    @Bean
    public TextWebSocketHandler getWebSocketHandler() {
        return new WebsocketHandler();
    }

    /**
     * <h2>添加WebSocket服务监听</h2>
     *
     * @param registry WebSocketHandlerRegistry
     */
    @Override
    public void registerWebSocketHandlers(@NotNull WebSocketHandlerRegistry registry) {
        if (!support.equals(WebSocketSupport.NO)) {
            registry.addHandler(getWebSocketHandler(), path).setAllowedOrigins("*");
        }
    }
}
