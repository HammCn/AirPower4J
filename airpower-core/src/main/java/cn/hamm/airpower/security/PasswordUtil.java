package cn.hamm.airpower.security;

import cn.hamm.airpower.result.Result;
import cn.hutool.crypto.SecureUtil;

/**
 * <h1>密码助手类</h1>
 *
 * @author Hamm
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
        return SecureUtil.sha1(SecureUtil.sha1(password + salt) + SecureUtil.sha1(salt + password));
    }
}
