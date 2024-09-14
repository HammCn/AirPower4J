package cn.hamm.airpower.open;

import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.enums.ServiceError;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.model.Json;
import cn.hamm.airpower.root.RootModel;
import cn.hamm.airpower.util.RedisUtil;
import cn.hamm.airpower.util.Utils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * <h1>{@code OpenApi} 请求体</h1>
 *
 * @author Hamm.cn
 */
@Slf4j
@Setter
public class OpenRequest {
    /**
     * <h2>防重放缓存前缀</h2>
     */
    private static final String NONCE_CACHE_PREFIX = "NONCE_";

    /**
     * <h2>防重放时长</h2>
     */
    private static final int NONCE_CACHE_SECOND = 300;

    /**
     * <h2>{@code AppKey}</h2>
     */
    @NotBlank(message = "AppKey不能为空")
    @Getter
    private String appKey;

    /**
     * <h2>版本号</h2>
     */
    @NotNull(message = "版本号不能为空")
    private int version;

    /**
     * <h2>请求毫秒时间戳</h2>
     */
    @NotNull(message = "请求毫秒时间戳不能为空")
    private long timestamp;

    /**
     * <h2>加密后的业务数据</h2>
     */
    @NotBlank(message = "业务数据包体不能为空")
    private String content;

    /**
     * <h2>签名字符串</h2>
     */
    @NotBlank(message = "签名字符串不能为空")
    private String signature;

    /**
     * <h2>请求随机串</h2>
     */
    @NotBlank(message = "请求随机串不能为空")
    private String nonce;

    /**
     * <h2>当前请求的应用</h2>
     */
    @Getter
    private IOpenApp openApp;

    /**
     * <h2>强转请求数据到指定的类对象</h2>
     *
     * @param clazz 业务数据对象类型
     */
    public final <T extends RootModel> T parse(Class<T> clazz) {
        String json = decodeContent();
        try {
            return Json.parse(json, clazz);
        } catch (Exception e) {
            ServiceError.JSON_DECODE_FAIL.show();
            throw new ServiceException(e);
        }
    }

    /**
     * <h2>校验请求</h2>
     */
    final void check() {
        checkIpWhiteList();
        checkTimestamp();
        checkSignature();
        checkNonce();
    }

    /**
     * <h2>解密请求数据</h2>
     *
     * @return 请求数据
     */
    final String decodeContent() {
        String request = content;
        OpenArithmeticType appArithmeticType = Utils.getDictionaryUtil().getDictionary(
                OpenArithmeticType.class, openApp.getArithmetic()
        );
        try {
            switch (appArithmeticType) {
                case AES -> request = Utils.getAesUtil()
                        .setKey(openApp.getAppSecret())
                        .decrypt(request);
                case RSA -> request = Utils.getRsaUtil()
                        .setPrivateKey(openApp.getPrivateKey())
                        .privateKeyDecrypt(request);
                case NO -> {
                }
                default -> throw new ServiceException("解密失败，不支持的加密算法类型");
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            ServiceError.DECRYPT_DATA_FAIL.show();
        }
        return request;
    }

    /**
     * <h2>时间戳检测</h2>
     */
    private void checkTimestamp() {
        ServiceError.TIMESTAMP_INVALID.when(
                timestamp > System.currentTimeMillis() + NONCE_CACHE_SECOND * Constant.MILLISECONDS_PER_SECOND ||
                        timestamp < System.currentTimeMillis() - NONCE_CACHE_SECOND * Constant.MILLISECONDS_PER_SECOND
        );
    }

    /**
     * <h2>验证IP白名单</h2>
     */
    private void checkIpWhiteList() {
        String ipStr = openApp.getIpWhiteList();
        if (Objects.isNull(ipStr) || !StringUtils.hasText(ipStr)) {
            // 未配置IP白名单
            return;
        }
        String[] ipList = ipStr
                .replaceAll(Constant.SPACE, Constant.EMPTY_STRING)
                .split(Constant.LINE_BREAK);
        final String ip = Utils.getRequestUtil().getIpAddress(Utils.getRequest());
        if (!StringUtils.hasText(ip)) {
            ServiceError.MISSING_REQUEST_ADDRESS.show();
        }
        if (Arrays.stream(ipList).toList().contains(ip)) {
            return;
        }
        ServiceError.INVALID_REQUEST_ADDRESS.show();
    }

    /**
     * <h2>签名验证结果</h2>
     */
    private void checkSignature() {
        ServiceError.SIGNATURE_INVALID.whenNotEquals(signature, sign());
    }

    /**
     * <h2>防重放检测</h2>
     */
    private void checkNonce() {
        RedisUtil redisUtil = Utils.getRedisUtil();
        Object savedNonce = redisUtil.get(NONCE_CACHE_PREFIX + nonce);
        ServiceError.REPEAT_REQUEST.whenNotNull(savedNonce);
        redisUtil.set(NONCE_CACHE_PREFIX + nonce, 1, NONCE_CACHE_SECOND);
    }

    /**
     * <h2>签名</h2>
     *
     * @return 签名后的字符串
     */
    private @org.jetbrains.annotations.NotNull String sign() {
        return DigestUtils.sha1Hex(openApp.getAppSecret() + appKey + version + timestamp + nonce + content);
    }
}
