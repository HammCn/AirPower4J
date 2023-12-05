package cn.hamm.airpower.request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>验证器工具类</h1>
 *
 * @author Hamm
 */
@SuppressWarnings("unused")
public class ValidateUtil {
    /**
     * 是否是数字
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isNumber(String value) {
        return validRegex(value, "^(-?\\d+)(\\.\\d+)?$");
    }

    /**
     * 是否是整数
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isInteger(String value) {
        return validRegex(value, "^-?[0-9]\\d*$");
    }

    /**
     * 是否是邮箱
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isEmail(String value) {
        return validRegex(value, "^[a-zA-Z0-9]+(\\.([a-zA-Z0-9]+)){0,}@[a-zA-Z0-9]+(\\.([a-zA-Z0-9]+)){1,}$");
    }

    /**
     * 是否是字母
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isLetter(String value) {
        return validRegex(value, "^[A-Za-z]+$");
    }

    /**
     * 是否是字母+数字
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isLetterOrNumber(String value) {
        return validRegex(value, "^[A-Za-z0-9]+$");
    }

    /**
     * 是否是中文汉字
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isChinese(String value) {
        return validRegex(value, "^[\\u4e00-\\u9fa5]{0,}$");
    }

    /**
     * 是否是手机号
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isMobilePhone(String value) {
        return validRegex(value, "^(\\+(\\d{1,4})){0,1}1[3-9](\\d{9})$");
    }

    /**
     * 是否是座机电话
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isTelPhone(String value) {
        return validRegex(value, "^(((0\\d{2,3})-){0,1}((\\d{7,8})|(400\\d{7})|(800\\d{7}))(-(\\d{1,4})){0,1})$");
    }

    /**
     * 是否是普通字符
     * 允许字符:
     * <p>
     * > @ # % a-z A-Z 0-9 汉字 _ + /
     * </p>
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isNormalCode(String value) {
        return validRegex(value, "^[@#%a-zA-Z0-9\\u4e00-\\u9fa5_\\-\\\\/\\\\+]+$");
    }

    /**
     * 是否是纯字母
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isOnlyLetter(String value) {
        return validRegex(value, "^[a-zA-Z]+$");
    }

    /**
     * 是否是纯字母和数字
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isOnlyNumberAndLetter(String value) {
        return validRegex(value, "^[0-9a-zA-Z]+$");
    }

    /**
     * 是否是自然数
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isNaturalNumber(String value) {
        return validRegex(value, "^[0-9]+((.)[0-9]+){0,1}$");
    }

    /**
     * 是否是自然整数
     *
     * @param value 参数
     * @return 验证结果
     */
    public static boolean isNaturalInteger(String value) {
        return validRegex(value, "^[0-9]+$");
    }

    /**
     * 正则校验
     *
     * @param value 参数
     * @param regex 正则
     * @return 验证结果
     */
    public static boolean validRegex(String value, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher emailMatcher = pattern.matcher(value);
        return emailMatcher.matches();
    }
}
