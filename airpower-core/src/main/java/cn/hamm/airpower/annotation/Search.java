package cn.hamm.airpower.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static cn.hamm.airpower.annotation.Search.Mode.LIKE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <h1>标记为搜索字段</h1>
 *
 * @author Hamm.cn
 * @apiNote 默认为 {@link Mode#LIKE}，支持 {@link Mode#LIKE}, {@link Mode#JOIN}, {@link Mode#EQUALS}
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Documented
public @interface Search {
    /**
     * <h3>搜索方式</h3>
     */
    Mode value() default LIKE;

    /**
     * <h3>搜索类型</h3>
     */
    enum Mode {
        /**
         * <h3>相等</h3>
         */
        EQUALS,

        /**
         * <h3>字符串模糊匹配</h3>
         */
        LIKE,

        /**
         * <h3>{@code JOIN} 查询</h3>
         */
        JOIN,
    }
}

