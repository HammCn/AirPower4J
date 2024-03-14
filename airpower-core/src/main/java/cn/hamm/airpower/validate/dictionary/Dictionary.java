package cn.hamm.airpower.validate.dictionary;

import cn.hamm.airpower.interfaces.IDictionary;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>标记进行字典校验</h1>
 *
 * @author Hamm
 * @apiNote 请注意, 请自行做非空验证, 字典必须实现 {@link IDictionary} 接口
 */
@Constraint(validatedBy = DictionaryAnnotationValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dictionary {
    /**
     * 错误信息
     */
    String message() default "不允许的枚举字典值";

    /**
     * 使用的枚举类
     *
     * @see IDictionary
     */
    Class<? extends IDictionary> value();

    /**
     * 验证组
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}