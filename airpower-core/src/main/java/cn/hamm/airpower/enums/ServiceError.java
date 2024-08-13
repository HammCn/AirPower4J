package cn.hamm.airpower.enums;

import cn.hamm.airpower.interfaces.IDictionary;
import cn.hamm.airpower.interfaces.IException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;

/**
 * <h1>系统错误代码字典</h1>
 *
 * @author Hamm.cn
 * @apiNote 如需自定义业务异常，可自行创建一个新业务异常枚举类并实现 {@link IException} 接口即可。
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
@Getter
@AllArgsConstructor
public enum ServiceError implements IException, IDictionary {
    CONTINUE(201, "请继续"),

    UPGRADE_CLIENT_NECESSARY(301, "请更新客户端"),
    UPGRADE_CLIENT_OPTIONAL(302, "建议升级客户端"),

    // 400 请求参数代码
    PARAM_MISSING(4001, "缺少参数"),
    PARAM_INVALID(4002, "无效的参数"),
    INVALID_APP_KEY(4003, "传入的AppKey无效"),
    SIGNATURE_INVALID(4004, "签名无效"),
    REPEAT_REQUEST(4005, "重复的请求"),
    TIMESTAMP_INVALID(4006, "请求时间不在允许范围内"),

    // 401 需要登录代码
    UNAUTHORIZED(401, "获取身份信息失败,请重新登录!"),

    // 403 无权限代码
    FORBIDDEN(403, "无权限"),
    FORBIDDEN_EXIST(4031, "唯一约束,无权重复"),
    FORBIDDEN_EDIT(4032, "无权修改"),
    FORBIDDEN_DELETE(4033, "无权删除"),
    FORBIDDEN_DELETE_USED(4034, "数据正在使用中,无法被删除!"),
    FORBIDDEN_UPLOAD_MAX_SIZE(4035, "上传的文件大小超过最大限制"),
    FORBIDDEN_DISABLED(4036, "ID为%s的%s已被禁用"),
    OPEN_APP_DISABLED(4037, "当前应用已被禁用"),

    // 404 没有查到数据代码
    DATA_NOT_FOUND(404, "没有查到相关的数据"),

    REQUEST_METHOD_UNSUPPORTED(405, "不支持的请求方法"),

    REQUEST_CONTENT_TYPE_UNSUPPORTED(415, "不支持的数据类型"),

    // 500 服务基础代码
    SERVICE_ERROR(500, "服务发生错误,请稍后再试"),
    DECRYPT_DATA_FAIL(5001, "数据解密失败"),
    ENCRYPT_DATA_FAIL(5002, "数据加密失败"),
    JSON_DECODE_FAIL(5003, "JSON解码失败"),

    API_SERVICE_UNSUPPORTED(501, "请求的接口暂未实现"),

    // 502 内部错误代码

    // 5021 数据库相关代码
    DATABASE_ERROR(5021, "数据库服务连接失败，请稍后再试"),
    DATABASE_UNKNOWN_FIELD(50211, "不支持的数据库字段"),
    DATABASE_TABLE_OR_FIELD_ERROR(50212, "数据库表或字段信息异常"),

    // 5022 Redis相关代码
    REDIS_ERROR(5022, "REDIS服务连接失败，请稍后再试"),

    // 5023 邮件相关代码
    EMAIL_ERROR(5023, "发送邮件可能出现异常,请检查收件箱"),

    ;

    private final int code;
    private final String message;

    @Contract(pure = true)
    @Override
    public int getKey() {
        return code;
    }

    @Contract(pure = true)
    @Override
    public String getLabel() {
        return message;
    }
}
