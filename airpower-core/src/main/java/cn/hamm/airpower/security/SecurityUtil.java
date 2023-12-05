package cn.hamm.airpower.security;

import cn.hamm.airpower.config.GlobalConfig;
import cn.hamm.airpower.result.Result;
import cn.hamm.airpower.util.redis.RedisUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <h1>安全助手类</h1>
 *
 * @author Hamm
 */
@Component
public class SecurityUtil {
    private final String ACCESS_TOKEN_PREFIX = "access_token_";
    @Autowired
    private RedisUtil<?> redisUtil;

    /**
     * 从AccessToken中获取用户ID
     *
     * @param accessToken accessToken
     */
    public Long getUserIdFromAccessToken(String accessToken) {
        Object data = redisUtil.get(ACCESS_TOKEN_PREFIX + accessToken);
        if (Objects.nonNull(data)) {
            return Long.valueOf(data.toString());
        }
        Result.UNAUTHORIZED.show("获取身份信息失败,请重新登录!");
        return null;
    }

    /**
     * 创建一个AccessToken
     *
     * @param userId 用户ID
     * @return AccessToken
     */
    public String createAccessToken(Long userId) {
        int accessTokenLength = 32;
        String accessToken = RandomUtil.randomString(accessTokenLength);
        try {
            getUserIdFromAccessToken(accessToken);
            return createAccessToken(userId);
        } catch (Exception e) {
            // 不存在 存储
            redisUtil.set(ACCESS_TOKEN_PREFIX + accessToken, userId, GlobalConfig.authorizeExpTime);
            return accessToken;
        }
    }
}
