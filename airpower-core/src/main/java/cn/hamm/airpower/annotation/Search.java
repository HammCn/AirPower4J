package cn.hamm.airpower.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>标记为搜索字段</h1>
 *
 * @author Hamm
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Search {
    /**
     * 搜索方式
     */
    Mode value() default Mode.LIKE;

    /**
     * 搜索类型
     */
    enum Mode {
        /**
         * 相等
         */
        EQUALS,

        /**
         * 字符串模糊匹配
         */
        LIKE,

        /**
         * JOIN查询
         */
        JOIN,
    }
}

