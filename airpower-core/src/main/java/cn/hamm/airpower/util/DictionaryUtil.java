package cn.hamm.airpower.util;

import cn.hamm.airpower.interfaces.IDictionary;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * <h1>枚举字典助手</h1>
 *
 * @author Hamm.cn
 */
@Slf4j
@Component
public class DictionaryUtil {

    /**
     * <h2>查字典</h2>
     *
     * @param enumClass 枚举字典类
     * @param key       枚举字典值
     * @param <D>       [泛型] 字典类型
     * @return 查到的字典
     */
    public final <D extends IDictionary> @Nullable D getDictionary(@NotNull Class<D> enumClass, int key) {
        return getDictionary(enumClass, IDictionary::getKey, key);
    }

    /**
     * <h2>查字典</h2>
     *
     * @param enumClass 枚举字典类
     * @param function  获取指定值的方法
     * @param value     比较的值
     * @param <D>       [泛型] 字典类型
     * @return 查到的字典
     */
    public final <D extends IDictionary> @Nullable D getDictionary(@NotNull Class<D> enumClass, Function<D, Object> function, Object value) {
        try {
            // 取出所有枚举类型
            D[] objs = enumClass.getEnumConstants();
            for (D obj : objs) {
                if (function.apply(obj).equals(value)) {
                    return obj;
                }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
        return null;
    }

    /**
     * <h2>获取指定枚举类的Map数据</h2>
     *
     * @param clazz 枚举类
     * @return 返回结果
     */
    public final <D extends IDictionary> @NotNull List<Map<String, Object>> getDictionaryList(@NotNull Class<D> clazz) {
        return getDictionaryList(clazz, "key", "label");
    }

    /**
     * <h2>获取指定枚举类的Map数据</h2>
     *
     * @param clazz  枚举类
     * @param params 参数列表
     * @return 返回结果
     */
    public final <D extends IDictionary> @NotNull List<Map<String, Object>> getDictionaryList(
            @NotNull Class<D> clazz, String... params
    ) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Object obj : clazz.getEnumConstants()) {
            //取出所有枚举类型
            Map<String, Object> item = new HashMap<>(params.length);
            for (String param : params) {
                // 依次取出参数的值
                try {
                    Method method = clazz.getMethod("get" + StringUtils.capitalize(param));
                    item.put(param, method.invoke(obj).toString());
                } catch (Exception exception) {
                    log.error(exception.getMessage(), exception);
                }
            }
            mapList.add(item);
        }
        return mapList;
    }
}
