package cn.hamm.airpower.interceptor.cache;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <h1>缓存请求的过滤器</h1>
 *
 * @author Hamm
 */
@Component
@WebFilter
public class RequestCacheFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        RequestBodyCacheWrapper wrapper = new RequestBodyCacheWrapper(httpServletRequest);

        filterChain.doFilter(wrapper, servletResponse);
    }
}
