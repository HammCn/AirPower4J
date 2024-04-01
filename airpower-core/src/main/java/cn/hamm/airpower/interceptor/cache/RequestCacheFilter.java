package cn.hamm.airpower.interceptor.cache;

import cn.hamm.airpower.request.RequestUtil;
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
        // 如果是上传 不做任何缓存
        if (RequestUtil.isUploadRequest(servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        RequestBodyCacheWrapper wrapper = new RequestBodyCacheWrapper(httpServletRequest);
        filterChain.doFilter(wrapper, servletResponse);
    }
}
