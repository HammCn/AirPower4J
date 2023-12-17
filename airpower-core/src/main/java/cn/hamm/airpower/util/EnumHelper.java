package cn.hamm.airpower.util;

import cn.hamm.airpower.interfaces.IEnum;
import cn.hamm.airpower.result.Result;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * 枚举助手
 *
 * @author Hamm
 */
@Component
public class EnumHelper {


    /**
     * 查询指定value的枚举项目
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param <T>       [泛型] 当前类型
     * @return 指定的枚举项目
     */
    public <T extends IEnum> T getEnumByValue(Class<T> enumClass, int value) {
        try {
            Method getValue = enumClass.getMethod("getValue");
            //取出所有枚举类型
            Object[] objs = enumClass.getEnumConstants();
            for (Object obj : objs) {
                @SuppressWarnings("unchecked")
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
}