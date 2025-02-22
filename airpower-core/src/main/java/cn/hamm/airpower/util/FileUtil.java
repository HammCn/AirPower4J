package cn.hamm.airpower.util;

import cn.hamm.airpower.config.Constant;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>文件工具类</h1>
 *
 * @author Hamm.cn
 */
public class FileUtil {
    /**
     * 获取文件名后缀
     *
     * @param fileName 文件名
     * @return 后缀
     */
    public static @NotNull String getExtension(@NotNull String fileName) {
        return fileName.substring(fileName.lastIndexOf(Constant.DOT) + 1).toLowerCase();
    }
}
