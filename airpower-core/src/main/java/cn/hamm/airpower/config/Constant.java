package cn.hamm.airpower.config;

import org.jetbrains.annotations.Contract;
import org.springframework.http.HttpStatus;

/**
 * <h1>内置常量</h1>
 *
 * @author Hamm.cn
 */
public class Constant {
    /**
     * <h3>{@code AirPower}</h3>
     */
    public static final String AIRPOWER = "airpower";

    /**
     * <h3>创建时间字段名 {@code createTime}</h3>
     */
    public static final String CREATE_TIME_FIELD = "createTime";

    /**
     * <h3>修改时间字段名 {@code updateTime}</h3>
     */
    public static final String UPDATE_TIME_FIELD = "updateTime";

    /**
     * <h3>主键 {@code ID} 字段名</h3>
     */
    public static final String ID = "id";

    /**
     * <h3>本机 {@code IP} 地址</h3>
     */
    public static final String LOCAL_IP_ADDRESS = "127.0.0.1";

    /**
     * <h3>空字符串 {@code }</h3>
     */
    public static final String EMPTY_STRING = "";

    /**
     * <h3>下划线 {@code _}</h3>
     */
    public final static String UNDERLINE = "_";

    /**
     * <h3>半角空格 {@code  }</h3>
     */
    public final static String SPACE = " ";

    /**
     * <h3>半角逗号 {@code ,}</h3>
     */
    public final static String COMMA = ",";

    /**
     * <h3>半角冒号 {@code :}</h3>
     */
    public final static String COLON = ":";

    /**
     * <h3>分号 {@code ;}</h3>
     */
    public final static String SEMICOLON = ";";

    /**
     * <h3>竖线 {@code |}</h3>
     */
    public final static String VERTICAL_LINE = "|";

    /**
     * <h3>斜线 {@code /}</h3>
     */
    public final static String SLASH = "/";

    /**
     * <h3>横线 {@code -}</h3>
     */
    public final static String LINE = "-";

    /**
     * <h3>等号 {@code =}</h3>
     */
    public final static String EQUAL = "=";

    /**
     * <h3>点 {@code .}</h3>
     */
    public final static String DOT = ".";

    /**
     * <h3>点的正则</h3>
     */
    public static final String DOT_REGEX = "\\.";

    /**
     * <h3>星号 {@code *}</h3>
     */
    public final static String ASTERISK = "*";

    /**
     * <h3>问号 {@code ?}</h3>
     */
    public final static String QUESTION = "?";

    /**
     * <h3>井号 {@code #}</h3>
     */
    public final static String SHARP = "#";

    /**
     * <h3>@ {@code @}</h3>
     */
    public final static String AT = "@";

    /**
     * <h3>{@code And &}</h3>
     */
    public static final String AND = "&";

    /**
     * <h3>百分号 {@code %}</h3>
     */
    public static final String PERCENT = "%";

    /**
     * <h3>控制器后缀 {@code Controller}</h3>
     */
    public static final String CONTROLLER_SUFFIX = "Controller";

    /**
     * <h3>{@code get}</h3>
     */
    public static final String GET = "get";

    /**
     * <h3>{@code Key}</h3>
     */
    public static final String KEY = "key";

    /**
     * <h3>{@code Label}</h3>
     */
    public static final String LABEL = "label";

    /**
     * <h3>{@code Error}</h3>
     */
    public static final String ERROR = "error";

    /**
     * <h3>{@code Name}</h3>
     */
    public static final String NAME = "name";

    /**
     * <h3>{@code Code}</h3>
     */
    public static final String CODE = "code";

    /**
     * <h3>{@code Message}</h3>
     */
    public static final String MESSAGE = "message";

    /**
     * <h3>{@code Success}</h3>
     */
    public static final String SUCCESS = "success";

    /**
     * <h3>{@code Value}</h3>
     */
    public static final String VALUE = "value";

    /**
     * <h3>Double {@code 0D}</h3>
     */
    public static final double ZERO_DOUBLE = 0D;

    /**
     * <h3>Long {@code 0L}</h3>
     */
    public static final long ZERO_LONG = 0L;

    /**
     * <h3>Int {@code 0}</h3>
     */
    public static final int ZERO_INT = 0;

    /**
     * <h3>String {@code "0"}</h3>
     */
    public static final String ZERO_STRING = "0";

    /**
     * <h3>String {@code "1"}</h3>
     */
    public static final String ONE_STRING = "1";

    /**
     * <h3>String {@code "true"}</h3>
     */
    public static final String TRUE_STRING = "true";

    /**
     * <h3>String {@code "false"}</h3>
     */
    public static final String FALSE_STRING = "false";

    /**
     * <h3>{@code JSON} 成功状态码</h3>
     */
    public static final int JSON_SUCCESS_CODE = HttpStatus.OK.value();

    /**
     * <h3>{@code JSON} 成功信息</h3>
     */
    public static final String JSON_SUCCESS_MESSAGE = "操作成功";

    /**
     * <h3>{@code ContentType}</h3>
     */
    public static final String CONTENT_TYPE = "Content-Type";

    /**
     * <h3>{@code Cookie}</h3>
     */
    public static final String COOKIE = "Cookie";

    /**
     * <h3>换行</h3>
     */
    public static final String LINE_BREAK = "\n";

    /**
     * <h3>{@code TAB}</h3>
     */
    public static final String TAB = "\t";

    /**
     * <h3>{@code "是"}</h3>
     */
    public static final String YES = "是";

    /**
     * <h3>{@code "否"}</h3>
     */
    public static final String NO = "否";

    /**
     * <h3>{@code unknown}</h3>
     */
    public static final String UNKNOWN = "unknown";

    /**
     * <h3>禁止外部实例化</h3>
     */
    @Contract(pure = true)
    private Constant() {
    }
}
