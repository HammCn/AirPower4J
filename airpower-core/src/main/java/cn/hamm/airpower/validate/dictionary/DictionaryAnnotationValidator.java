package cn.hamm.airpower.validate.dictionary;

import cn.hamm.airpower.interfaces.IDictionary;
import cn.hamm.airpower.util.DictionaryUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

/**
 * <h1>枚举字典验证实现类</h1>
 *
 * @author Hamm
 */
public class DictionaryAnnotationValidator implements ConstraintValidator<Dictionary, Integer> {
    Class<? extends IDictionary> enumClazz = null;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (null == value) {
            return true;
        }

        IDictionary dictionary = DictionaryUtil.getDictionaryByKey(enumClazz, value);
        return Objects.nonNull(dictionary);
    }

    @Override
    public void initialize(Dictionary dictionary) {
        enumClazz = dictionary.value();
    }
}