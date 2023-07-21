package cn.hamm.airpower.result.json;

import cn.hamm.airpower.annotation.Description;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <h1>带DATA返回的JSON对象</h1>
 *
 * @author Hamm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class JsonData extends Json {
    /**
     * <h1>返回数据</h1>
     */
    @Description("返回数据")
    private Object data;

    /**
     * <h1>实例化JSON</h1>
     *
     * @param data 数据
     */
    public JsonData(Object data) {
        super("获取成功");
        this.data = data;
    }

    /**
     * <h1>实例化JSON</h1>
     *
     * @param data    数据
     * @param message 错误信息
     */
    public JsonData(Object data, String message) {
        super(message);
        this.data = data;
    }
}