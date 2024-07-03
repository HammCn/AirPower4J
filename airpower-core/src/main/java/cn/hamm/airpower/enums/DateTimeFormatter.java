package cn.hamm.airpower.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h2>格式化模板</h2>
 *
 * @author Hamm.cn
 */
@Getter
@AllArgsConstructor
public enum DateTimeFormatter {
    /**
     * <h2>年</h2>
     */
    YEAR("yyyy"),

    /**
     * <h2>月</h2>
     */
    MONTH("MM"),

    /**
     * <h2>日</h2>
     */
    DAY("dd"),

    /**
     * <h2>时</h2>
     */
    HOUR("HH"),

    /**
     * <h2>分</h2>
     */
    MINUTE("mm"),

    /**
     * <h2>秒</h2>
     */
    SECOND("ss"),

    /**
     * <h2>年月日</h2>
     */
    FULL_DATE("yyyy-MM-dd"),

    /**
     * <h2>时分秒</h2>
     */
    FULL_TIME("HH:mm:ss"),

    /**
     * <h2>年月日时分秒</h2>
     */
    FULL_DATETIME("yyyy-MM-dd HH:mm:ss"),

    /**
     * <h2>月日时分</h2>
     */
    SHORT_DATETIME("MM-dd HH:mm"),
    ;

    private final String value;
}
