package cn.hamm.airpower.util;

import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.interfaces.IDictionary;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

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
     * <h2>查询指定key的枚举字典项目</h2>
     *
     * @param enumClass 枚举字典类
     * @param key       枚举字典值
     * @param <D>       [泛型] 当前类型
     * @return 指定的枚举字典项目
     */

    public final <D extends IDictionary> @Nullable D getDictionaryByKey(@NotNull Class<D> enumClass, int key) {
        try {
            // 取出所有枚举类型
            D[] objs = enumClass.getEnumConstants();
            for (D obj : objs) {
                if (obj.equalsKey(key)) {
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
        return getDictionaryList(clazz, IDictionary::getKey, IDictionary::getLabel);
    }

    /**
     * <h2>获取指定枚举类的Map数据</h2>
     *
     * @param clazz     枚举类
     * @param functions 枚举类中的方法
     * @return 返回结果
     */
    @SafeVarargs
    public final <D extends IDictionary> @NotNull List<Map<String, Object>> getDictionaryList(
            @NotNull Class<D> clazz, Function<D, Object>... functions
    ) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (D d : clazz.getEnumConstants()) {
            //取出所有枚举类型
            Map<String, Object> item = new HashMap<>(functions.length);
            for (Function<D, Object> function : functions) {
                item.put(function.toString().replace(Constant.GET, Constant.EMPTY_STRING).toLowerCase(), function.apply(d));
            }
            mapList.add(item);
        }
        return mapList;
    }

    public <D extends IDictionary> D getDictionary(@NotNull Class<D> enumClass, Function<D, Object> function, Object value) {
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
}
