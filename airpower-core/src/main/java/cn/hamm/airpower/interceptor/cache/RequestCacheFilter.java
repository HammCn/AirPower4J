package cn.hamm.airpower.interceptor.cache;

import cn.hamm.airpower.util.AirUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <h1>缓存请求的过滤器</h1>
 *
 * @author Hamm.cn
 */
@Component
@WebFilter
@Slf4j
public class RequestCacheFilter implements Filter {
    @Override
    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = ((HttpServletRequest) servletRequest);
        // 如果是GET请求 不做任何缓存
        if (HttpMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (!request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        try {
            // 如果是上传 不做任何缓存
            if (AirUtil.getRequestUtil().isUploadRequest(servletRequest)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            RequestBodyCacheWrapper wrapper = new RequestBodyCacheWrapper(httpServletRequest);
            filterChain.doFilter(wrapper, servletResponse);
        } catch (Exception exception) {
            log.error("缓存请求发生异常", exception);
        }
    }
}
