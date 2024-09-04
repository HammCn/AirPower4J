package cn.hamm.airpower.annotation;

import cn.hamm.airpower.validate.dictionary.Dictionary;

import java.lang.annotation.*;

/**
 * <h1>{@code Excel} 导出列</h1>
 *
 * @author Hamm.cn
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ExcelColumn {
    /**
     * <h2>列数据类型</h2>
     */
    Type value() default Type.TEXT;

    /**
     * <h2>列数据类型</h2>
     */
    enum Type {
        /**
         * <h2>普通文本</h2>
         */
        TEXT,

        /**
         * <h2>时间日期</h2>
         */
        DATETIME,

        /**
         * <h2>数字</h2>
         */
        NUMBER,

        /**
         * <h2>字典</h2>
         *
         * @apiNote 请确保同时标记了 @{@link Dictionary}
         */
        DICTIONARY,

        /**
         * <h2>布尔值</h2>
         */
        BOOLEAN
    }
}
