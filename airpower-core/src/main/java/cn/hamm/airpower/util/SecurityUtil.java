package cn.hamm.airpower.util;

import cn.hamm.airpower.config.AirConfig;
import cn.hamm.airpower.enums.SystemError;
import cn.hamm.airpower.exception.SystemException;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <h1>安全助手类</h1>
 *
 * @author Hamm.cn
 */
@Component
public class SecurityUtil {
    /**
     * <h2>AccessToken在Redis的存储前缀</h2>
     */
    private final String ACCESS_TOKEN_PREFIX = "access_token_";

    /**
     * <h2>从AccessToken中获取用户ID</h2>
     *
     * @param accessToken accessToken
     */
    public final long getUserIdFromAccessToken(String accessToken) {
        Object data = AirUtil.getRedisUtil().get(ACCESS_TOKEN_PREFIX + accessToken);
        if (Objects.nonNull(data)) {
            return Long.parseLong(data.toString());
        }
        throw new SystemException(SystemError.UNAUTHORIZED);
    }

    /**
     * <h2>创建一个AccessToken</h2>
     *
     * @param userId 用户ID
     * @return AccessToken
     */
    public final String createAccessToken(Long userId) {
        String accessToken = AirUtil.getRandomUtil().randomString();
        try {
            getUserIdFromAccessToken(accessToken);
            return createAccessToken(userId);
        } catch (Exception ignored) {
            // 不存在 存储
            AirUtil.getRedisUtil().set(
                    ACCESS_TOKEN_PREFIX + accessToken, userId,
                    AirConfig.getGlobalConfig().getAuthorizeExpireTime()
            );
            return accessToken;
        }
    }
}
