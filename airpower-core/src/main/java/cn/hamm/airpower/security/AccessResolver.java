package cn.hamm.airpower.security;

import cn.hamm.airpower.config.GlobalConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/**
 * <h1>用户授权处理类</h1>
 *
 * @author Hamm
 */
@Component
public class AccessResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == Long.class;
    }

    /**
     * ACCESS_TOKEN换用户ID
     */
    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = null;
        if (request != null) {
            accessToken = request.getHeader(GlobalConfig.authorizeHeader);
        }
        return securityUtil.getUserIdFromAccessToken(accessToken);
    }
}
