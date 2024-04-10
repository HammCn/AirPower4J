package cn.hamm.airpower.result.json;

import cn.hamm.airpower.annotation.Description;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <h1>带DATA返回的JSON对象</h1>
 *
 * @author Hamm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class JsonData extends Json {
    /**
     * <h2>返回数据</h2>
     */
    @Description("返回数据")
    private Object data;

    /**
     * <h2>实例化JSON</h2>
     *
     * @param data 数据
     */
    public JsonData(Object data) {
        super("获取成功");
        this.data = data;
    }

    /**
     * <h2>实例化JSON</h2>
     *
     * @param data    数据
     * @param message 错误信息
     */
    public JsonData(Object data, String message) {
        super(message);
        this.data = data;
    }

    /**
     * <h2>实例化JSON</h2>
     *
     * @param data    数据
     * @param message 错误信息
     * @param code    错误代码
     */
    public JsonData(Object data, String message, int code) {
        super(message);
        this.data = data;
        this.setCode(code);
    }
}