package cn.hamm.airpower.exception;

import cn.hamm.airpower.enums.SystemError;
import cn.hamm.airpower.interfaces.IException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>系统异常包装类</h1>
 *
 * @author Hamm.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SystemException extends RuntimeException implements IException {
    /**
     * <h2>错误代码</h2>
     */
    private int code = SystemError.SERVICE_ERROR.getCode();

    /**
     * <h2>错误信息</h2>
     */
    private String message = SystemError.SERVICE_ERROR.getMessage();

    /**
     * <h2>错误数据</h2>
     */
    private Object data = null;

    /**
     * <h2>抛出一个自定义错误信息的默认异常</h2>
     *
     * @param message 错误信息
     */
    public SystemException(String message) {
        this.setCode(SystemError.SERVICE_ERROR.getCode()).setMessage(message);
    }

    /**
     * <h2>直接抛出一个异常</h2>
     *
     * @param errorCode 异常
     */
    public SystemException(@NotNull SystemError errorCode) {
        this.setCode(errorCode.getCode()).setMessage(errorCode.getMessage());
    }

    /**
     * <h2>直接抛出一个异常</h2>
     *
     * @param errorCode 异常
     * @param message   错误信息
     */
    public SystemException(@NotNull SystemError errorCode, String message) {
        this.setCode(errorCode.getCode())
                .setMessage(message);
    }

    /**
     * <h2>直接抛出一个异常</h2>
     *
     * @param code    错误代码
     * @param message 错误信息
     */
    public SystemException(int code, String message) {
        this.setCode(code).setMessage(message);
    }

    /**
     * <h2>直接抛出一个异常</h2>
     *
     * @param exception 异常
     */
    public SystemException(@NotNull Exception exception) {
        this.setCode(SystemError.EMAIL_ERROR.getCode()).setMessage(exception.getMessage());
    }
}
