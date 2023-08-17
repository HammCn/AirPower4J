package cn.hamm.airpower.validate.password;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>标记进行密码规范校验</h1>
 * 请注意，请自行做非空验证
 *
 * @author Hamm
 */
@Constraint(validatedBy = PasswordAnnotationValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    /**
     * <h2>错误信息</h2>
     */
    String message() default "不是规范的密码格式";

    /**
     * <h2>验证组</h2>
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}