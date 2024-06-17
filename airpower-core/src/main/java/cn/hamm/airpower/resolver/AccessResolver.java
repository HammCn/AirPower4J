package cn.hamm.airpower.resolver;

import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.util.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

/**
 * <h1>用户授权处理类</h1>
 *
 * @author Hamm.cn
 */
@Component
public class AccessResolver implements HandlerMethodArgumentResolver {
    /**
     * <h2>支持的参数类型</h2>
     *
     * @param parameter 参数
     * @return 是否支持
     */
    @Override
    public final boolean supportsParameter(@NotNull MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == Long.class;
    }

    /**
     * <h2>ACCESS_TOKEN换用户ID</h2>
     */
    @Override
    public final @NotNull @Unmodifiable Object resolveArgument(
            @NotNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @NotNull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = Objects.nonNull(request) ?
                request.getHeader(Configs.getServiceConfig().getAuthorizeHeader()) :
                null;
        return Utils.getSecurityUtil().getIdFromAccessToken(accessToken);
    }
}
