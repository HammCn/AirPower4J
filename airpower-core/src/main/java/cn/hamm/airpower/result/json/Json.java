package cn.hamm.airpower.result.json;

import cn.hamm.airpower.result.IResult;
import cn.hamm.airpower.result.Result;
import cn.hamm.airpower.result.ResultException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

/**
 * <h1>简单JSON对象</h1>
 *
 * @author Hamm
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Json implements IResult {
    /**
     * <h2>错误码</h2>
     */
    private int code = Result.SUCCESS.getCode();

    /**
     * <h2>错误信息</h2>
     */
    private String message = Result.SUCCESS.getMessage();

    /**
     * <h2>实例化JSON</h2>
     *
     * @param message 错误信息
     */
    public Json(String message) {
        this.message = message;
    }

    /**
     * <h2>实例化JSON</h2>
     *
     * @param result 枚举
     */
    public Json(Result result) {
        this.code = result.getCode();
        this.message = result.getMessage();
    }

    /**
     * <h2>实例化JSON</h2>
     *
     * @param result  枚举
     * @param message 错误信息
     */
    public Json(Result result, String message) {
        this.code = result.getCode();
        this.message = message;
    }

    /**
     * <h2>实例化JSON</h2>
     *
     * @param result  自定义响应类
     * @param message 错误信息
     */
    public Json(ResultException result, String message) {
        this.code = result.getCode();
        this.message = message;
    }

    /**
     * <h2>将JSON字符串转到指定的类的实例</h2>
     *
     * @param json  字符串
     * @param clazz 目标类
     * @param <E>   目标类
     * @return 目标类的实例
     */
    public static <E> @Nullable E parse(String json, Class<E> clazz) {
        try {
            return getObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException exception) {
            log.error("JSON字符串转对象失败", exception);
            return null;
        }
    }


    /**
     * <h2>将指定对象转到JSON字符串</h2>
     *
     * @param object 对象
     * @return 字符串
     */
    public static String toString(Object object) {
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            log.error("对象转JSON字符串失败", exception);
            return "";
        }
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 忽略未声明的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}