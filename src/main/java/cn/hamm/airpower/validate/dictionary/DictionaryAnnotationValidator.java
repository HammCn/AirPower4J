package cn.hamm.airpower.validate.dictionary;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

/**
 * <h1>枚举字典验证实现类</h1>
 *
 * @author Hamm
 */
public class DictionaryAnnotationValidator implements ConstraintValidator<Dictionary, Integer> {

    Class<?> enumClazz = null;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (null == value) {
            return true;
        }
        boolean isValidated = false;
        try {
            Method getValue = enumClazz.getMethod("getValue");
            //取出所有枚举类型
            Object[] objs = enumClazz.getEnumConstants();
            for (Object obj : objs) {
                if(value.equals(getValue.invoke(obj))){
                    isValidated = true;
                    break;
                }
            }
        } catch (Exception ignored) {
        }
        return isValidated;
    }

    @Override
    public void initialize(Dictionary constraintAnnotation) {
        enumClazz = constraintAnnotation.value();
    }
}