package cn.hamm.airpower.annotation;

import java.lang.annotation.*;

/**
 * <h1>标记为搜索字段</h1>
 *
 * @author Hamm.cn
 * @apiNote 默认为 {@code LIKE}，支持 {@code LIKE}, {@code JOIN}, {@code EQUALS}
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
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
         * <h2>{@code JOIN} 查询</h2>
         */
        JOIN,
    }
}

