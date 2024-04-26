package cn.hamm.airpower.util;

import cn.hamm.airpower.config.MessageConstant;
import cn.hamm.airpower.enums.Result;
import cn.hamm.airpower.exception.ResultException;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * <h1>RSA加解密助手类</h1>
 *
 * @author Hamm.cn
 */
@Slf4j
@Accessors(chain = true)
@Component
public class RsaUtil {
    /**
     * <h2>加密算法KEY长度</h2>
     */
    private final int CRYPT_KEY_SIZE = 2048;
    /**
     * <h2>加密方式</h2>
     */
    private final String CRYPT_METHOD = "RSA";


    /**
     * <h2>公钥</h2>
     *
     * @apiNote openssl genrsa -out ca.key 2048 && openssl pkcs8 -topk8 -inform PEM -in ca.key -outform PEM -nocrypt -out ca.pem && openssl rsa -in ca.pem -pubout -out ca.crt
     */
    @Setter
    private String publicKey;

    /**
     * <h2>私钥</h2>
     */
    @Setter
    private String privateKey;

    /**
     * <h2>公钥加密</h2>
     *
     * @param sourceContent 原文
     * @return 密文
     */
    public final String publicKeyEncode(String sourceContent) {
        try {
            int blockSize = CRYPT_KEY_SIZE / 8 - 11;
            PublicKey publicKey = getPublicKey(this.publicKey);
            return encodeByKey(sourceContent, publicKey, blockSize);
        } catch (Exception exception) {
            log.error("公钥加密失败", exception);
            throw new ResultException(exception);
        }
    }

    /**
     * <h2>私钥解密</h2>
     *
     * @param encryptedContent 密文
     * @return 原文
     */
    public final @NotNull String privateKeyDecode(String encryptedContent) {
        try {
            int blockSize = CRYPT_KEY_SIZE / 8;
            PrivateKey privateKey = getPrivateKey(this.privateKey);
            return decodeByKey(encryptedContent, privateKey, blockSize);
        } catch (Exception exception) {
            log.error(MessageConstant.EXCEPTION_WHEN_RSA_CRYPTO, exception);
            throw new ResultException(exception);
        }
    }

    /**
     * <h2>私钥加密</h2>
     *
     * @param sourceContent 原文
     * @return 密文
     */
    public final String privateKeyEncode(String sourceContent) {
        try {
            int blockSize = CRYPT_KEY_SIZE / 8 - 11;
            PrivateKey privateKey = getPrivateKey(this.privateKey);
            return encodeByKey(sourceContent, privateKey, blockSize);
        } catch (Exception exception) {
            log.error("私钥加密失败", exception);
            throw new ResultException(exception);
        }
    }


    /**
     * <h2>公钥解密</h2>
     *
     * @param encryptedContent 密文
     * @return 原文
     */
    public final @NotNull String publicKeyDecode(String encryptedContent) {
        try {
            int blockSize = CRYPT_KEY_SIZE / 8;
            PublicKey publicKey = getPublicKey(this.publicKey);
            return decodeByKey(encryptedContent, publicKey, blockSize);
        } catch (Exception exception) {
            log.error("公钥解密失败", exception);
            throw new ResultException(exception);
        }
    }

    /**
     * <h2>公私钥解密</h2>
     *
     * @param encryptedContent 密文
     * @param key              公私钥
     * @param blockSize        分块大小
     * @return 明文
     */
    @Contract("_, _, _ -> new")
    private @NotNull String decodeByKey(String encryptedContent, Key key, int blockSize) throws Exception {
        byte[] srcBytes = Base64.getDecoder().decode(encryptedContent);
        Cipher deCipher;
        deCipher = Cipher.getInstance(CRYPT_METHOD);
        deCipher.init(Cipher.DECRYPT_MODE, key);
        byte[] resultBytes;
        resultBytes = rsaDoFinal(deCipher, srcBytes, blockSize);
        return new String(resultBytes);
    }

    /**
     * <h2>公私钥加密</h2>
     *
     * @param sourceContent 明文
     * @param key           公私钥
     * @param blockSize     区块大小
     * @return 密文
     */
    private String encodeByKey(@NotNull String sourceContent, Key key, int blockSize) throws Exception {
        byte[] srcBytes = sourceContent.getBytes();
        Cipher cipher;
        cipher = Cipher.getInstance(CRYPT_METHOD);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] resultBytes;
        resultBytes = rsaDoFinal(cipher, srcBytes, blockSize);
        return Base64.getEncoder().encodeToString(resultBytes);
    }

    /**
     * <h2>获取一个公钥</h2>
     *
     * @param publicKeyString 公钥字符串
     * @return 公钥
     * @throws Exception 异常
     */
    private PublicKey getPublicKey(String publicKeyString) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(CRYPT_METHOD);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    /**
     * <h2>获取一个私钥</h2>
     *
     * @param privateKeyString 私钥字符串
     * @return 私钥
     * @throws Exception 异常
     */
    private PrivateKey getPrivateKey(String privateKeyString) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(CRYPT_METHOD);
        PKCS8EncodedKeySpec private8KeySpec =
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
        return keyFactory.generatePrivate(private8KeySpec);
    }

    /**
     * <h2>RSA处理方法</h2>
     *
     * @param cipher      RSA实例
     * @param sourceBytes 加解密原始数据
     * @param blockSize   分片大小
     * @return 加解密结果
     * @throws Exception 加解密异常
     */
    private byte @NotNull [] rsaDoFinal(Cipher cipher, byte @NotNull [] sourceBytes, int blockSize) throws Exception {
        Result.ERROR.when(blockSize <= 0, MessageConstant.BLOCK_SIZE_MUST_BE_GREATER_THAN_ZERO);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int inputLength = sourceBytes.length;
        int currentOffSet = 0;
        byte[] cacheBytes;
        int index = 0;
        // 对数据分段解密
        while (inputLength - currentOffSet > 0) {
            cacheBytes = cipher.doFinal(sourceBytes, currentOffSet, Math.min(inputLength - currentOffSet, blockSize));
            byteArrayOutputStream.write(cacheBytes, 0, cacheBytes.length);
            index++;
            currentOffSet = index * blockSize;
        }
        byte[] data = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return data;
    }
}
