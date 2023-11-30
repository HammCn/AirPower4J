package cn.hamm.airpower.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * <h1>WebSocket配置</h1>
 *
 * @author hamm
 */
@Configuration
public class WebsocketConfig implements WebSocketConfigurer {
    /**
     * <h2>Websocket ping消息</h2>
     */
    static String ping = "ping";

    /**
     * <h2>Websocket pong消息</h2>
     */
    static String pong = "pong";

    /**
     * <h2>Websocket 路径</h2>
     */
    static String path = "/websocket";

    /**
     * <h2>订阅全频道</h2>
     */
    static String channelAll = "WEBSOCKET_ALL";

    /**
     * <h2>订阅用户频道前缀</h2>
     */
    static String channelUserPrefix = "WEBSOCKET_USER_";

    @Bean
    public TextWebSocketHandler getWebSocketHandler() {
        return new WebsocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getWebSocketHandler(), WebsocketConfig.path).setAllowedOrigins("*");
    }
}
