package cn.hamm.airpower.interceptor.cache;

import cn.hamm.airpower.util.Utils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = ((HttpServletRequest) servletRequest);
        try {
            // 如果是上传 不做任何缓存
            if (!requestCacheRequired(request)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            RequestBodyCacheWrapper wrapper = new RequestBodyCacheWrapper(request);
            filterChain.doFilter(wrapper, servletResponse);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    /**
     * 判断是否需要缓存
     *
     * @param request 请求
     * @return 是否需要缓存
     */
    private boolean requestCacheRequired(@NotNull HttpServletRequest request) {
        // GET请求不缓存
        if (HttpMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
            return false;
        }

        // 空ContentType或者非JSON不缓存
        String contentType = request.getContentType();
        if (Objects.isNull(contentType) || !contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return false;
        }
        // 上传请求不缓存
        return !Utils.getRequestUtil().isUploadRequest(request);
    }
}
