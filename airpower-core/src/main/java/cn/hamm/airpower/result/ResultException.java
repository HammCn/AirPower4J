package cn.hamm.airpower.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <h1>自定义异常包装类</h1>
 *
 * @author Hamm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ResultException extends RuntimeException implements IResult {
    /**
     * <h2>错误代码</h2>
     */
    private int code = Result.ERROR.getCode();

    /**
     * <h2>错误信息</h2>
     */
    private String message = Result.ERROR.getMessage();

    /**
     * <h2>错误数据</h2>
     */
    private Object data = null;

    /**
     * <h2>抛出一个自定义错误信息的默认异常</h2>
     *
     * @param message 错误信息
     */
    public ResultException(String message) {
        this.setCode(Result.ERROR.getCode()).setMessage(message);
    }

    /**
     * <h2>直接抛出一个异常</h2>
     *
     * @param error 异常
     */
    public ResultException(Result error) {
        this.setCode(error.getCode()).setMessage(error.getMessage());
    }

    /**
     * <h2>直接抛出一个异常</h2>
     *
     * @param code    错误代码
     * @param message 错误信息
     */
    public ResultException(int code, String message) {
        this.setCode(code).setMessage(message);
    }
}
