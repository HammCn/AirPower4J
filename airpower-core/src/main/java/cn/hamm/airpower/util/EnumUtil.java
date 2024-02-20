package cn.hamm.airpower.util;

import cn.hamm.airpower.interfaces.IEnum;
import cn.hamm.airpower.result.Result;
import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>枚举助手</h1>
 *
 * @author Hamm
 */
@SuppressWarnings("unused")
public class EnumUtil {
    /**
     * 查询指定value的枚举项目
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param <T>       [泛型] 当前类型
     * @return 指定的枚举项目
     */
    public static <T extends IEnum> T getEnumByValue(Class<T> enumClass, int value) {
        try {
            Method getValue = enumClass.getMethod("getValue");
            //取出所有枚举类型
            Object[] objs = enumClass.getEnumConstants();
            for (Object obj : objs) {
                int exitValue = (int) getValue.invoke(obj);
                if (exitValue == value) {
                    //noinspection unchecked
                    return (T) obj;
                }
            }
        } catch (Exception ignored) {
            Result.PARAM_INVALID.show(value + "不在可选范围内");
        }
        return null;
    }


    /**
     * 获取指定枚举类的Map数据
     *
     * @param clazz  枚举类
     * @param params 参数列表
     * @return 返回结果
     */
    public static @NotNull List<Map<String, String>> getEnumMapList(@NotNull Class<?> clazz, String... params) {
        List<Map<String, String>> mapList = new ArrayList<>();
        for (Object obj : clazz.getEnumConstants()) {
            //取出所有枚举类型
            Map<String, String> item = new HashMap<>(params.length);
            for (String param : params) {
                // 依次取出参数的值
                try {
                    Method method = clazz.getMethod("get" + StrUtil.upperFirst(param));
                    item.put(param, method.invoke(obj).toString());
                } catch (Exception ignored) {
                }
            }
            mapList.add(item);
        }
        return mapList;
    }

    /**
     * 获取指定枚举类的Map数据
     *
     * @param clazz 枚举类
     * @return 返回结果
     */
    public static <T extends IEnum> @NotNull List<Map<String, String>> getEnumMapList(Class<T> clazz) {
        return getEnumMapList(clazz, "value", "label");
    }
}
