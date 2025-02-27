package cn.hamm.airpower.util;

import cn.hamm.airpower.config.Constant;
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

    /**
     * <h3>友好格式化时间</h3>
     *
     * @param milliSecond 毫秒时间戳
     * @return 友好格式化后的时间
     */
    public static @NotNull String friendlyFormatMillisecond(long milliSecond) {
        long second = milliSecond / Constant.MILLISECONDS_PER_SECOND;
        return friendlyFormatSecond(second);
    }

    /**
     * <h3>友好格式化时间</h3>
     *
     * @param second {@code Unix}秒时间戳
     * @return 友好格式化后的时间
     */
    public static @NotNull String friendlyFormatSecond(long second) {
        long currentSecond = System.currentTimeMillis() / Constant.MILLISECONDS_PER_SECOND;
        long diff = Math.abs(currentSecond - second);
        String suffix = second > currentSecond ? "后" : "前";
        long[] stepSeconds = new long[]{
                0,
                Constant.SECOND_PER_MINUTE,
                Constant.SECOND_PER_HOUR,
                Constant.SECOND_PER_DAY,
                Constant.SECOND_PER_DAY * Constant.DAY_PER_WEEK,
                Constant.SECOND_PER_DAY * Constant.DAY_PER_MONTH,
                Constant.SECOND_PER_DAY * Constant.DAY_PER_YEAR
        };
        String[] stepLabels = new String[]{
                "秒",
                "分钟",
                "小时",
                "天",
                "周",
                "月",
                "年"
        };
        for (int i = stepSeconds.length - 1; i >= 0; i--) {
            long step = stepSeconds[i];
            if (second < currentSecond && diff < Constant.SECOND_PER_MINUTE) {
                // 过去时间，且小于60s
                return "刚刚";
            }
            if (diff >= step) {
                if (step == 0) {
                    return String.format("%d%s%s", diff, stepLabels[i], suffix);
                }
                return String.format("%d%s%s", diff / step, stepLabels[i], suffix);
            }
        }
        return "未知时间";
    }
}
