package cn.hamm.airpower.validate.phone;

import cn.hamm.airpower.request.ValidateUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * <h1>电话验证实现类</h1>
 * @author Hamm
 */
public class PhoneAnnotationValidator implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasLength(value)) {
            return true;
        }
        return ValidateUtil.isMobilePhone(value) || ValidateUtil.isTelPhone(value);
    }

    @Override
    public void initialize(Phone constraintAnnotation) {
        // 在这里进行初始化操作，例如读取注解中的参数值等
    }
}