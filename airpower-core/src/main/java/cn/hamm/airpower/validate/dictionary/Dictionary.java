package cn.hamm.airpower.validate.dictionary;

import cn.hamm.airpower.interfaces.IDictionary;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <h1>标记进行字典校验</h1>
 *
 * @author Hamm.cn
 * @apiNote 请注意, 请自行做非空验证, 字典必须实现 {@link IDictionary} 接口
 */
@Constraint(validatedBy = DictionaryAnnotationValidator.class)
@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Documented
public @interface Dictionary {
    /**
     * <h3>错误信息</h3>
     */
    String message() default "不允许的枚举字典值";

    /**
     * <h3>使用的枚举类</h3>
     *
     * @see IDictionary
     */
    Class<? extends IDictionary> value();

    /**
     * <h3>验证组</h3>
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}