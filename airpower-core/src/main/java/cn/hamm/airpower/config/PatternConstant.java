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
     * <h2>数字</h2>
     */
    public static final Pattern NUMBER = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");

    /**
     * <h2>字母</h2>
     */
    public static final Pattern LETTER = Pattern.compile("^[A-Za-z]+$");

    /**
     * <h2>整数</h2>
     */
    public static final Pattern INTEGER = Pattern.compile("^-?[0-9]\\d*$");

    /**
     * <h2>邮箱</h2>
     */
    public static final Pattern EMAIL = Pattern.compile(
            "^[a-zA-Z0-9]+(\\.([a-zA-Z0-9]+))*@[a-zA-Z0-9]+(\\.([a-zA-Z0-9]+))+$"
    );

    /**
     * <h2>字母或数字</h2>
     */
    public static final Pattern LETTER_OR_NUMBER = Pattern.compile("^[A-Za-z0-9]+$");

    /**
     * <h2>中文</h2>
     */
    public static final Pattern CHINESE = Pattern.compile("^[\\u4e00-\\u9fa5]*$");

    /**
     * <h2>手机</h2>
     */
    public static final Pattern MOBILE_PHONE = Pattern.compile("^(\\+(\\d{1,4}))?1[3-9](\\d{9})$");

    /**
     * <h2>座机电话</h2>
     */
    public static final Pattern TEL_PHONE = Pattern.compile(
            "^(((0\\d{2,3})-)?((\\d{7,8})|(400\\d{7})|(800\\d{7}))(-(\\d{1,4}))?)$"
    );

    /**
     * <h2>普通字符</h2>
     */
    public static final Pattern NORMAL_CODE = Pattern.compile("^[@#%a-zA-Z0-9\\u4e00-\\u9fa5_\\-\\\\/+]+$");

    /**
     * <h2>数字或字母</h2>
     */
    public static final Pattern NUMBER_OR_LETTER = Pattern.compile("^[0-9a-zA-Z]+$");

    /**
     * <h2>自然数</h2>
     */
    public static final Pattern NATURAL_NUMBER = Pattern.compile("^[0-9]+((.)[0-9]+)?$");

    /**
     * <h2>自然整数</h2>
     */
    public static final Pattern NATURAL_INTEGER = Pattern.compile("^[0-9]+$");


    /**
     * <h2>禁止外部实例化</h2>
     */
    @Contract(pure = true)
    private PatternConstant() {
    }
}
