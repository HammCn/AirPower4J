package cn.hamm.airpower.validate.dictionary;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>标记进行字典校验</h1>
 * 请注意，请自行做非空验证
 *
 * @author Hamm
 */
@Constraint(validatedBy = DictionaryAnnotationValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dictionary {
    /**
     * <h1>错误信息</h1>
     */
    String message() default "传入的字典值不在允许的范围内";

    /**
     * <h1>使用的枚举类</h1>
     */
    Class<?> value() default Void.class;

    /**
     * <h1>验证组</h1>
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}