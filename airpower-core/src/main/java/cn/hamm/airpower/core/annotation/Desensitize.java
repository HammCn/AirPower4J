package cn.hamm.airpower.core.annotation;

import cn.hamm.airpower.core.security.DesensitizeUtil;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <h1>标记字段在 {@code API} 输出时自动脱敏</h1>
 *
 * @author Hamm.cn
 * @apiNote 如需标记不脱敏的接口，可使用 {@link DesensitizeExclude}
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface Desensitize {
    /**
     * <h3>脱敏类型</h3>
     */
    DesensitizeUtil.Type value();

    /**
     * <h3>开始保留位数</h3>
     */
    int head() default 0;

    /**
     * <h3>结束保留位数</h3>
     */
    int tail() default 0;

    /**
     * <h3>脱敏符号</h3>
     *
     * @apiNote <ul>
     * <li>{@code replace==false} 提交的数据包含脱敏符号，则该类数据不更新到数据库</li>
     * <li>{@code replace==true} 提交的数据和脱敏符号一致，则该类数据不更新到数据库</li>
     * </ul>
     */
    String symbol() default "*";

    /**
     * <h3>是否替换</h3>
     *
     * @apiNote 如标记为 {@code true}, 则整体脱敏为符号，而不是单个字符替换
     */
    boolean replace() default false;

}
