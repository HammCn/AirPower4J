package cn.hamm.airpower.util;

import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.exception.ServiceError;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.model.Json;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <h1>{@code AccessToken} 工具类</h1>
 *
 * @author Hamm.cn
 */
@Slf4j
public class AccessTokenUtil {
    /**
     * <h2>无效的令牌</h2>
     */
    public static final String ACCESS_TOKEN_INVALID = "身份令牌无效，请重新获取身份令牌";

    /**
     * <h2>算法</h2>
     */
    private static final String HMAC_SHA_256 = "HmacSHA256";

    /**
     * <h2>缺少负载</h2>
     */
    private static final String PAYLOADS_IS_EMPTY = "没有任何负载数据";

    /**
     * <h2>{@code HMAC-SHA-256}错误</h2>
     */
    private static final String HMAC_SHA_256_ERROR = "HMAC-SHA-256发生错误";

    /**
     * <h2>{@code Token} 由 {@code 3} 部分组成</h2>
     */
    private static final int TOKEN_PART_COUNT = 3;

    /**
     * <h2>验证后的 {@code Token}</h2>
     */
    private VerifiedToken verifiedToken;

    /**
     * <h2>禁止外部实例化</h2>
     */
    @Contract(pure = true)
    private AccessTokenUtil() {
    }

    /**
     * <h2>创建实例</h2>
     *
     * @return {@code AccessTokenUtil}
     */
    public static @NotNull AccessTokenUtil create() {
        AccessTokenUtil accessTokenUtil = new AccessTokenUtil();
        accessTokenUtil.verifiedToken = new VerifiedToken();
        return accessTokenUtil;
    }

    /**
     * <h2>创建一个 {@code AccessToken}</h2>
     *
     * @param id           {@code TokenID}
     * @param expireSecond 有效期（秒）
     * @return {@code AccessTokenUtil}
     */
    public AccessTokenUtil setPayloadId(Long id, long expireSecond) {
        return addPayload(Constant.ID, id)
                .setExpireMillisecond(expireSecond * Constant.MILLISECONDS_PER_SECOND);
    }

    /**
     * <h2>从 {@code AccessToken} 中获取 {@code ID}</h2>
     *
     * @param accessToken {@code AccessToken}
     * @param secret      {@code AccessToken} 密钥
     * @return {@code ID}
     */
    public final long getPayloadId(String accessToken, String secret) {
        ServiceError.UNAUTHORIZED.whenNull(accessToken);
        Object userId = verify(accessToken, secret).getPayload(Constant.ID);
        ServiceError.UNAUTHORIZED.whenNull(userId);
        return Long.parseLong(userId.toString());
    }

    /**
     * <h2>生成 {@code Token}</h2>
     *
     * @param secret 密钥
     * @return {@code AccessToken}
     */
    public final String build(String secret) {
        ServiceError.PARAM_INVALID.whenEquals(Constant.AIRPOWER, secret,
                "身份令牌创建失败，请在环境变量配置 airpower.accessTokenSecret");
        if (verifiedToken.getPayloads().isEmpty()) {
            throw new ServiceException(PAYLOADS_IS_EMPTY);
        }
        String payloadBase = Base64.getUrlEncoder().encodeToString(
                Json.toString(verifiedToken.getPayloads()).getBytes(StandardCharsets.UTF_8)
        );
        String content = verifiedToken.getExpireTimestamps() +
                Constant.DOT +
                hmacSha256(secret, verifiedToken.getExpireTimestamps() + Constant.DOT + payloadBase) +
                Constant.DOT +
                payloadBase;
        return Base64.getUrlEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * <h2>添加负载</h2>
     *
     * @param key   负载的 {@code Key}
     * @param value 负载的 {@code Value}
     * @return {@code AccessTokenUtil}
     */
    @Contract("_, _ -> this")
    public final AccessTokenUtil addPayload(String key, Object value) {
        verifiedToken.getPayloads().put(key, value);
        return this;
    }

    /**
     * <h2>移除负载</h2>
     *
     * @param key 负载 {@code Key}
     * @return {@code AccessTokenUtil}
     */
    @Contract("_ -> this")
    public final AccessTokenUtil removePayload(String key) {
        verifiedToken.getPayloads().remove(key);
        return this;
    }

    /**
     * <h2>设置过期时间 {@code 毫秒}</h2>
     *
     * @param millisecond 过期毫秒
     * @return {@code AccessTokenUtil}
     */
    @Contract("_ -> this")
    public final AccessTokenUtil setExpireMillisecond(long millisecond) {
        if (millisecond <= 0) {
            throw new ServiceException(ServiceError.PARAM_INVALID, "过期毫秒数必须大于0");
        }
        verifiedToken.setExpireTimestamps(System.currentTimeMillis() + millisecond);
        return this;
    }

    /**
     * <h2>验证 {@code AccessToken} 并返回 {@code VerifiedToken}</h2>
     *
     * @param accessToken {@code AccessToken}
     * @param secret      密钥
     * @return {@code VerifiedToken}
     */
    public final VerifiedToken verify(@NotNull String accessToken, String secret) {
        String source;
        try {
            source = new String(Base64.getUrlDecoder().decode(accessToken.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new ServiceException(ServiceError.UNAUTHORIZED, ACCESS_TOKEN_INVALID);
        }
        if (!StringUtils.hasText(source)) {
            throw new ServiceException(ServiceError.UNAUTHORIZED, ACCESS_TOKEN_INVALID);
        }
        String[] list = source.split(Constant.DOT_REGEX);
        if (list.length != TOKEN_PART_COUNT) {
            throw new ServiceException(ServiceError.UNAUTHORIZED);
        }
        //noinspection AlibabaUndefineMagicConstant
        if (!Objects.equals(hmacSha256(secret, list[0] + Constant.DOT + list[2]), list[1])) {
            throw new ServiceException(ServiceError.UNAUTHORIZED, ACCESS_TOKEN_INVALID);
        }
        if (Long.parseLong(list[0]) < System.currentTimeMillis() && Long.parseLong(list[0]) != 0) {
            throw new ServiceException(ServiceError.UNAUTHORIZED, ACCESS_TOKEN_INVALID);
        }
        Map<String, Object> payloads = Json.parse2Map(new String(
                Base64.getUrlDecoder().decode(list[2].getBytes(StandardCharsets.UTF_8)))
        );
        return new VerifiedToken().setExpireTimestamps(Long.parseLong(list[0])).setPayloads(payloads);
    }

    /**
     * <h2>{@code HMacSha256}</h2>
     *
     * @param secret  密钥
     * @param content 数据
     * @return 签名
     */
    private @NotNull String hmacSha256(@NotNull String secret, @NotNull String content) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA_256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA_256);
            mac.init(secretKeySpec);
            StringBuilder hexString = new StringBuilder();
            for (byte b : mac.doFinal(content.getBytes(StandardCharsets.UTF_8))) {
                hexString.append(String.format("%02x", b & 0xff));
            }
            return hexString.toString();
        } catch (Exception exception) {
            log.error("hmacSha256 error", exception);
            throw new ServiceException(HMAC_SHA_256_ERROR);
        }
    }

    /**
     * <h2>已验证的身份令牌</h2>
     *
     * @author Hamm.cn
     */
    @Data
    @Accessors(chain = true)
    public static class VerifiedToken {
        /**
         * <h2>负载数据</h2>
         */
        private Map<String, Object> payloads = new HashMap<>();

        /**
         * <h2>过期时间 {@code 毫秒}</h2>
         */
        private long expireTimestamps = 0;

        /**
         * <h2>获取负载</h2>
         *
         * @param key 负载的 {@code Key}
         * @return 负载的 {@code Value}
         */
        public final @Nullable Object getPayload(String key) {
            return payloads.get(key);
        }
    }
}
