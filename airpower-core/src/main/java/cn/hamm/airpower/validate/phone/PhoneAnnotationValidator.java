package cn.hamm.airpower.validate.phone;

import cn.hamm.airpower.util.ValidateUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <h1>电话验证实现类</h1>
 *
 * @author Hamm.cn
 */
@Component
public class PhoneAnnotationValidator implements ConstraintValidator<Phone, String> {
    /**
     * <h2>是否座机</h2>
     */
    private boolean tel = true;

    /**
     * <h2>是否手机号</h2>
     */
    private boolean mobile = true;

    /**
     * <h2>验证</h2>
     *
     * @param value   验证的值
     * @param context 验证会话
     * @return 验证结果
     */
    @Override
    public final boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasLength(value)) {
            return true;
        }
        if (!mobile && !tel) {
            // 不允许座机也不允许手机 验证个鬼啊
            return true;
        }
        if (!mobile) {
            // 只允许座机
            return ValidateUtil.isTelPhone(value);
        }
        if (!tel) {
            // 只允许手机
            return ValidateUtil.isMobilePhone(value);
        }
        // 手机座机均可
        return ValidateUtil.isMobilePhone(value) || ValidateUtil.isTelPhone(value);
    }

    /**
     * <h2>初始化</h2>
     *
     * @param phone 电话验证注解
     */
    @Override
    public final void initialize(@NotNull Phone phone) {
        mobile = phone.mobile();
        tel = phone.tel();
    }
}
