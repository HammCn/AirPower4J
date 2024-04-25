package cn.hamm.airpower.util;

import cn.hamm.airpower.config.GlobalConfig;
import cn.hamm.airpower.enums.Result;
import cn.hamm.airpower.exception.ResultException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RedisUtil<?> redisUtil;

    @Autowired
    private GlobalConfig globalConfig;

    /**
     * <h2>从AccessToken中获取用户ID</h2>
     *
     * @param accessToken accessToken
     */
    public final long getUserIdFromAccessToken(String accessToken) {
        Object data = redisUtil.get(ACCESS_TOKEN_PREFIX + accessToken);
        if (Objects.nonNull(data)) {
            return Long.parseLong(data.toString());
        }
        throw new ResultException(Result.UNAUTHORIZED);
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
            redisUtil.set(ACCESS_TOKEN_PREFIX + accessToken, userId, globalConfig.getAuthorizeExpireTime());
            return accessToken;
        }
    }
}
