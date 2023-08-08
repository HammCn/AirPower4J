package cn.hamm.airpower.validate.phone;

import cn.hamm.airpower.request.ValidateUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <h1>手机验证实现类</h1>
 * @author Hamm
 */
public class MobilePhoneAnnotationValidator implements ConstraintValidator<MobilePhone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasLength(value)) {
            return true;
        }
        return ValidateUtil.isMobilePhone(value);
    }

    @Override
    public void initialize(MobilePhone constraintAnnotation) {
        // 在这里进行初始化操作，例如读取注解中的参数值等
    }
}