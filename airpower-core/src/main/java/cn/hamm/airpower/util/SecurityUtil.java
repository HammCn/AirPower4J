package cn.hamm.airpower.util;

import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.config.ServiceConfig;
import cn.hamm.airpower.enums.ServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>安全助手类</h1>
 *
 * @author Hamm.cn
 */
@Component
public class SecurityUtil {
    @Autowired
    private ServiceConfig serviceConfig;

    /**
     * <h2>从 {@code AccessToken} 中获取 {@code ID}</h2>
     *
     * @param accessToken {@code AccessToken}
     */
    public final long getIdFromAccessToken(String accessToken) {
        ServiceError.UNAUTHORIZED.whenNull(accessToken);
        Object userId = Utils.getTokenUtil()
                .verify(accessToken, serviceConfig.getAccessTokenSecret())
                .getPayload(Constant.ID);
        ServiceError.UNAUTHORIZED.whenNull(userId);
        return Long.parseLong(userId.toString());
    }

    /**
     * <h2>创建一个 {@code AccessToken}</h2>
     *
     * @param id {@code TokenID}
     * @return {@code AccessToken}
     */
    public final String createAccessToken(Long id) {
        return Utils.getTokenUtil().addPayload(Constant.ID, id)
                .setExpireMillisecond(
                        serviceConfig.getAuthorizeExpireSecond() * Constant.MILLISECONDS_PER_SECOND
                )
                .create(serviceConfig.getAccessTokenSecret());
    }
}
