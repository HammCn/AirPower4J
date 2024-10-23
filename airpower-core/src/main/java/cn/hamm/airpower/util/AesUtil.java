package cn.hamm.airpower.util;

import cn.hamm.airpower.exception.ServiceException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * <h1>{@code AES} 工具类</h1>
 *
 * @author Hamm.cn
 */
public class AesUtil {
    /**
     * <h2>密钥长度</h2>
     */
    private static final int KEY_SIZE = 256;

    /**
     * <h2>AES</h2>
     */
    private static final String AES = "AES";

    /**
     * <h2>{@code AES/CBC/PKCS5Padding}</h2>
     */
    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";

    /**
     * <h2>密钥</h2>
     */
    private byte[] key;

    /**
     * <h2>偏移向量</h2>
     */
    private byte[] iv = "0000000000000000".getBytes(StandardCharsets.UTF_8);

    /**
     * <h2>算法</h2>
     */
    private String algorithm = AES_CBC_PKCS5_PADDING;


    /**
     * <h2>禁止外部实例化</h2>
     */
    private AesUtil() {

    }

    /**
     * <h2>创建实例</h2>
     *
     * @return 新实例
     */
    @Contract(" -> new")
    public static @NotNull AesUtil create() {
        return new AesUtil();
    }

    /**
     * <h2>获取随机密钥</h2>
     *
     * @return 随机密钥
     */
    @Contract(" -> new")
    public static @NotNull String getRandomKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            keyGen.init(KEY_SIZE, new SecureRandom());
            SecretKey aesKey = keyGen.generateKey();
            byte[] keyBytes = aesKey.getEncoded();
            return Base64.getEncoder().encodeToString(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * <h2>获取随机向量</h2>
     *
     * @return 随机向量
     */
    @Contract(" -> new")
    public static @NotNull String getRandomIv() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            keyGen.init(KEY_SIZE / 2, new SecureRandom());
            SecretKey aesKey = keyGen.generateKey();
            byte[] keyBytes = aesKey.getEncoded();
            return Base64.getEncoder().encodeToString(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * <h2>设置偏移向量</h2>
     *
     * @param iv 偏移向量
     * @return {@code AesUtil}
     */
    @Contract("_ -> this")
    public final AesUtil setIv(String iv) {
        this.iv = Base64.getDecoder().decode(iv);
        return this;
    }

    /**
     * <h2>设置算法</h2>
     *
     * @param algorithm 算法
     * @return {@code AesUtil}
     */
    @Contract(value = "_ -> this", mutates = "this")
    public final AesUtil setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    /**
     * <h2>设置密钥</h2>
     *
     * @param key 密钥
     * @return {@code AesUtil}
     */
    @Contract("_ -> this")
    public final AesUtil setKey(String key) {
        this.key = Base64.getDecoder().decode(key);
        return this;
    }

    /**
     * <h2>加密</h2>
     *
     * @param source 待加密的内容
     * @return 加密后的内容
     */
    public final String encrypt(String source) {
        try {
            return Base64.getEncoder().encodeToString(getCipher(Cipher.ENCRYPT_MODE)
                    .doFinal(source.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <h2>解密</h2>
     *
     * @param content 加密后的内容
     * @return 解密后的内容
     */
    @Contract("_ -> new")
    public final @NotNull String decrypt(String content) {
        try {
            return new String(getCipher(Cipher.DECRYPT_MODE)
                    .doFinal(Base64.getDecoder().decode(content)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <h2>获取 {@code Cipher}</h2>
     *
     * @param mode 模式
     * @return {@code Cipher}
     */
    private @NotNull Cipher getCipher(int mode) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(mode, secretKeySpec, ivParameterSpec);
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
