package cn.hamm.airpower.interceptor;

import cn.hamm.airpower.config.Configs;
import cn.hamm.airpower.config.Constant;
import cn.hamm.airpower.config.MessageConstant;
import cn.hamm.airpower.enums.ServiceError;
import cn.hamm.airpower.exception.ServiceException;
import cn.hamm.airpower.interceptor.document.ApiDocument;
import cn.hamm.airpower.model.Json;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <h1>全局异常处理拦截器</h1>
 *
 * @author Hamm.cn
 * @see ServiceError
 */
@ControllerAdvice
@ResponseStatus(HttpStatus.OK)
@ResponseBody
@Slf4j
public class ExceptionInterceptor {

    /**
     * <h2>参数验证失败</h2>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Json badRequestHandle(@NotNull MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());
        BindingResult result = exception.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        if (!result.hasErrors()) {
            return Json.error(ServiceError.PARAM_INVALID);
        }
        if (!result.hasFieldErrors()) {
            return Json.error(ServiceError.PARAM_INVALID);
        }
        List<FieldError> errors = result.getFieldErrors();
        errors.stream().findFirst().ifPresent(error -> stringBuilder.append(String.format(
                MessageConstant.MESSAGE_AND_DESCRIPTION, error.getDefaultMessage(), error.getField()
        )));
        return Json.error(ServiceError.PARAM_INVALID, stringBuilder.toString());
    }

    /**
     * <h2>参数校验失败</h2>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Json badRequestHandle(@NotNull ConstraintViolationException exception) {
        log.error(exception.getMessage());
        StringBuilder stringBuilder = new StringBuilder();
        Set<ConstraintViolation<?>> errors = exception.getConstraintViolations();
        errors.stream().findFirst().ifPresent(error -> stringBuilder.append(String.format(
                MessageConstant.MESSAGE_AND_DESCRIPTION, error.getMessage(), error.getInvalidValue().toString()
        )));
        return Json.error(ServiceError.PARAM_INVALID, stringBuilder.toString());
    }

    /**
     * <h2>删除时的数据关联校验异常</h2>
     */
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class, DataIntegrityViolationException.class})
    public Json deleteUsingDataException(@NotNull java.lang.Exception exception) {
        log.error(exception.getMessage());
        if (Configs.getServiceConfig().isDebug()) {
            log.error(ServiceError.FORBIDDEN_DELETE_USED.getMessage(), exception);
        }
        return Json.error(ServiceError.FORBIDDEN_DELETE_USED);
    }

    /**
     * <h2>访问的接口没有实现</h2>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Json notFoundHandle(@NotNull NoHandlerFoundException exception, HttpServletResponse response) {
        log.error(exception.getMessage());
        if (Configs.getServiceConfig().isEnableDocument()) {
            String[] arr = exception.getRequestURL().split(Constant.SLASH);
            if (arr.length > 1) {
                String packageName = arr[arr.length - 1];
                boolean result = ApiDocument.writeEntityDocument(packageName, response);
                if (!result) {
                    response.reset();
                }
            }
        }
        return Json.error(ServiceError.API_SERVICE_UNSUPPORTED);
    }

    /**
     * <h2>请求的数据不是标准JSON</h2>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Json dataExceptionHandle(@NotNull HttpMessageNotReadableException exception) {
        log.error(exception.getMessage());
        return Json.error(ServiceError.REQUEST_CONTENT_TYPE_UNSUPPORTED, MessageConstant.PARAM_INVALID_MAY_BE_NOT_JSON);
    }

    /**
     * <h2>不支持的请求方法</h2>
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Json methodExceptionHandle(@NotNull HttpRequestMethodNotSupportedException exception) {
        log.error(exception.getMessage());
        String supportedMethod = String.join(Constant.SLASH, Objects.requireNonNull(exception.getSupportedMethods()));
        return Json.error(ServiceError.REQUEST_METHOD_UNSUPPORTED, String.format(
                MessageConstant.REQUEST_METHOD_NOT_SUPPORTED, exception.getMethod(), supportedMethod
        ));
    }

    /**
     * <h2>不支持的数据类型</h2>
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Json httpMediaTypeNotSupportedExceptionHandle(@NotNull HttpMediaTypeNotSupportedException exception) {
        log.error(exception.getMessage());
        if (Configs.getServiceConfig().isDebug()) {
            log.error(ServiceError.REQUEST_CONTENT_TYPE_UNSUPPORTED.getMessage(), exception);
        }
        return Json.error(ServiceError.REQUEST_CONTENT_TYPE_UNSUPPORTED, String.format(
                MessageConstant.ONLY_CONTENT_TYPE_JSON_SUPPORTED,
                Objects.requireNonNull(exception.getContentType()).getType()
        ));
    }

    /**
     * <h2>数据库连接发生错误</h2>
     */
    @ExceptionHandler(CannotCreateTransactionException.class)
    public Json databaseExceptionHandle(@NotNull CannotCreateTransactionException exception) {
        log.error(exception.getMessage());
        if (Configs.getServiceConfig().isDebug()) {
            log.error(ServiceError.DATABASE_ERROR.getMessage(), exception);
        }
        return Json.error(ServiceError.DATABASE_ERROR);
    }

    /**
     * <h2>REDIS连接发生错误</h2>
     */
    @ExceptionHandler(RedisConnectionFailureException.class)
    public Json redisExceptionHandle(@NotNull RedisConnectionFailureException exception) {
        log.error(exception.getMessage());
        if (Configs.getServiceConfig().isDebug()) {
            log.error(ServiceError.REDIS_ERROR.getMessage(), exception);
        }
        return Json.error(ServiceError.REDIS_ERROR);
    }

    /**
     * <h2>系统自定义异常</h2>
     */
    @ExceptionHandler(ServiceException.class)
    public Json systemExceptionHandle(@NotNull ServiceException exception) {
        log.error(exception.getMessage());
        if (Configs.getServiceConfig().isDebug()) {
            log.error(ServiceError.SERVICE_ERROR.getMessage(), exception);
        }
        return Json.error(exception).setData(exception.getData());
    }

    /**
     * <h2>数据字段不存在</h2>
     */
    @ExceptionHandler(value = PropertyReferenceException.class)
    public Json propertyReferenceExceptionHandle(@NotNull PropertyReferenceException exception) {
        log.error(exception.getMessage());
        if (Configs.getServiceConfig().isDebug()) {
            log.error(ServiceError.DATABASE_UNKNOWN_FIELD.getMessage(), exception);
        }
        return Json.error(ServiceError.DATABASE_UNKNOWN_FIELD, String.format(
                MessageConstant.MISSING_FIELD_IN_DATABASE, exception.getPropertyName()
        ));
    }

    /**
     * <h2>数据表或字段异常</h2>
     */
    @ExceptionHandler(value = InvalidDataAccessResourceUsageException.class)
    public Json invalidDataAccessResourceUsageExceptionHandle(
            @NotNull InvalidDataAccessResourceUsageException exception
    ) {
        log.error(exception.getMessage());
        if (Configs.getServiceConfig().isDebug()) {
            log.error(ServiceError.DATABASE_TABLE_OR_FIELD_ERROR.getMessage(), exception);
        }
        return Json.error(ServiceError.DATABASE_TABLE_OR_FIELD_ERROR);
    }

    /**
     * <h2>数据表或字段异常</h2>
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public Json maxUploadSizeExceededExceptionHandle(@NotNull MaxUploadSizeExceededException exception) {
        log.error(exception.getMessage());
        if (Configs.getServiceConfig().isDebug()) {
            log.error(ServiceError.FORBIDDEN_UPLOAD_MAX_SIZE.getMessage(), exception);
        }
        return Json.error(ServiceError.FORBIDDEN_UPLOAD_MAX_SIZE);
    }

    /**
     * <h2>其他异常</h2>
     */
    @ExceptionHandler(value = {java.lang.Exception.class, RuntimeException.class})
    public Object otherExceptionHandle(@NotNull java.lang.Exception exception) {
        log.error(exception.getMessage());
        if (Configs.getServiceConfig().isDebug()) {
            log.error(ServiceError.SERVICE_ERROR.getMessage(), exception);
        }
        return Json.error(ServiceError.SERVICE_ERROR);
    }
}