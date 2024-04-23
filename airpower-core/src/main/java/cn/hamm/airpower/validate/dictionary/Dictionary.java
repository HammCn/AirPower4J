package cn.hamm.airpower.validate.dictionary;

import cn.hamm.airpower.interfaces.IDictionary;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * <h1>标记进行字典校验</h1>
 *
 * @author Hamm.cn
 * @apiNote 请注意, 请自行做非空验证, 字典必须实现 {@link IDictionary} 接口
 */
@Constraint(validatedBy = DictionaryAnnotationValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dictionary {
    /**
     * <h2>错误信息</h2>
     */
    String message() default "不允许的枚举字典值";

    /**
     * <h2>使用的枚举类</h2>
     *
     * @see IDictionary
     */
    Class<? extends IDictionary> value();

    /**
     * <h2>验证组</h2>
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}