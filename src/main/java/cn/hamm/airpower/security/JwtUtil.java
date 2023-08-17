package cn.hamm.airpower.security;

import cn.hamm.airpower.config.GlobalConfig;
import cn.hamm.airpower.result.Result;
import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;

/**
 * <h1>JWT助手类</h1>
 *
 * @author Hamm
 */
public class JwtUtil {
    public static final String KEY_OF_USER_ID = "userId";
    public static final String KEY_OF_APP_ID = "appId";
    public static final String KEY_OF_TIME = "time";

    /**
     * <h2>获取AccessToken</h2>
     *
     * @param userId       用户ID
     * @param userPassword 用户密码
     * @return AccessToken
     */
    public static String getAccessToken(String userId, String userPassword) {
        return getAccessToken(userId, userPassword, "");
    }

    /**
     * <h2>获取一个TOKEN</h2>
     *
     * @param userId       用户ID
     * @param userPassword 用户密码
     * @param appId        应用ID
     * @return TOKEN
     */
    public static String getAccessToken(String userId, String userPassword, String appId) {
        return JWT.create().setKey(userPassword.getBytes())
                .setPayload(KEY_OF_USER_ID, userId)
                .setPayload(KEY_OF_APP_ID, appId)
                .setPayload(KEY_OF_TIME, DateUtil.currentSeconds())
                .sign();
    }

    /**
     * <h2>校验用户AccessToken是否正确</h2>
     *
     * @param password    密码
     * @param accessToken AccessToken
     */
    public static void verify(String password, String accessToken) {
        if (!JWT.of(accessToken).setKey(password.getBytes()).verify()) {
            Result.UNAUTHORIZED.show();
        }
    }

    /**
     * <h2>从AccessToken中获取用户ID</h2>
     *
     * @param accessToken 身份令牌
     * @return 用户ID
     */
    public static Long getUserId(String accessToken) {
        Result.PARAM_MISSING.whenNull(accessToken, "必须在header中传入" + GlobalConfig.authorizeHeader);
        String userId = getPayload(accessToken, KEY_OF_USER_ID);
        return Long.valueOf(userId);
    }

    /**
     * <h2>获取参数</h2>
     *
     * @param accessToken AccessToken
     * @param payload     索引
     * @return 参数
     */
    public static String getPayload(String accessToken, String payload) {
        return JWT.of(accessToken).getPayload(payload).toString();
    }
}
