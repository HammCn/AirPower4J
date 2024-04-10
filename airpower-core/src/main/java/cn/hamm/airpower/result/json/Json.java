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
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
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
}