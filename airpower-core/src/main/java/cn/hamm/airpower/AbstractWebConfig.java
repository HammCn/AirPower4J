package cn.hamm.airpower;

import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.config.WebSocketConfig;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.interceptor.AbstractRequestInterceptor;
import cn.hamm.airpower.interceptor.cache.RequestCacheFilter;
import cn.hamm.airpower.resolver.AccessResolver;
import cn.hamm.airpower.websocket.WebSocketHandler;
import cn.hamm.airpower.websocket.WebSocketSupport;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.List;
import java.util.Objects;

/**
 * <h1>全局配置</h1>
 *
 * @author Hamm.cn
 */
@Configuration
public abstract class AbstractWebConfig implements WebMvcConfigurer, WebSocketConfigurer {
    @Autowired
    private AccessResolver accessResolver;

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Autowired
    private WebSocketHandler webSocketHandler;

    /**
     * <h2>获取一个拦截器实例</h2>
     *
     * @return 拦截器实例
     */
    @Bean
    public abstract AbstractRequestInterceptor getAccessInterceptor();

    /**
     * <h2>获取一个 <code>WebSocketHandler</code></h2>
     *
     * @return <code>WebSocketHandler</code>
     */
    @Bean
    public WebSocketHandler getWebsocketHandler() {
        return webSocketHandler;
    }

    /**
     * <h2>添加拦截器</h2>
     *
     * @param registry 拦截器管理器
     */
    @Override
    public final void addInterceptors(@NotNull InterceptorRegistry registry) {
        //添加身份校验拦截器
        registry.addInterceptor(getAccessInterceptor())
                .addPathPatterns("/**");
        addCustomInterceptors(registry);
    }

    /**
     * <h2>添加参数解析器</h2>
     *
     * @param resolvers 参数解析器
     */
    @Override
    public final void addArgumentResolvers(@NotNull List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(accessResolver);
    }

    /**
     * <h2>添加自定义拦截器</h2>
     *
     * @param registry 拦截器管理器
     */
    @SuppressWarnings({"EmptyMethod", "unused"})
    public void addCustomInterceptors(InterceptorRegistry registry) {
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
     * <h2>添加 <code>WebSocket</code> 服务监听</h2>
     *
     * @param registry <code>WebSocketHandlerRegistry</code>
     */
    @Override
    public final void registerWebSocketHandlers(@NotNull WebSocketHandlerRegistry registry) {
        if (Configs.getWebsocketConfig().getSupport().equals(WebSocketSupport.NO)) {
            return;
        }
        final String channelPrefix = Configs.getWebsocketConfig().getChannelPrefix();
        if (Objects.isNull(channelPrefix) || !StringUtils.hasText(channelPrefix)) {
            throw new ServiceException("没有配置 airpower.websocket.channelPrefix, 无法启动WebSocket服务");
        }
        registry.addHandler(getWebsocketHandler(), Configs.getWebsocketConfig().getPath())
                .setAllowedOrigins(webSocketConfig.getAllowedOrigins());
    }
}