package cn.hamm.airpower.interceptor;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * <h1>全局拦截响应</h1>
 *
 * @author Hamm
 */
@ControllerAdvice
public class ResponseBodyInterceptor implements ResponseBodyAdvice<Object> {
    @Override
    public final boolean supports(
            @NotNull MethodParameter returnType,
            @NotNull Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return true;
    }

    @Override
    public final Object beforeBodyWrite(
            Object body,
            @NotNull MethodParameter returnType,
            @NotNull MediaType selectedContentType,
            @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NotNull ServerHttpRequest request,
            @NotNull ServerHttpResponse response
    ) {
        return beforeResponseFinished(body, request, response);
    }

    /**
     * <h2>响应结束前置方法</h2>
     *
     * @param body 响应体
     * @return 响应体
     * @apiNote 如无其他操作，请直接返回<code>body</code>参数即可
     */
    protected Object beforeResponseFinished(Object body, ServerHttpRequest request, ServerHttpResponse response) {
        return body;
    }

    /**
     * <h2>获取共享数据</h2>
     *
     * @param key KEY
     * @return VALUE
     */
    protected final Object getShareData(String key) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return null;
        }
        return requestAttributes.getAttribute(key, RequestAttributes.SCOPE_REQUEST);
    }
}
