package cn.hamm.airpower.web;

import cn.hamm.airpower.web.interceptor.cache.RequestCacheFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <h1>全局配置</h1>
 *
 * @author Hamm.cn
 */
@Configuration
public abstract class AbstractWebConfigurer implements WebMvcConfigurer {
    /**
     * <h3>添加缓存过滤器</h3>
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
}
