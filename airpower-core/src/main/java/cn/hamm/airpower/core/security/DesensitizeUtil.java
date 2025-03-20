package cn.hamm.airpower.core.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import java.util.stream.IntStream;

import static cn.hamm.airpower.core.constant.Constant.*;

/**
 * <h1>字符串脱敏处理工具类</h1>
 *
 * @author Hamm.cn
 */
public class DesensitizeUtil {
    /**
     * <h3>{@code IPV4} 的块长度</h3>
     */
    private static final int IPV4_PART_COUNT = 4;

    /**
     * <h3>禁止外部实例化</h3>
     */
    @Contract(pure = true)
    private DesensitizeUtil() {
    }

    /**
     * <h3>字符串替换</h3>
     *
     * @param text   原始字符串
     * @param head   头部保留长度
     * @param tail   尾部保留长度
     * @param symbol 中间替换的单个符号
     * @return 替换后的字符串
     */
    public static @NotNull String replace(String text, int head, int tail, String symbol) {
        if (head < 0 || tail < 0 || head + tail >= text.length()) {
            return text;
        }
        StringBuilder stringBuilder = new StringBuilder();
        IntStream.range(0, text.length()).forEach(i -> {
            if (i >= head && i <= text.length() - tail - 1) {
                stringBuilder.append(symbol);
            } else {
                stringBuilder.append(text.charAt(i));
            }
        });
        return stringBuilder.toString();
    }

    /**
     * <h3>{@code IPv4} 地址脱敏</h3>
     *
     * @param ipv4   {@code IPv4} 地址
     * @param symbol 符号
     * @return 脱敏后的 {@code IPv4} 地址
     */
    public static @NotNull String desensitizeIpv4Address(@NotNull String ipv4, String symbol) {
        if (!StringUtils.hasText(symbol)) {
            symbol = STRING_ASTERISK;
        }
        String[] strings = ipv4.split(REGEX_DOT);
        if (strings.length != IPV4_PART_COUNT) {
            return ipv4;
        }
        strings[1] = symbol + symbol + symbol;
        strings[2] = strings[1];
        return String.join(STRING_DOT, strings);
    }

    /**
     * <h3>{@code IPv4} 地址脱敏</h3>
     *
     * @param ipv4 {@code IPv4} 地址
     * @return 脱敏后的 {@code IPv4} 地址
     */
    public static @NotNull String desensitizeIpv4Address(@NotNull String ipv4) {
        return desensitizeIpv4Address(ipv4, STRING_ASTERISK);
    }

    /**
     * <h3>文本脱敏</h3>
     *
     * @param text 原始文本
     * @param type 脱敏类型
     * @param head 头部保留
     * @param tail 尾部保留
     * @return 脱敏后的文本
     */
    @Contract(pure = true)
    public static @NotNull String desensitize(@NotNull String text, Type type, int head, int tail) {
        return desensitize(text, type, head, tail, STRING_ASTERISK);
    }

    /**
     * <h3>文本脱敏</h3>
     *
     * @param valueString 原始文本
     * @param type        脱敏类型
     * @param head        头部保留
     * @param tail        尾部保留
     * @param symbol      脱敏符号
     * @return 脱敏后的文本
     */
    @Contract(pure = true)
    public static @NotNull String desensitize(
            @NotNull String valueString, @NotNull Type type, int head, int tail, String symbol
    ) {
        switch (type) {
            case BANK_CARD,
                 ID_CARD,
                 MOBILE,
                 ADDRESS,
                 CAR_NUMBER,
                 EMAIL -> {
                head = Math.max(type.getMinHead(), head);
                tail = Math.max(type.getMinTail(), tail);
            }
            case IP_V4 -> {
                return desensitizeIpv4Address(valueString, symbol);
            }
            case CHINESE_NAME -> {
                head = Math.max(type.getMinHead(), head);
                tail = Math.max(type.getMinTail(), tail);
                if (valueString.length() <= head + tail) {
                    tail = 0;
                }
            }
            case TELEPHONE -> {
                // 包含区号 前后各留4 不包含则各留2
                int isContainRegionCode = valueString.length() > 8 ? 4 : 2;
                head = Math.max(isContainRegionCode, head);
                tail = Math.max(isContainRegionCode, tail);
            }
            default -> {
            }
        }
        return replace(valueString, head, tail, symbol);
    }

    /**
     * <h3>脱敏方式</h3>
     */
    @AllArgsConstructor
    @Getter
    public
    enum Type {
        /**
         * <h3>座机号码</h3>
         */
        TELEPHONE(0, 0),

        /**
         * <h3>手机号码</h3>
         */
        MOBILE(3, 4),

        /**
         * <h3>身份证号</h3>
         */
        ID_CARD(6, 4),

        /**
         * <h3>银行卡号</h3>
         */
        BANK_CARD(4, 4),

        /**
         * <h3>车牌号</h3>
         */
        CAR_NUMBER(2, 1),

        /**
         * <h3>邮箱</h3>
         */
        EMAIL(2, 2),

        /**
         * <h3>中文名</h3>
         */
        CHINESE_NAME(1, 1),

        /**
         * <h3>地址</h3>
         */
        ADDRESS(3, 0),

        /**
         * <h3><code>IPv4</code>地址</h3>
         */
        IP_V4(0, 0),

        /**
         * <h3>自定义</h3>
         */
        CUSTOM(0, 0);

        /**
         * <h3>开始至少保留</h3>
         */
        private final int minHead;

        /**
         * <h3>结束至少保留</h3>
         */
        private final int minTail;
    }
}
