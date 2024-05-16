package cn.hamm.airpower.util;

import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.enums.ServiceError;
import org.springframework.stereotype.Component;

/**
 * <h1>安全助手类</h1>
 *
 * @author Hamm.cn
 */
@Component
public class SecurityUtil {
    /**
     * <h2>从AccessToken中获取ID</h2>
     *
     * @param accessToken accessToken
     */
    public final long getIdFromAccessToken(String accessToken) {
        Object userId = Utils.getTokenUtil()
                .verify(accessToken, Configs.getServiceConfig().getAccessTokenSecret())
                .getPayload(Constant.ID);
        ServiceError.UNAUTHORIZED.whenNull(userId);
        return Long.parseLong(userId.toString());
    }

    /**
     * <h2>创建一个AccessToken</h2>
     *
     * @param id TokenID
     * @return AccessToken
     */
    public final String createAccessToken(Long id) {
        return Utils.getTokenUtil().addPayload(Constant.ID, id)
                .setExpireMillisecond(Configs.getServiceConfig().getAuthorizeExpireSecond() * 1000)
                .create(Configs.getServiceConfig().getAccessTokenSecret());
    }
}
