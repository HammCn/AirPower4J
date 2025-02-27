package cn.hamm.airpower.util;

import cn.hamm.airpower.config.Constant;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * <h1>文件工具类</h1>
 *
 * @author Hamm.cn
 */
@Slf4j
public class FileUtil {
    /**
     * <h3>获取文件名后缀</h3>
     *
     * @param fileName 文件名
     * @return 后缀
     */
    public static @NotNull String getExtension(@NotNull String fileName) {
        return fileName.substring(fileName.lastIndexOf(Constant.DOT) + 1).toLowerCase();
    }

    /**
     * <h3>格式化文件大小</h3>
     *
     * @param size 文件大小
     * @return 格式化后的文件大小
     */
    public static String formatSize(long size) {
        double fileSize = size;
        if (fileSize <= 0) {
            log.error("错误的文件大小: {}", size);
            return Constant.LINE;
        }
        String[] units = {"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        for (String unit : units) {
            if (fileSize < Constant.FILE_SCALE) {
                return decimalFormat.format(fileSize) + unit;
            }
            fileSize /= Constant.FILE_SCALE;
        }
        return Constant.LINE;
    }
}
