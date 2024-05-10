package cn.hamm.airpower.config;

/**
 * <h1>错误信息常量</h1>
 *
 * @author Hamm.cn
 */
public class MessageConstant {
    public static final String MISSING_ID_WHEN_UPDATE = "修改失败，请传入%s的ID!";
    public static final String DATA_MUST_NOT_NULL = "提交的数据不允许为空";
    public static final String MISSING_ID_WHEN_QUERY = "查询失败，请传入%s的ID！";
    public static final String QUERY_DATA_NOT_FOUND = "没有查询到ID为%s的%s";
    public static final String TARGET_DATA_EXIST = "%s(ID:%s)已经存在！";
    public static final String SUCCESS_TO_ADD = "添加成功";
    public static final String SUCCESS_TO_UPDATE = "修改成功";
    public static final String SUCCESS_TO_DELETE = "删除成功";
    public static final String SUCCESS_TO_DISABLE = "禁用成功";
    public static final String SUCCESS_TO_ENABLE = "启用成功";
    public static final String GET_PAGE_OR_GET_LIST_USE_ONLY = "该字段仅用于查询列表(分页和不分页)的接口作为时间段参数使用";
    public static final String SERVICE_MAINTAINING_AND_TRY_LATER = "服务短暂维护中,请稍后再试：）";
    public static final String BLOCK_SIZE_MUST_BE_GREATER_THAN_ZERO = "分段大小必须大于0";
    public static final String EXCEPTION_WHEN_GET_IP_ADDR = "获取ID地址异常";
    public static final String EXCEPTION_WHEN_REFLECT_FIELD = "反射操作属性失败";
    public static final String PASSWORD_CAN_NOT_BE_NULL = "密码不能为空";
    public static final String PASSWORD_SALT_CAN_NOT_BE_NULL = "盐不能为空";
    public static final String EXCEPTION_WHEN_MQTT_PUBLISH = "MQTT发布失败";
    public static final String MAIL_SERVER_CONFIG_MISSING = "未配置邮件服务的信息";
    public static final String FAILED_TO_LOAD_CURRENT_USER_INFO = "获取当前用户信息失败";
    public static final String EXCEPTION_WHEN_JSON_PARSE = "JSON字符串转对象失败";
    public static final String EXCEPTION_WHEN_JSON_TO_STRING = "对象转JSON字符串失败";
    public static final String ONLY_CONTENT_TYPE_JSON_SUPPORTED = "%s 不被支持，请使用JSON请求";
    public static final String MISSING_FIELD_IN_DATABASE = "数据库缺少字段%s";
    public static final String PARAM_INVALID_MAY_BE_NOT_JSON = "请求参数格式不正确,请检查是否接口支持的JSON";
    public static final String REQUEST_METHOD_NOT_SUPPORTED = "%s 不被支持，请使用 %s 方法请求";
    public static final String MESSAGE_AND_DESCRIPTION = "%s (%s)";
    public static final String ACCESS_DENIED = "你无权访问 " + MESSAGE_AND_DESCRIPTION;
}
