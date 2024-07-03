package cn.hamm.airpower.annotation;

import java.lang.annotation.*;

/**
 * <h1>Excel导出列</h1>
 *
 * @author Hamm.cn
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ExcelColumn {
    /**
     * <h2>类型</h2>
     */
    Type value() default Type.TEXT;

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
         * <h2>布尔值</h2>
         */
        BOOLEAN
    }
}
