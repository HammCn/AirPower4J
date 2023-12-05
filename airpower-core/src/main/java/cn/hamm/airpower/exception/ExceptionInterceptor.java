package cn.hamm.airpower.exception;

import cn.hamm.airpower.result.Result;
import cn.hamm.airpower.result.ResultException;
import cn.hamm.airpower.result.json.Json;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <h1>全局异常处理拦截器</h1>
 *
 * @author Hamm
 */
@ControllerAdvice
@ResponseStatus(HttpStatus.OK)
@ResponseBody
public class ExceptionInterceptor {
    /**
     * 参数验证失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Json badRequestHandle(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        if (!result.hasErrors()) {
            return new Json(Result.PARAM_INVALID);
        }
        if (!result.hasFieldErrors()) {
            return new Json(Result.PARAM_INVALID);
        }
        List<FieldError> errors = result.getFieldErrors();
        for (FieldError error : errors) {
            stringBuilder
                    .append(error.getDefaultMessage())
                    .append("(")
                    .append(error.getField())
                    .append(")");
            break;
        }
        return new Json(Result.PARAM_INVALID, stringBuilder.toString());
    }

    /**
     * 参数校验失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Json badRequestHandle(ConstraintViolationException exception) {
        StringBuilder stringBuilder = new StringBuilder();
        Set<ConstraintViolation<?>> errors = exception.getConstraintViolations();
        for (ConstraintViolation<?> error : errors) {
            stringBuilder
                    .append(error.getMessage())
                    .append("(")
                    .append(error.getInvalidValue())
                    .append(")");
            break;
        }
        return new Json(Result.PARAM_INVALID, stringBuilder.toString());
    }

    /**
     * 删除时的数据关联校验异常
     */
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class, DataIntegrityViolationException.class})
    public Json deleteUsingDataException() {
        return new Json(Result.FORBIDDEN_DELETE_USED, "数据正在使用中,无法被删除!");
    }

    /**
     * 访问的接口没有实现
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Json notFoundHandle() {
        return new Json(Result.SERVICE_NOT_FOUND);
    }

    /**
     * 请求的数据不是标准JSON
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Json dataExceptionHandle() {
        return new Json(Result.REQUEST_CONTENT_TYPE_UNSUPPORTED, "请求参数格式不正确,请检查是否接口支持的JSON");
    }

    /**
     * 不支持的请求方法
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Json methodExceptionHandle(HttpRequestMethodNotSupportedException exception) {
        String supportedMethod = String.join("|", exception.getSupportedMethods());
        return new Json(
                Result.REQUEST_METHOD_UNSUPPORTED,
                exception.getMethod() + "不被支持,请使用" + supportedMethod + "方法请求");
    }

    /**
     * 不支持的数据类型
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Json httpMediaTypeNotSupportedExceptionHandle(HttpMediaTypeNotSupportedException exception) {
        return new Json(
                Result.REQUEST_CONTENT_TYPE_UNSUPPORTED,
                Objects.requireNonNull(exception.getContentType()).toString() + "不被支持,请使用JSON请求");
    }

    /**
     * 数据库连接发生错误
     */
    @ExceptionHandler(CannotCreateTransactionException.class)
    public Json databaseExceptionHandle() {
        return new Json(Result.DATABASE_ERROR);
    }

    /**
     * REDIS连接发生错误
     */
    @ExceptionHandler(RedisConnectionFailureException.class)
    public Json redisExceptionHandle() {
        return new Json(Result.REDIS_ERROR);
    }

    /**
     * 自定义业务异常
     */
    @ExceptionHandler(ResultException.class)
    public Json customExceptionHandle(ResultException result) {
        return new Json(result, result.getMessage());
    }

    /**
     * JWT校验失败错误
     */
    @ExceptionHandler(value = {cn.hutool.jwt.JWTException.class})
    public Json jwtExceptionHandle() {
        return new Json(Result.UNAUTHORIZED);
    }

    /**
     * 数据字段不存在
     */
    @ExceptionHandler(value = PropertyReferenceException.class)
    public Json propertyReferenceExceptionHandle(PropertyReferenceException exception) {
        return new Json(Result.DATABASE_UNKNOWN_FIELD, "不支持的数据字段" + exception.getPropertyName());
    }

    /**
     * 数据表或字段异常
     */
    @ExceptionHandler(value = InvalidDataAccessResourceUsageException.class)
    public Json invalidDataAccessResourceUsageExceptionHandle() {
        return new Json(Result.DATABASE_TABLE_OR_FIELD_ERROR);
    }

    /**
     * 其他异常
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public Object otherExceptionHandle(Exception exception) {
        exception.printStackTrace();
        return new Json(Result.ERROR);
    }
}