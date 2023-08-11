package cn.hamm.airpower.validate.phone;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>标记电话验证 座机或手机</h1>
 * 请注意，请自行做非空验证
 *
 * @author Hamm
 */
@Constraint(validatedBy = PhoneAnnotationValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    /**
     * <h1>错误信息</h1>
     */
    String message() default "不是有效的电话号码";

    /**
     * <h1>验证组</h1>
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * <h1>是否允许手机号格式</h1>
     */
    boolean mobile() default true;

    /**
     * <h1>是否允许座机电话格式</h1>
     */
    boolean tel() default true;
}