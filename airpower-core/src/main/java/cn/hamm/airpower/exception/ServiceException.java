package cn.hamm.airpower.exception;

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
public class ServiceException extends RuntimeException implements IException {
    /**
     * <h3>错误代码</h3>
     */
    private int code = ServiceError.SERVICE_ERROR.getCode();

    /**
     * <h3>错误信息</h3>
     */
    private String message = ServiceError.SERVICE_ERROR.getMessage();

    /**
     * <h3>错误数据</h3>
     */
    private Object data = null;

    /**
     * <h3>抛出一个自定义错误信息的默认异常</h3>
     *
     * @param message 错误信息
     */
    public ServiceException(String message) {
        setCode(ServiceError.SERVICE_ERROR.getCode()).setMessage(message);
    }

    /**
     * <h3>直接抛出一个异常</h3>
     *
     * @param serviceError 异常
     */
    public ServiceException(@NotNull ServiceError serviceError) {
        setCode(serviceError.getCode()).setMessage(serviceError.getMessage());
    }

    /**
     * <h3>直接抛出一个异常</h3>
     *
     * @param serviceError 异常
     * @param message      错误信息
     */
    public ServiceException(@NotNull ServiceError serviceError, String message) {
        setCode(serviceError.getCode()).setMessage(message);
    }

    /**
     * <h3>直接抛出一个异常</h3>
     *
     * @param code    错误代码
     * @param message 错误信息
     */
    public ServiceException(int code, String message) {
        setCode(code).setMessage(message);
    }

    /**
     * <h3>直接抛出一个异常</h3>
     *
     * @param exception 异常
     */
    public ServiceException(@NotNull Exception exception) {
        setCode(ServiceError.SERVICE_ERROR.getCode()).setMessage(exception.getMessage());
    }
}
