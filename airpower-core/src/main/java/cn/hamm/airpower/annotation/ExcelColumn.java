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
     * <h3>列数据类型</h3>
     */
    Type value() default Type.TEXT;

    /**
     * <h3>列数据类型</h3>
     */
    enum Type {
        /**
         * <h3>普通文本</h3>
         */
        TEXT,

        /**
         * <h3>时间日期</h3>
         */
        DATETIME,

        /**
         * <h3>数字</h3>
         */
        NUMBER,

        /**
         * <h3>字典</h3>
         *
         * @apiNote 请确保同时标记了 @{@link Dictionary}
         */
        DICTIONARY,

        /**
         * <h3>布尔值</h3>
         */
        BOOLEAN
    }
}
