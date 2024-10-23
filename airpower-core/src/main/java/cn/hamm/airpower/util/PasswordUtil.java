package cn.hamm.airpower.util;

import cn.hamm.airpower.exception.ServiceError;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>密码工具类</h1>
 *
 * @author Hamm.cn
 */
public class PasswordUtil {

    /**
     * <h2>禁止外部实例化</h2>
     */
    @Contract(pure = true)
    private PasswordUtil() {
    }

    /**
     * <h2>密码和盐获取密码的散列摘要</h2>
     *
     * @param password 明文密码
     * @param salt     盐
     * @return {@code sha1} 散列摘要
     */
    public static @NotNull String encode(@NotNull String password, @NotNull String salt) {
        ServiceError.PARAM_MISSING.whenEmpty(password, "密码不能为空");
        ServiceError.PARAM_MISSING.whenEmpty(salt, "盐不能为空");
        return DigestUtils.sha1Hex(
                DigestUtils.sha1Hex(password + salt) + DigestUtils.sha1Hex(salt + password)
        );
    }
}
