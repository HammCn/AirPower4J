package cn.hamm.airpower.util;

import cn.hamm.airpower.annotation.Desensitize;
import cn.hamm.airpower.config.Constant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.stream.IntStream;

/**
 * <h1>字符串处理类</h1>
 *
 * @author Hamm.cn
 */
@Component
public class StringUtil {
    /**
     * <h2>{@code IPV4} 的块长度</h2>
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
     * <h2>{@code IPv4} 地址脱敏</h2>
     *
     * @param ipv4   {@code IPv4} 地址
     * @param symbol 符号
     * @return 脱敏后的 {@code IPv4} 地址
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
     * <h2>{@code IPv4} 地址脱敏</h2>
     *
     * @param ipv4 {@code IPv4} 地址
     * @return 脱敏后的 {@code IPv4} 地址
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
    public final @NotNull String desensitize(
            @NotNull String valueString, Desensitize.@NotNull Type type, int head, int tail, String symbol
    ) {
        final StringUtil stringUtil = Utils.getStringUtil();
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
                return stringUtil.desensitizeIpv4Address(valueString, symbol);
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
        return stringUtil.replace(valueString, head, tail, symbol);
    }
}
