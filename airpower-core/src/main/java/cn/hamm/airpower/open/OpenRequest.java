package cn.hamm.airpower.open;

import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.enums.ServiceError;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.model.Json;
import cn.hamm.airpower.root.RootModel;
import cn.hamm.airpower.util.Utils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * <h1>OpenApi请求体</h1>
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
    private static final int NONCE_CACHE_SECOND = 60;

    /**
     * <h2>AppKey</h2>
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
    @NotNull(message = "业务数据包体不能为空")
    private String content;

    /**
     * <h2>签名字符串</h2>
     */
    @NotNull(message = "签名字符串不能为空")
    private String signature;

    /**
     * <h2>请求随机串</h2>
     */
    @NotNull(message = "请求随机串不能为空")
    private String nonce;

    /**
     * <h2>当前请求的应用</h2>
     */
    @Getter
    private IOpenApp openApp;

    /**
     * <h2>签名验证结果</h2>
     *
     * @apiNote 无需手动调用
     */
    public final void checkSignature() {
        ServiceError.SIGNATURE_INVALID.whenNotEquals(this.signature, this.sign());
        checkNonce();
        checkTimestamp();
    }

    /**
     * <h2>签名</h2>
     *
     * @return 签名后的字符串
     */
    public final @org.jetbrains.annotations.NotNull String sign() {
        return DigestUtils.sha1Hex(this.openApp.getAppSecret() + this.appKey + this.version + this.timestamp + this.nonce + this.content);
    }

    /**
     * <h2>强转请求数据到指定的类对象</h2>
     *
     * @param clazz 业务数据对象类型
     */
    public final <T extends RootModel<T>> T parse(Class<T> clazz) {
        try {
            return Json.parse(decodeContent(), clazz);
        } catch (Exception e) {
            ServiceError.JSON_DECODE_FAIL.show();
            throw new ServiceException(e);
        }
    }

    /**
     * <h2>解密请求数据</h2>
     *
     * @return 请求数据
     */
    public final String decodeContent() {
        String request = this.content;
        OpenArithmeticType appArithmeticType = Utils.getDictionaryUtil().getDictionary(
                OpenArithmeticType.class, this.openApp.getArithmetic()
        );
        try {
            switch (appArithmeticType) {
                case AES:
                    request = Utils.getAesUtil().setKey(this.openApp.getAppSecret()).decrypt(request);
                    break;
                case RSA:
                    request = Utils.getRsaUtil().setPrivateKey(openApp.getPrivateKey()).privateKeyDecrypt(request);
                    break;
                case NO:
                    break;
                default:
                    throw new ServiceException("解密失败，不支持的加密算法类型");
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
                this.timestamp > System.currentTimeMillis() + NONCE_CACHE_SECOND * Constant.MILLISECONDS_PER_SECOND ||
                        this.timestamp < System.currentTimeMillis() - NONCE_CACHE_SECOND * Constant.MILLISECONDS_PER_SECOND
        );
    }

    /**
     * <h2>防重放检测</h2>
     */
    private void checkNonce() {
        Object savedNonce = Utils.getRedisUtil().get(NONCE_CACHE_PREFIX + this.nonce);
        ServiceError.REPEAT_REQUEST.whenNotNull(savedNonce);
        Utils.getRedisUtil().set(NONCE_CACHE_PREFIX + this.nonce, 1, NONCE_CACHE_SECOND);
    }
}
