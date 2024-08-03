package cn.hamm.airpower.config;

import org.springframework.http.HttpStatus;

/**
 * <h1>内置常量</h1>
 *
 * @author Hamm.cn
 */
public class Constant {
    /**
     * <h2>{@code AirPower}</h2>
     */
    public static final String AIRPOWER = "airpower";

    /**
     * <h2>创建时间字段名 {@code createTime}</h2>
     */
    public static final String CREATE_TIME_FIELD = "createTime";

    /**
     * <h2>修改时间字段名 {@code updateTime}</h2>
     */
    public static final String UPDATE_TIME_FIELD = "updateTime";

    /**
     * <h2>主键 {@code ID} 字段名</h2>
     */
    public static final String ID = "id";

    /**
     * <h2>一分钟 {@code 60} 秒</h2>
     */
    public static final int SECOND_PER_MINUTE = 60;

    /**
     * <h2>一天 {@code 24} 小时</h2>
     */
    public static final int HOUR_PER_DAY = 24;

    /**
     * <h2>一周 {@code 7} 天</h2>
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
     * <h2>本机 {@code IP} 地址</h2>
     */
    public static final String LOCAL_IP_ADDRESS = "127.0.0.1";

    /**
     * <h2>空字符串 {@code }</h2>
     */
    public static final String EMPTY_STRING = "";

    /**
     * <h2>下划线 {@code _}</h2>
     */
    public final static String UNDERLINE = "_";

    /**
     * <h2>半角空格 {@code  }</h2>
     */
    public final static String SPACE = " ";

    /**
     * <h2>半角逗号 {@code ,}</h2>
     */
    public final static String COMMA = ",";

    /**
     * <h2>半角冒号 {@code :}</h2>
     */
    public final static String COLON = ":";

    /**
     * <h2>分号 {@code ;}</h2>
     */
    public final static String SEMICOLON = ";";

    /**
     * <h2>竖线 {@code |}</h2>
     */
    public final static String VERTICAL_LINE = "|";

    /**
     * <h2>斜线 {@code /}</h2>
     */
    public final static String SLASH = "/";

    /**
     * <h2>横线 {@code -}</h2>
     */
    public final static String LINE = "-";

    /**
     * <h2>等号 {@code =}</h2>
     */
    public final static String EQUAL = "=";

    /**
     * <h2>点 {@code .}</h2>
     */
    public final static String DOT = ".";

    /**
     * <h2>点的正则</h2>
     */
    public static final String DOT_REGEX = "\\.";

    /**
     * <h2>星号 {@code *}</h2>
     */
    public final static String ASTERISK = "*";

    /**
     * <h2>百分号 {@code %}</h2>
     */
    public static final String PERCENT = "%";

    /**
     * <h2>控制器后缀 {@code Controller}</h2>
     */
    public static final String CONTROLLER_SUFFIX = "Controller";

    /**
     * <h2>{@code get}</h2>
     */
    public static final String GET = "get";

    /**
     * <h2>{@code Key}</h2>
     */
    public static final String KEY = "key";

    /**
     * <h2>{@code Label}</h2>
     */
    public static final String LABEL = "label";

    /**
     * <h2>{@code Error}</h2>
     */
    public static final String ERROR = "error";

    /**
     * <h2>{@code Name}</h2>
     */
    public static final String NAME = "name";

    /**
     * <h2>{@code Code}</h2>
     */
    public static final String CODE = "code";

    /**
     * <h2>{@code Message}</h2>
     */
    public static final String MESSAGE = "message";

    /**
     * <h2>{@code Success}</h2>
     */
    public static final String SUCCESS = "success";

    /**
     * <h2>{@code Value}</h2>
     */
    public static final String VALUE = "value";

    /**
     * <h2>Double {@code 0D}</h2>
     */
    public static final double ZERO_DOUBLE = 0D;

    /**
     * <h2>Long {@code 0L}</h2>
     */
    public static final long ZERO_LONG = 0L;

    /**
     * <h2>Int {@code 0}</h2>
     */
    public static final int ZERO_INT = 0;

    /**
     * <h2>String {@code "0"}</h2>
     */
    public static final String ZERO_STRING = "0";

    /**
     * <h2>String {@code "1"}</h2>
     */
    public static final String ONE_STRING = "1";

    /**
     * <h2>String {@code "true"}</h2>
     */
    public static final String TRUE_STRING = "true";

    /**
     * <h2>String {@code "false"}</h2>
     */
    public static final String FALSE_STRING = "false";

    /**
     * <h2>{@code JSON} 成功状态码</h2>
     */
    public static final int JSON_SUCCESS_CODE = HttpStatus.OK.value();

    /**
     * <h2>{@code JSON} 成功信息</h2>
     */
    public static final String JSON_SUCCESS_MESSAGE = "操作成功";

    /**
     * <h2>{@code ContentType}</h2>
     */
    public static final String CONTENT_TYPE = "Content-Type";

    /**
     * <h2>{@code Cookie}</h2>
     */
    public static final String COOKIE = "Cookie";

    /**
     * <h2>毫秒转秒</h2>
     */
    public static final int MILLISECONDS_PER_SECOND = 1000;

    /**
     * <h2>换行</h2>
     */
    public static final String LINE_BREAK = "\n";

    /**
     * <h2>{@code TAB}</h2>
     */
    public static final String TAB = "\t";

    /**
     * <h2>{@code "是"}</h2>
     */
    public static final String YES = "是";

    /**
     * <h2>{@code "否"}</h2>
     */
    public static final String NO = "否";
}
