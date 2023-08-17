package cn.hamm.airpower.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>标记为搜索字段</h1>
 *
 * @author hamm
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Search {
    /**
     * <h2>搜索方式</h2>
     */
    Mode value() default Mode.LIKE;

    /**
     * <h2>搜索类型</h2>
     */
    enum Mode {
        /**
         * <h2>相等</h2>
         */
        EQUALS,

        /**
         * <h2>字符串模糊匹配</h2>
         */
        LIKE,

        /**
         * <h2>JOIN查询</h2>
         */
        JOIN,
    }
}

