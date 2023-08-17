package cn.hamm.airpower.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSocket Config
 *
 * @author hamm
 */
@Configuration
public class WebsocketConfig implements WebSocketConfigurer {
    /**
     * Websocket ping消息
     */
    static String ping = "ping";

    /**
     * Websocket pong消息
     */
    static String pong = "pong";

    /**
     * Websocket 路径
     */
    static String path = "/websocket";

    @Bean
    public TextWebSocketHandler getWebSocketHandler() {
        return new WebsocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getWebSocketHandler(), WebsocketConfig.path).setAllowedOrigins("*");
    }
}
