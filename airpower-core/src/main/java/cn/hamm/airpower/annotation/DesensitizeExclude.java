package cn.hamm.airpower.annotation;

import java.lang.annotation.*;

/**
 * <h1>此接口返回数据不脱敏</h1>
 *
 * @author Hamm.cn
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DesensitizeExclude {
}
