package cn.hamm.airpower.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <h1>自定义异常包装类</h1>
 *
 * @author Hamm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ResultException extends RuntimeException implements IResult {
    /**
     * 错误代码
     */
    private int code = Result.ERROR.getCode();

    /**
     * 错误信息
     */
    private String message = Result.ERROR.getMessage();

    /**
     * 错误数据
     */
    private Object data = null;

    /**
     * 抛出一个自定义错误信息的默认异常
     *
     * @param message 错误信息
     */
    public ResultException(String message) {
        this.setCode(Result.ERROR.getCode()).setMessage(message);
    }

    /**
     * 直接抛出一个异常
     *
     * @param error 异常
     */
    public ResultException(Result error) {
        this.setCode(error.getCode()).setMessage(error.getMessage());
    }

    /**
     * 直接抛出一个异常
     *
     * @param code    错误代码
     * @param message 错误信息
     */
    public ResultException(int code, String message) {
        this.setCode(code).setMessage(message);
    }
}
