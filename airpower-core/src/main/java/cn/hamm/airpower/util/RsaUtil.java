package cn.hamm.airpower.util;

import cn.hamm.airpower.config.MessageConstant;
import cn.hamm.airpower.enums.ServiceError;
import cn.hamm.airpower.exception.ServiceException;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * <h1>{@code RSA} 加解密助手类</h1>
 *
 * @author Hamm.cn
 */
@Slf4j
@Accessors(chain = true)
@Component
public class RsaUtil {
    /**
     * <h2>加密算法 {@code KEY} 长度</h2>
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
    public final String publicKeyEncrypt(String sourceContent) {
        try {
            int blockSize = CRYPT_KEY_SIZE / 8 - 11;
            return encrypt(sourceContent, getPublicKey(publicKey), blockSize);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new ServiceException(exception);
        }
    }

    /**
     * <h2>私钥解密</h2>
     *
     * @param encryptedContent 密文
     * @return 原文
     */
    public final @NotNull String privateKeyDecrypt(String encryptedContent) {
        try {
            int blockSize = CRYPT_KEY_SIZE / 8;
            return decrypt(encryptedContent, getPrivateKey(privateKey), blockSize);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new ServiceException(exception);
        }
    }

    /**
     * <h2>私钥加密</h2>
     *
     * @param sourceContent 原文
     * @return 密文
     */
    public final String privateKeyEncrypt(String sourceContent) {
        try {
            int blockSize = CRYPT_KEY_SIZE / 8 - 11;
            return encrypt(sourceContent, getPrivateKey(privateKey), blockSize);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new ServiceException(exception);
        }
    }

    /**
     * <h2>公钥解密</h2>
     *
     * @param encryptedContent 密文
     * @return 原文
     */
    public final @NotNull String publicKeyDecrypt(String encryptedContent) {
        try {
            int blockSize = CRYPT_KEY_SIZE / 8;
            return decrypt(encryptedContent, getPublicKey(publicKey), blockSize);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new ServiceException(exception);
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
    private @NotNull String decrypt(String encryptedContent, Key key, int blockSize) throws Exception {
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
    private String encrypt(@NotNull String sourceContent, Key key, int blockSize) throws Exception {
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
     * <h2>{@code RSA} 处理方法</h2>
     *
     * @param cipher      {@code RSA} 实例
     * @param sourceBytes 加解密原始数据
     * @param blockSize   分片大小
     * @return 加解密结果
     * @throws Exception 加解密异常
     */
    private byte @NotNull [] rsaDoFinal(Cipher cipher, byte @NotNull [] sourceBytes, int blockSize) throws Exception {
        ServiceError.SERVICE_ERROR.when(blockSize <= 0, MessageConstant.BLOCK_SIZE_MUST_BE_GREATER_THAN_ZERO);
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

    /**
     * <h2>生成 {@code RSA} 密钥对</h2>
     *
     * @return {@code KeyPair}
     */
    public final KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CRYPT_METHOD);
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * <h2>将公钥转换为 {@code PEM} 格式</h2>
     *
     * @param publicKey 公钥
     * @return {@code PEM}
     */
    public final @NotNull String convertPublicKeyToPem(@NotNull PublicKey publicKey) {
        byte[] encoded = publicKey.getEncoded();
        String base64Encoded = Base64.getEncoder().encodeToString(encoded);
        return "-----BEGIN PUBLIC KEY-----\n" +
                wrapBase64Text(base64Encoded) +
                "-----END PUBLIC KEY-----";
    }

    /**
     * <h2>将私钥转换为 {@code PEM} 格式</h2>
     *
     * @param privateKey 私钥
     * @return {@code PEM}
     */
    public final @NotNull String convertPrivateKeyToPem(@NotNull PrivateKey privateKey) {
        byte[] encoded = privateKey.getEncoded();
        String base64Encoded = Base64.getEncoder().encodeToString(encoded);
        return "-----BEGIN RSA PRIVATE KEY-----\n" +
                wrapBase64Text(base64Encoded) +
                "-----END RSA PRIVATE KEY-----";
    }

    /**
     * <h2>将 {@code Base64} 编码的文本换行</h2>
     *
     * @param base64Text 原始 {@code Base64}
     * @return 换行后的
     */
    private @NotNull String wrapBase64Text(@NotNull String base64Text) {
        final int wrapLength = 64;
        StringBuilder wrappedText = new StringBuilder();
        int start = 0;
        while (start < base64Text.length()) {
            int end = Math.min(start + wrapLength, base64Text.length());
            wrappedText.append(base64Text, start, end).append("\n");
            start = end;
        }
        return wrappedText.toString();
    }
}
