package cn.hamm.airpower.security;

import cn.hamm.airpower.result.Result;
import cn.hamm.airpower.result.ResultException;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * <h1>RSA加解密助手类</h1>
 *
 * @author Hamm
 * @noinspection unused
 */
public class RsaHelper {
    /**
     * <h2>加密算法KEY长度</h2>
     */
    private final int CRYPT_KEY_SIZE = 2048;
    /**
     * <h2>加密方式</h2>
     */
    private final String CRYPT_METHOD = "RSA";
    /**
     * openssl genrsa -out ca.key 2048 && openssl pkcs8 -topk8 -inform PEM -in ca.key -outform PEM -nocrypt -out ca.pem && openssl rsa -in ca.pem -pubout -out ca.crt
     */
    String publicKey;
    String privateKey;

    /**
     * <h2>初始化RSA Helper</h2>
     *
     * @param publicKey  公钥字符串
     * @param privateKey 私钥字符串
     */
    public RsaHelper(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * <h2>私钥解密</h2>
     *
     * @param encryptedContent 密文
     * @return 原文
     */
    public String privateKeyDecode(String encryptedContent) {
        try {
            int blockSize = CRYPT_KEY_SIZE / 8;
            PrivateKey privateKey = getPrivateKey(this.privateKey);
            byte[] srcBytes = Base64.getDecoder().decode(encryptedContent);
            Cipher deCipher = Cipher.getInstance(CRYPT_METHOD);
            deCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] resultBytes;
            //分段加密
            resultBytes = rsaDoFinal(deCipher, srcBytes, blockSize);
            return new String(resultBytes);
        } catch (Exception e) {
            throw new ResultException(Result.ERROR);
        }
    }

    /**
     * <h2>私钥加密</h2>
     *
     * @param sourceContent 原文
     * @return 密文
     */
    public String privateKeyEncode(String sourceContent) {
        try {
            int blockSize = CRYPT_KEY_SIZE / 8 - 11;
            PrivateKey privateKey = getPrivateKey(this.privateKey);
            byte[] srcBytes = sourceContent.getBytes();
            Cipher deCipher = Cipher.getInstance(CRYPT_METHOD);
            deCipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] resultBytes;
            //分段加密
            resultBytes = rsaDoFinal(deCipher, srcBytes, blockSize);
            return Base64.getEncoder().encodeToString(resultBytes);
        } catch (Exception e) {
            throw new ResultException(Result.ERROR);
        }
    }

    /**
     * <h2>公钥加密</h2>
     *
     * @param sourceContent 原文
     * @return 密文
     */
    public String publicKeyEncode(String sourceContent) {
        try {
            int blockSize = CRYPT_KEY_SIZE / 8 - 11;
            PublicKey publicKey = getPublicKey(this.publicKey);
            byte[] srcBytes = sourceContent.getBytes();
            Cipher cipher = Cipher.getInstance(CRYPT_METHOD);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] resultBytes;
            //分段加密
            resultBytes = rsaDoFinal(cipher, srcBytes, blockSize);
            return Base64.getEncoder().encodeToString(resultBytes);
        } catch (Exception e) {
            throw new ResultException(Result.ERROR);
        }
    }

    /**
     * <h2>公钥解密</h2>
     *
     * @param encryptedContent 密文
     * @return 原文
     */
    public String publicKeyDecode(String encryptedContent) {
        try {
            int blockSize = CRYPT_KEY_SIZE / 8;
            PublicKey publicKey = getPublicKey(this.publicKey);
            byte[] srcBytes = Base64.getDecoder().decode(encryptedContent);
            Cipher cipher = Cipher.getInstance(CRYPT_METHOD);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] resultBytes;
            //分段加密
            resultBytes = rsaDoFinal(cipher, srcBytes, blockSize);
            return new String(resultBytes);
        } catch (Exception e) {
            throw new ResultException(Result.ERROR);
        }
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
    private byte[] rsaDoFinal(Cipher cipher, byte[] sourceBytes, int blockSize) throws Exception {
        if (blockSize <= 0) {
            throw new ResultException(Result.ERROR, "分段大小必须大于0");
        }
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
