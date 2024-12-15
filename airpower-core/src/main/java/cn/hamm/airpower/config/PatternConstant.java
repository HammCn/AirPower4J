package cn.hamm.airpower.config;

import org.jetbrains.annotations.Contract;

import java.util.regex.Pattern;

/**
 * <h1>正则常量</h1>
 *
 * @author Hamm.cn
 */
public class PatternConstant {
    /**
     * <h3>数字</h3>
     */
    public static final Pattern NUMBER = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");

    /**
     * <h3>字母</h3>
     */
    public static final Pattern LETTER = Pattern.compile("^[A-Za-z]+$");

    /**
     * <h3>整数</h3>
     */
    public static final Pattern INTEGER = Pattern.compile("^-?[0-9]\\d*$");

    /**
     * <h3>邮箱</h3>
     */
    public static final Pattern EMAIL = Pattern.compile(
            "^[a-zA-Z0-9]+(\\.([a-zA-Z0-9]+))*@[a-zA-Z0-9]+(\\.([a-zA-Z0-9]+))+$"
    );

    /**
     * <h3>字母或数字</h3>
     */
    public static final Pattern LETTER_OR_NUMBER = Pattern.compile("^[A-Za-z0-9]+$");

    /**
     * <h3>中文</h3>
     */
    public static final Pattern CHINESE = Pattern.compile("^[\\u4e00-\\u9fa5]*$");

    /**
     * <h3>手机</h3>
     */
    public static final Pattern MOBILE_PHONE = Pattern.compile("^(\\+(\\d{1,4}))?1[3-9](\\d{9})$");

    /**
     * <h3>座机电话</h3>
     */
    public static final Pattern TEL_PHONE = Pattern.compile(
            "^(((0\\d{2,3})-)?((\\d{7,8})|(400\\d{7})|(800\\d{7}))(-(\\d{1,4}))?)$"
    );

    /**
     * <h3>普通字符</h3>
     */
    public static final Pattern NORMAL_CODE = Pattern.compile("^[@#%a-zA-Z0-9\\u4e00-\\u9fa5_\\-\\\\/+]+$");

    /**
     * <h3>数字或字母</h3>
     */
    public static final Pattern NUMBER_OR_LETTER = Pattern.compile("^[0-9a-zA-Z]+$");

    /**
     * <h3>自然数</h3>
     */
    public static final Pattern NATURAL_NUMBER = Pattern.compile("^[0-9]+((.)[0-9]+)?$");

    /**
     * <h3>自然整数</h3>
     */
    public static final Pattern NATURAL_INTEGER = Pattern.compile("^[0-9]+$");


    /**
     * <h3>禁止外部实例化</h3>
     */
    @Contract(pure = true)
    private PatternConstant() {
    }
}
