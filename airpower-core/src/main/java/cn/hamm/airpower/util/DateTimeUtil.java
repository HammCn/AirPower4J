package cn.hamm.airpower.util;

import cn.hamm.airpower.enums.DateTimeFormatter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * <h1>时间日期格式化工具类</h1>
 *
 * @author Hamm.cn
 */
public class DateTimeUtil {
    /**
     * <h3>默认时区</h3>
     */
    private static final String ASIA_CHONGQING = "Asia/Chongqing";

    /**
     * <h3>禁止外部实例化</h3>
     */
    @Contract(pure = true)
    private DateTimeUtil() {
    }

    /**
     * <h3>格式化时间</h3>
     *
     * @param milliSecond 毫秒
     * @return 格式化后的时间
     */
    public static @NotNull String format(long milliSecond) {
        return format(milliSecond, DateTimeFormatter.FULL_DATETIME.getValue());
    }

    /**
     * <h3>格式化时间</h3>
     *
     * @param milliSecond 毫秒
     * @param formatter   格式化模板
     * @return 格式化后的时间
     */
    public static @NotNull String format(long milliSecond, @NotNull DateTimeFormatter formatter) {
        return format(milliSecond, formatter.getValue());
    }

    /**
     * <h3>格式化时间</h3>
     *
     * @param milliSecond 毫秒
     * @param formatter   格式化模板
     * @return 格式化后的时间
     */
    public static @NotNull String format(long milliSecond, String formatter) {
        return format(milliSecond, formatter, ASIA_CHONGQING);
    }

    /**
     * <h3>格式化时间</h3>
     *
     * @param milliSecond 毫秒
     * @param formatter   格式化模板
     * @param zone        时区
     * @return 格式化后的时间
     */
    public static @NotNull String format(long milliSecond, String formatter, String zone) {
        Instant instant = Instant.ofEpochMilli(milliSecond);
        ZonedDateTime beijingTime = instant.atZone(ZoneId.of(zone));
        return beijingTime.format(java.time.format.DateTimeFormatter.ofPattern(formatter));
    }
}
