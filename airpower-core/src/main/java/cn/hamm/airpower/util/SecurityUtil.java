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
     * <h2>从 <code>AccessToken</code> 中获取 <code>ID</code></h2>
     *
     * @param accessToken <code>AccessToken</code>
     */
    public final long getIdFromAccessToken(String accessToken) {
        ServiceError.UNAUTHORIZED.whenNull(accessToken);
        Object userId = Utils.getTokenUtil()
                .verify(accessToken, Configs.getServiceConfig().getAccessTokenSecret())
                .getPayload(Constant.ID);
        ServiceError.UNAUTHORIZED.whenNull(userId);
        return Long.parseLong(userId.toString());
    }

    /**
     * <h2>创建一个 <code>AccessToken</code></h2>
     *
     * @param id <code>TokenID</code>
     * @return <code>AccessToken</code>
     */
    public final String createAccessToken(Long id) {
        return Utils.getTokenUtil().addPayload(Constant.ID, id)
                .setExpireMillisecond(Configs.getServiceConfig().getAuthorizeExpireSecond() * 1000)
                .create(Configs.getServiceConfig().getAccessTokenSecret());
    }
}
