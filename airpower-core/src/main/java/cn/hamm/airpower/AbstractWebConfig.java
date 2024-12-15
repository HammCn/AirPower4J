package cn.hamm.airpower;

import cn.hamm.airpower.config.WebSocketConfig;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.interceptor.cache.RequestCacheFilter;
import cn.hamm.airpower.websocket.WebSocketHandler;
import cn.hamm.airpower.websocket.WebSocketSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Objects;

/**
 * <h1>全局配置</h1>
 *
 * @author Hamm.cn
 */
@Configuration
public abstract class AbstractWebConfig implements WebMvcConfigurer, WebSocketConfigurer {
    @Autowired
    protected WebSocketConfig webSocketConfig;

    @Autowired
    protected WebSocketHandler webSocketHandler;

    /**
     * <h2>获取一个 {@code WebSocketHandler}</h2>
     *
     * @return {@code WebSocketHandler}
     */
    @Bean
    public WebSocketHandler getWebsocketHandler() {
        return webSocketHandler;
    }

    /**
     * <h2>添加缓存过滤器</h2>
     *
     * @return 过滤器对象
     */
    @Bean
    public FilterRegistrationBean<RequestCacheFilter> bodyCachingFilterRegistration() {
        FilterRegistrationBean<RequestCacheFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestCacheFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    /**
     * <h2>添加 {@code WebSocket} 服务监听</h2>
     *
     * @param registry {@code WebSocketHandlerRegistry}
     */
    @Override
    public final void registerWebSocketHandlers(@NotNull WebSocketHandlerRegistry registry) {
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
