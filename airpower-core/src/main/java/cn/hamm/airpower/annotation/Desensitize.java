package cn.hamm.airpower.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.*;

/**
 * <h1>标记字段在API输出时自动脱敏</h1>
 *
 * @author Hamm.cn
 * @apiNote 如需标记不脱敏的接口，可使用 {@link DesensitizeExclude}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitize {
    /**
     * <h2>脱敏类型</h2>
     */
    Type value();

    /**
     * <h2>开始保留位数</h2>
     */
    int head() default 0;

    /**
     * <h2>结束保留位数</h2>
     */
    int tail() default 0;

    /**
     * <h2>脱敏符号</h2>
     */
    String symbol() default "*";

    /**
     * <h2>脱敏方式</h2>
     */
    @AllArgsConstructor
    @Getter
    enum Type {
        /**
         * <h2>座机号码</h2>
         */
        TELEPHONE(0, 0),

        /**
         * <h2>手机号码</h2>
         */
        MOBILE(3, 4),

        /**
         * <h2>身份证号</h2>
         */
        ID_CARD(6, 4),

        /**
         * <h2>银行卡号</h2>
         */
        BANK_CARD(4, 4),

        /**
         * <h2>车牌号</h2>
         */
        CAR_NUMBER(2, 1),

        /**
         * <h2>邮箱</h2>
         */
        EMAIL(2, 2),

        /**
         * <h2>中文名</h2>
         */
        CHINESE_NAME(1, 1),

        /**
         * <h2>地址</h2>
         */
        ADDRESS(3, 0),

        /**
         * <h2><code>IPv4</code>地址</h2>
         */
        IP_V4(0, 0),

        /**
         * <h2>自定义</h2>
         */
        CUSTOM(0, 0);

        /**
         * <h2>开始至少保留</h2>
         */
        private final int minHead;

        /**
         * <h2>结束至少保留</h2>
         */
        private final int minTail;
    }
}
