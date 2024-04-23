package cn.hamm.airpower.request;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>验证器工具类</h1>
 *
 * @author Hamm.cn
 */
public class ValidateUtil {
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
     * <h2>是否是数字</h2>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isNumber(String value) {
        return validRegex(value, NUMBER);
    }

    /**
     * <h2>是否是整数</h2>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isInteger(String value) {
        return validRegex(value, INTEGER);
    }

    /**
     * <h2>是否是邮箱</h2>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isEmail(String value) {
        return validRegex(value, EMAIL);
    }


    /**
     * <h2>是否是字母</h2>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isLetter(String value) {
        return validRegex(value, LETTER);
    }

    /**
     * <h2>是否是字母+数字</h2>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isLetterOrNumber(String value) {
        return validRegex(value, LETTER_OR_NUMBER);
    }

    /**
     * <h2>是否是中文汉字</h2>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isChinese(String value) {
        return validRegex(value, CHINESE);
    }

    /**
     * <h2>是否是手机号</h2>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isMobilePhone(String value) {
        return validRegex(value, MOBILE_PHONE);
    }

    /**
     * <h2>是否是座机电话</h2>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isTelPhone(String value) {
        return validRegex(value, TEL_PHONE);
    }

    /**
     * <h2>是否是普通字符</h2>
     * 允许字符:
     * <p>
     * > @ # % a-z A-Z 0-9 汉字 _ + /
     * </p>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isNormalCode(String value) {
        return validRegex(value, NORMAL_CODE);
    }

    /**
     * <h2>是否是纯字母和数字</h2>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isOnlyNumberAndLetter(String value) {
        return validRegex(value, NUMBER_OR_LETTER);
    }

    /**
     * <h2>是否是自然数</h2>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isNaturalNumber(String value) {
        return validRegex(value, NATURAL_NUMBER);
    }

    /**
     * <h2>是否是自然整数</h2>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isNaturalInteger(String value) {
        return validRegex(value, NATURAL_INTEGER);
    }

    /**
     * <h2>正则校验</h2>
     *
     * @param value   参数
     * @param pattern 正则
     * @return 验证结果
     */
    public static boolean validRegex(String value, @NotNull Pattern pattern) {
        Matcher emailMatcher = pattern.matcher(value);
        return emailMatcher.matches();
    }
}
