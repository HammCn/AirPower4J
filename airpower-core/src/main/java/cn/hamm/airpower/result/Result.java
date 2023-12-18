package cn.hamm.airpower.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h1>系统异常代码字典</h1>
 *
 * @author Hamm
 */
@Getter
@SuppressWarnings({"AlibabaEnumConstantsMustHaveComment", "unused"})
@AllArgsConstructor
public enum Result implements IResult {
    SUCCESS(0.200, "成功"),
    CONTINUE(0.201, "请继续"),

    // 400 请求参数代码
    PARAM_MISSING(0.4001, "缺少参数"),
    PARAM_INVALID(0.4002, "无效的参数"),

    // 401 需要登录代码
    UNAUTHORIZED(0.401, "身份验证失败,请重新登录"),

    // 403 无权限代码
    FORBIDDEN(0.403, "无权限"),
    FORBIDDEN_EXIST(0.4031, "唯一约束,无权重复"),
    FORBIDDEN_EDIT(0.4032, "无权修改"),
    FORBIDDEN_DELETE(0.4033, "无权删除"),
    FORBIDDEN_DELETE_USED(0.4034, "无权删除被使用中的数据"),

    // 404 没有查到数据代码
    NOT_FOUND(0.404, "没有查到相关的数据"),

    REQUEST_METHOD_UNSUPPORTED(0.405, "不支持的请求方法"),

    REQUEST_CONTENT_TYPE_UNSUPPORTED(0.415, "不支持的数据类型"),

    // 500 服务基础代码
    ERROR(0.5, "服务发生错误,请稍后再试"),
    SERVICE_NOT_FOUND(0.501, "请求的接口暂未实现"),

    // 502 内部错误代码

    // 5021 数据库相关代码
    DATABASE_ERROR(0.5021, "数据库服务连接失败，请稍后再试"),
    DATABASE_UNKNOWN_FIELD(0.50211, "不支持的数据库字段"),
    DATABASE_TABLE_OR_FIELD_ERROR(0.50212, "数据库表或字段信息异常"),

    // 5022 Redis相关代码
    REDIS_ERROR(0.5022, "REDIS服务连接失败，请稍后再试"),

    // 5023 邮件相关代码
    EMAIL_ERROR(0.5023, "发送邮件可能出现异常,请检查收件箱"),

    ;


    private final Double code;
    private final String message;

    @Override
    public int getCode() {
        return (int) Math.round(this.code * 1000000);
    }
}
