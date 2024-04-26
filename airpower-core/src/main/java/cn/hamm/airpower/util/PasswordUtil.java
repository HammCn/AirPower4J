package cn.hamm.airpower.util;

import cn.hamm.airpower.config.MessageConstant;
import cn.hamm.airpower.enums.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * <h1>密码助手类</h1>
 *
 * @author Hamm.cn
 */
@Component
public class PasswordUtil {

    /**
     * <h2>密码和盐获取密码的散列摘要</h2>
     *
     * @param password 明文密码
     * @param salt     盐
     * @return sha1散列摘要
     */
    public @NotNull String encode(String password, String salt) {
        Result.PARAM_MISSING.whenEmpty(password, MessageConstant.PASSWORD_CAN_NOT_BE_NULL);
        Result.PARAM_MISSING.whenEmpty(salt, MessageConstant.PASSWORD_SALT_CAN_NOT_BE_NULL);
        return DigestUtils.sha1Hex(
                DigestUtils.sha1Hex(password + salt) +
                        DigestUtils.sha1Hex(salt + password)
        );
    }
}