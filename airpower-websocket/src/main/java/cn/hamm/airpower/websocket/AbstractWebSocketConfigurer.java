package cn.hamm.airpower.websocket;

import cn.hamm.airpower.core.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Objects;

/**
 * <h1>WebSocket配置</h1>
 *
 * @author Hamm.cn
 */
@Configuration
public abstract class AbstractWebSocketConfigurer implements WebSocketConfigurer {
    @Autowired
    protected WebSocketConfig webSocketConfig;

    /**
     * <h3>获取一个 {@code WebSocketHandler}</h3>
     *
     * @return {@code WebSocketHandler}
     */
    @Bean
    protected abstract WebSocketHandler getWebsocketHandler();

    /**
     * <h3>添加 {@code WebSocket} 服务监听</h3>
     *
     * @param registry {@code WebSocketHandlerRegistry}
     */
    @Override
    public void registerWebSocketHandlers(@NotNull WebSocketHandlerRegistry registry) {
        if (Objects.equals(webSocketConfig.getSupport(), WebSocketSupport.NO)) {
            return;
        }
        final String channelPrefix = webSocketConfig.getChannelPrefix();
        if (!StringUtils.hasText(channelPrefix)) {
            throw new ServiceException("没有配置 airpower.websocket.channelPrefix, 无法启动WebSocket服务");
        }
        registry.addHandler(getWebsocketHandler(), webSocketConfig.getPath())
                .setAllowedOrigins(webSocketConfig.getAllowedOrigins());
    }
}
