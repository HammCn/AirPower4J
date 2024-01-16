package cn.hamm.airpower.result.json;

import cn.hamm.airpower.result.IResult;
import cn.hamm.airpower.result.Result;
import cn.hamm.airpower.result.ResultException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <h1>简单JSON对象</h1>
 *
 * @author Hamm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@SuppressWarnings("unused")
public class Json implements IResult {
    /**
     * 错误码
     */
    private int code = Result.SUCCESS.getCode();

    /**
     * 错误信息
     */
    private String message = Result.SUCCESS.getMessage();

    /**
     * 实例化JSON
     *
     * @param message 错误信息
     */
    public Json(String message) {
        this.message = message;
    }

    /**
     * 实例化JSON
     *
     * @param result 枚举
     */
    public Json(Result result) {
        this.code = result.getCode();
        this.message = result.getMessage();
    }

    /**
     * 实例化JSON
     *
     * @param result  枚举
     * @param message 错误信息
     */
    public Json(Result result, String message) {
        this.code = result.getCode();
        this.message = message;
    }

    /**
     * 实例化JSON
     *
     * @param result  自定义响应类
     * @param message 错误信息
     */
    public Json(ResultException result, String message) {
        this.code = result.getCode();
        this.message = message;
    }
}