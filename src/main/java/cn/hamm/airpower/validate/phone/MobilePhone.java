package cn.hamm.airpower.validate.phone;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>标记手机号验证</h1>
 * 请注意，请自行做非空验证
 *
 * @author Hamm
 */
@Constraint(validatedBy = MobilePhoneAnnotationValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MobilePhone {
    /**
     * <h1>错误信息</h1>
     */
    String message() default "不是有效的手机号码";

    /**
     * <h1>验证组</h1>
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}