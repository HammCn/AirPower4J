package cn.hamm.airpower.validate.dictionary;

import cn.hamm.airpower.interfaces.IDictionary;
import cn.hamm.airpower.util.DictionaryUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * <h1>枚举字典验证实现类</h1>
 *
 * @author Hamm.cn
 */
public class DictionaryAnnotationValidator implements ConstraintValidator<Dictionary, Integer> {
    Class<? extends IDictionary> enumClazz = null;

    @Override
    public final boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (null == value) {
            return true;
        }
        return Objects.nonNull(DictionaryUtil.getDictionaryByKey(enumClazz, value));
    }

    @Override
    public final void initialize(@NotNull Dictionary dictionary) {
        enumClazz = dictionary.value();
    }
}