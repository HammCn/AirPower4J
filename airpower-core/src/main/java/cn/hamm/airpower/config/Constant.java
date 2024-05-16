package cn.hamm.airpower.config;

import org.springframework.http.HttpStatus;

/**
 * <h1>内置常量</h1>
 *
 * @author Hamm.cn
 */
public class Constant {
    /**
     * <h2>AirPower</h2>
     */
    public static final String AIRPOWER = "airpower";

    /**
     * <h2>创建时间字段名</h2>
     */
    public static final String CREATE_TIME_FIELD = "createTime";

    /**
     * <h2>修改时间字段名</h2>
     */
    public static final String UPDATE_TIME_FIELD = "updateTime";

    /**
     * <h2>主键ID字段名</h2>
     */
    public static final String ID = "id";

    /**
     * <h2>一分钟</h2>
     */
    public static final int SECOND_PER_MINUTE = 60;

    /**
     * <h2>一天24小时</h2>
     */
    public static final int HOUR_PER_DAY = 24;

    /**
     * <h2>一周7天</h2>
     */
    public static final int DAY_PER_WEEK = 7;

    /**
     * <h2>一小时的秒数</h2>
     */
    public static final int SECOND_PER_HOUR = SECOND_PER_MINUTE * SECOND_PER_MINUTE;

    /**
     * <h2>一天的秒数</h2>
     */
    public static final int SECOND_PER_DAY = SECOND_PER_HOUR * HOUR_PER_DAY;

    /**
     * <h2>本机IP地址</h2>
     */
    public static final String LOCAL_IP_ADDRESS = "127.0.0.1";

    /**
     * <h2>空字符串</h2>
     */
    public static final String EMPTY_STRING = "";

    /**
     * <h2>下划线</h2>
     */
    public final static String UNDERLINE = "_";

    /**
     * <h2>半角空格</h2>
     */
    public final static String SPACE = " ";

    /**
     * <h2>半角逗号</h2>
     */
    public final static String COMMA = ",";

    /**
     * <h2>半角冒号</h2>
     */
    public final static String COLON = ":";

    /**
     * <h2>分号</h2>
     */
    public final static String SEMICOLON = ";";

    /**
     * <h2>竖线</h2>
     */
    public final static String VERTICAL_LINE = "|";

    /**
     * <h2>斜线</h2>
     */
    public final static String SLASH = "/";

    /**
     * <h2>横线</h2>
     */
    public final static String LINE = "-";

    /**
     * <h2>等号</h2>
     */
    public final static String EQUAL = "=";

    /**
     * <h2>点</h2>
     */
    public final static String DOT = ".";

    /**
     * <h2>控制器后缀</h2>
     */
    public static final String CONTROLLER_SUFFIX = "Controller";

    /**
     * <h2>Key</h2>
     */
    public static final String KEY = "key";

    /**
     * <h2>Label</h2>
     */
    public static final String LABEL = "label";

    /**
     * <h2>Error</h2>
     */
    public static final String ERROR = "error";

    /**
     * <h2>Name</h2>
     */
    public static final String NAME = "name";

    /**
     * <h2>Code</h2>
     */
    public static final String CODE = "code";

    /**
     * <h2>Message</h2>
     */
    public static final String MESSAGE = "message";

    /**
     * <h2>Success</h2>
     */
    public static final String SUCCESS = "success";

    /**
     * <h2>Value</h2>
     */
    public static final String VALUE = "value";

    /**
     * <h2>Double 0</h2>
     */
    public static final double ZERO_DOUBLE = 0D;

    /**
     * <h2>Long 0</h2>
     */
    public static final long ZERO_LONG = 0L;

    /**
     * <h2>String 0</h2>
     */
    public static final String ZERO_STRING = "0";

    /**
     * <h2>String 1</h2>
     */
    public static final String ONE_STRING = "1";

    /**
     * <h2>String true</h2>
     */
    public static final String TRUE_STRING = "true";

    /**
     * <h2>String false</h2>
     */
    public static final String FALSE_STRING = "false";

    /**
     * <h2>JSON成功状态码</h2>
     */
    public static final int JSON_SUCCESS_CODE = HttpStatus.OK.value();

    /**
     * <h2>JSON成功信息</h2>
     */
    public static final String JSON_SUCCESS_MESSAGE = "操作成功";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String COOKIE = "Cookie";
}
