package cn.hamm.airpower.validate.phone;

import cn.hamm.airpower.util.Utils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

/**
 * <h1>电话验证实现类</h1>
 *
 * @author Hamm.cn
 */
public class PhoneAnnotationValidator implements ConstraintValidator<Phone, String> {
    /**
     * <h2>是否座机</h2>
     */
    private boolean tel = true;

    /**
     * <h2>是否手机号</h2>
     */
    private boolean mobile = true;

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
            return Utils.getValidateUtil().isTelPhone(value);
        }
        if (!tel) {
            // 只允许手机
            return Utils.getValidateUtil().isMobilePhone(value);
        }
        // 手机座机均可
        return Utils.getValidateUtil().isMobilePhone(value) || Utils.getValidateUtil().isTelPhone(value);
    }

    @Override
    public final void initialize(@NotNull Phone constraintAnnotation) {
        mobile = constraintAnnotation.mobile();
        tel = constraintAnnotation.tel();
    }
}