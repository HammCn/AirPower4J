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
     * <h1>搜索方式</h1>
     */
    Mode value() default Mode.LIKE;

    /**
     * <h1>搜索类型</h1>
     */
    enum Mode {
        /**
         * <h1>相等</h1>
         */
        EQUALS,

        /**
         * <h1>字符串模糊匹配</h1>
         */
        LIKE,

        /**
         * <h1>JOIN查询</h1>
         */
        JOIN,
    }
}

