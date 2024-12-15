package cn.hamm.airpower.validate.phone;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * <h1>标记电话验证 座机或手机</h1>
 *
 * @author Hamm.cn
 * @apiNote 请注意，请自行做非空验证
 */
@Constraint(validatedBy = PhoneAnnotationValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Phone {
    /**
     * <h3>错误信息</h3>
     */
    String message() default "不是有效的电话号码";

    /**
     * <h3>验证组</h3>
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * <h3>是否允许手机号格式</h3>
     */
    boolean mobile() default true;

    /**
     * <h3>是否允许座机电话格式</h3>
     */
    boolean tel() default true;
}