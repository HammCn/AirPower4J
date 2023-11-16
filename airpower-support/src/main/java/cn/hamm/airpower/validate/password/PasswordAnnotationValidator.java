package cn.hamm.airpower.validate.password;

import cn.hamm.airpower.config.GlobalConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>手机验证实现类</h1>
 *
 * @author Hamm
 */
public class PasswordAnnotationValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasLength(value)) {
            return true;
        }

        if (value.length() < GlobalConfig.passwordMinLength) {
            return false;
        }

        if (value.length() > GlobalConfig.passwordMaxLength) {
            return contain(value, ".*[a-zA-Z]+.*") &&
                    contain(value, ".*[0-9]+.*");
        }

        return contain(value, ".*[a-z]+.*") &&
                contain(value, ".*[A-Z]+.*") &&
                contain(value, ".*[0-9]+.*");
    }

    public boolean contain(String value, String regx) {
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}