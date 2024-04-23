package cn.hamm.airpower.security;

import cn.hamm.airpower.result.Result;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * <h1>密码助手类</h1>
 *
 * @author Hamm.cn
 */
public class PasswordUtil {
    /**
     * <h2>密码和盐获取密码的散列摘要</h2>
     *
     * @param password 明文密码
     * @param salt     盐
     * @return sha1散列摘要
     */
    public static String encode(String password, String salt) {
        Result.PARAM_MISSING.whenEmpty(password, "密码不能为空");
        Result.PARAM_MISSING.whenEmpty(salt, "盐不能为空");
        return DigestUtils.sha1Hex(DigestUtils.sha1Hex(password + salt) + DigestUtils.sha1Hex(salt + password));
    }
}
