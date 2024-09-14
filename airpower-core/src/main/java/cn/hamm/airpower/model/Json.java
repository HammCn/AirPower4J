package cn.hamm.airpower.model;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.enums.ServiceError;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.interfaces.IException;
import cn.hamm.airpower.root.RootEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <h1>简单 {@code JSON} 对象</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
@Slf4j
public class Json {
    /**
     * <h2>{@code ObjectMapper}</h2>
     */
    private static ObjectMapper objectMapper = null;

    /**
     * <h2>错误代码</h2>
     */
    @Description("错误代码")
    private int code = Constant.JSON_SUCCESS_CODE;

    /**
     * <h2>错误信息</h2>
     */
    @Description("错误信息")
    private String message = Constant.JSON_SUCCESS_MESSAGE;

    /**
     * <h2>返回数据</h2>
     */
    @Description("返回数据")
    private Object data;

    @Contract(pure = true)
    private Json() {
        // 禁止外部实例化
    }

    /**
     * <h2>输出提示信息</h2>
     *
     * @param message 提示信息
     * @return {@code Json}
     */
    public static Json success(String message) {
        return create().setMessage(message);
    }

    /**
     * <h2>输出数据</h2>
     *
     * @param data 数据
     * @return {@code Json}
     */
    public static Json data(Object data) {
        return data(data, "获取成功");
    }

    /**
     * <h2>输出实体</h2>
     *
     * @param id 实体 {@code ID}
     * @return {@code Json}
     */
    public static Json entity(@NotNull Long id) {
        return data(new RootEntity().setId(id));
    }

    /**
     * <h2>输出实体</h2>
     *
     * @param id      实体 {@code Json}
     * @param message 提示信息
     * @return {@code Json}
     */
    public static Json entity(@NotNull Long id, @NotNull String message) {
        return entity(id).setMessage(message);
    }

    /**
     * <h2>输出数据</h2>
     *
     * @param data    数据
     * @param message 提示信息
     * @return {@code Json}
     */
    public static Json data(Object data, String message) {
        return create().setData(data).setMessage(message);
    }

    /**
     * <h2>输出错误</h2>
     *
     * @param error 错误枚举
     * @return {@code Json}
     */
    public static Json error(IException error) {
        return error(error, error.getMessage());
    }

    /**
     * <h2>输出错误</h2>
     *
     * @param error   错误枚举
     * @param message 错误信息
     * @return {@code Json}
     */
    public static Json error(@NotNull IException error, String message) {
        return show(error.getCode(), message, null);
    }

    /**
     * <h2>输出错误</h2>
     *
     * @param message 错误信息
     * @return {@code Json}
     */
    public static Json error(String message) {
        return error(ServiceError.SERVICE_ERROR, message);
    }

    /**
     * <h2>输出 {@code Json}</h2>
     *
     * @param code    错误代码
     * @param message 提示信息
     * @param data    输出数据
     * @return {@code Json}
     */
    public static Json show(int code, String message, Object data) {
        return create().setCode(code).setMessage(message).setData(data);
    }

    /**
     * <h2>{@code Json} 反序列化到指定类</h2>
     *
     * @param json  字符串
     * @param clazz 目标类
     * @param <T>   目标类
     * @return 目标类的实例
     */
    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return getObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException exception) {
            throw new ServiceException(exception);
        }
    }

    /**
     * <h2>{@code Json} 反序列化为数组</h2>
     *
     * @param json  字符串
     * @param clazz 目标数组类
     * @param <T>   目标类型
     * @return 目标类的实例数组
     */
    public static <T> T[] parseList(String json, Class<? extends T[]> clazz) {
        try {
            return getObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException exception) {
            throw new ServiceException(exception);
        }
    }

    /**
     * <h2>{@code Json} 反序列化为 {@code Map}</h2>
     *
     * @param json 字符串
     * @return {@code Map}
     */
    public static Map<String, Object> parse2Map(String json) {
        try {
            TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
            };
            return getObjectMapper().readValue(json, typeRef);
        } catch (Exception exception) {
            throw new ServiceException(exception);
        }
    }

    /**
     * <h2>{@code Json} 反序列化为 {@code ListMap}</h2>
     *
     * @param json 字符串
     * @return {@code List<Map>}
     */
    public static List<Map<String, Object>> parse2MapList(String json) {
        try {
            TypeReference<List<Map<String, Object>>> typeRef = new TypeReference<>() {
            };
            return getObjectMapper().readValue(json, typeRef);
        } catch (Exception exception) {
            throw new ServiceException(exception);
        }
    }

    /**
     * <h2>{@code Json} 序列化到字符串</h2>
     *
     * @param object 对象
     * @return 字符串
     */
    public static String toString(Object object) {
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            return Constant.EMPTY_STRING;
        }
    }

    /**
     * <h2>获取一个配置后的 {@code ObjectMapper}</h2>
     *
     * @return {@code ObjectMapper}
     */
    private static @NotNull ObjectMapper getObjectMapper() {
        if (Objects.isNull(objectMapper)) {
            objectMapper = new ObjectMapper();
            // 忽略未声明的属性
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 忽略值为null的属性
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            // 忽略没有属性的类
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
        return objectMapper;
    }

    /**
     * <h2>初始化一个新的 {@code JSON} 对象</h2>
     *
     * @return {@code JSON} 对象
     */
    @Contract(" -> new")
    public static @NotNull Json create() {
        return new Json();
    }
}
