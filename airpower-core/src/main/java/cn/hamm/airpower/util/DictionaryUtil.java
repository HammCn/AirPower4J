package cn.hamm.airpower.util;

import cn.hamm.airpower.interfaces.IDictionary;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>枚举字典助手</h1>
 *
 * @author Hamm
 */
public class DictionaryUtil {
    /**
     * <h2>查询指定key的枚举字典项目</h2>
     *
     * @param enumClass 枚举字典类
     * @param key       枚举字典值
     * @param <T>       [泛型] 当前类型
     * @return 指定的枚举字典项目
     */
    @SneakyThrows
    public static <T extends IDictionary> T getDictionaryByKey(Class<T> enumClass, int key) {
        Method getKey = enumClass.getMethod("getKey");

        // 取出所有枚举类型
        Object[] objs = enumClass.getEnumConstants();
        for (Object obj : objs) {
            int exitValue = (int) getKey.invoke(obj);
            if (exitValue == key) {
                //noinspection unchecked
                return (T) obj;
            }
        }
        return null;
    }

    /**
     * <h2>获取指定枚举类的Map数据</h2>
     *
     * @param clazz 枚举类
     * @return 返回结果
     */
    public static <T extends IDictionary> @NotNull List<Map<String, String>> getDictionaryList(Class<T> clazz) {
        return getDictionaryList(clazz, "key", "label");
    }


    /**
     * <h2>获取指定枚举类的Map数据</h2>
     *
     * @param clazz  枚举类
     * @param params 参数列表
     * @return 返回结果
     */
    public static @NotNull List<Map<String, String>> getDictionaryList(@NotNull Class<?> clazz, String... params) {
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
}
