package cn.hamm.airpower;

import cn.hamm.airpower.interceptor.AbstractRequestInterceptor;
import cn.hamm.airpower.interceptor.cache.RequestCacheFilter;
import cn.hamm.airpower.resolver.AccessResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <h1>全局配置</h1>
 *
 * @author Hamm.cn
 */
@Configuration
public abstract class AbstractWebConfig implements WebMvcConfigurer {
    @Autowired
    private AccessResolver accessResolver;

    /**
     * <h2>获取一个拦截器实例</h2>
     *
     * @return 拦截器实例
     */
    @Bean
    public abstract AbstractRequestInterceptor getAccessInterceptor();

    @Override
    public final void addInterceptors(@NotNull InterceptorRegistry registry) {
        //添加身份校验拦截器
        registry.addInterceptor(getAccessInterceptor()).addPathPatterns("/**");
        addCustomInterceptors(registry);

    }

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

    @Bean
    public FilterRegistrationBean<RequestCacheFilter> bodyCachingFilterRegistration() {
        FilterRegistrationBean<RequestCacheFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestCacheFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }
}