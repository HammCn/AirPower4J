package cn.hamm.airpower.web.model;

import cn.hamm.airpower.core.exception.ServiceException;
import cn.hamm.airpower.core.security.AccessTokenUtil;
import cn.hamm.airpower.web.annotation.Permission;
import cn.hamm.airpower.web.config.WebConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static cn.hamm.airpower.core.exception.ServiceError.UNAUTHORIZED;

/**
 * <h1>控制器根类</h1>
 *
 * @author Hamm.cn
 */
@Permission(login = false)
@Slf4j
public class RootController {
    @Autowired
    protected WebConfig webConfig;

    @Autowired
    protected HttpServletRequest request;

    /**
     * <h3>获取当前登录用户的信息</h3>
     *
     * @return 用户 {@code ID}
     */
    protected final long getCurrentUserId() {
        try {
            String accessToken = request.getHeader(webConfig.getAuthorizeHeader());
            AccessTokenUtil.VerifiedToken verifiedToken = AccessTokenUtil.create().verify(accessToken, webConfig.getAccessTokenSecret());
            return verifiedToken.getPayloadId();
        } catch (Exception exception) {
            throw new ServiceException(UNAUTHORIZED);
        }
    }
}
