package cn.hamm.airpower.util;

import cn.hamm.airpower.annotation.Desensitize;
import cn.hamm.airpower.config.Constant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <h1>字符串处理类</h1>
 *
 * @author Hamm.cn
 */
@Component
public class StringUtil {
    /**
     * <h2>IPV4的块长度</h2>
     */
    private static final int IPV4_PART_COUNT = 4;

    /**
     * <h2>字符串替换</h2>
     *
     * @param text   原始字符串
     * @param head   头部保留长度
     * @param tail   尾部保留长度
     * @param symbol 中间替换的单个符号
     * @return 替换后的字符串
     */
    public final @NotNull String replace(String text, int head, int tail, String symbol) {
        if (head < 0 || tail < 0) {
            return text;
        }
        if (head + tail >= text.length()) {
            return text;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (i >= head && i <= text.length() - tail - 1) {
                stringBuilder.append(symbol);
            } else {
                stringBuilder.append(text.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    /**
     * <h2>IPv4地址脱敏</h2>
     *
     * @param ipv4   IPv4地址
     * @param symbol 符号
     * @return 脱敏后的IPv4地址
     */
    public final @NotNull String desensitizeIpv4Address(@NotNull String ipv4, String symbol) {
        if (!StringUtils.hasText(symbol)) {
            symbol = Constant.ASTERISK;
        }
        String[] strings = ipv4.split(Constant.DOT_REGEX);
        if (strings.length != IPV4_PART_COUNT) {
            return ipv4;
        }
        strings[1] = symbol + symbol + symbol;
        strings[2] = strings[1];
        return String.join(Constant.DOT, strings);
    }

    /**
     * <h2>IPv4地址脱敏</h2>
     *
     * @param ipv4 IPv4地址
     * @return 脱敏后的IPv4地址
     */
    public final @NotNull String desensitizeIpv4Address(@NotNull String ipv4) {
        return desensitizeIpv4Address(ipv4, Constant.ASTERISK);
    }


    /**
     * <h2>文本脱敏</h2>
     *
     * @param text 原始文本
     * @param type 脱敏类型
     * @param head 头部保留
     * @param tail 尾部保留
     * @return 脱敏后的文本
     */
    @Contract(pure = true)
    public final @NotNull String desensitize(@NotNull String text, Desensitize.Type type, int head, int tail) {
        return desensitize(text, type, head, tail, Constant.ASTERISK);
    }

    /**
     * <h2>文本脱敏</h2>
     *
     * @param valueString 原始文本
     * @param type        脱敏类型
     * @param head        头部保留
     * @param tail        尾部保留
     * @param symbol      脱敏符号
     * @return 脱敏后的文本
     */
    @Contract(pure = true)
    public final @NotNull String desensitize(@NotNull String valueString, Desensitize.@NotNull Type type, int head, int tail, String symbol) {
        switch (type) {
            case CHINESE_NAME:
                head = Math.max(1, head);
                tail = Math.max(1, tail);
                if (valueString.length() <= head + tail) {
                    tail = 0;
                }
                break;
            case BANK_CARD:
                head = Math.max(4, head);
                tail = Math.max(4, tail);
                break;
            case ID_CARD:
                head = Math.max(6, head);
                tail = Math.max(4, tail);
                break;
            case MOBILE:
                head = Math.max(3, head);
                tail = Math.max(4, tail);
                break;
            case EMAIL:
                head = 2;
                tail = 2;
                break;
            case IP_V4:
                return Utils.getStringUtil().desensitizeIpv4Address(valueString, symbol);
            case ADDRESS:
                head = Math.max(3, head);
                tail = Math.max(0, tail);
                break;
            case TELEPHONE:
                //noinspection AlibabaUndefineMagicConstant
                if (valueString.length() <= 8) {
                    head = Math.max(2, head);
                    tail = Math.max(2, tail);
                } else {
                    head = Math.max(4, head);
                    tail = Math.max(4, tail);
                }
                break;
            case CAR_NUMBER:
                head = Math.max(2, head);
                tail = Math.max(1, tail);
                break;
            default:
        }
        return Utils.getStringUtil().replace(valueString, head, tail, symbol);
    }
}
