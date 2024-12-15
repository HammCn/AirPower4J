package cn.hamm.airpower.open;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.interfaces.IDictionary;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h1>开放应用加密方式</h1>
 *
 * @author Hamm
 */
@AllArgsConstructor
@Getter
@Description("开放应用加密方式")
public enum OpenArithmeticType implements IDictionary {
    /**
     * <h3>不加密</h3>
     */
    NO(0, "NO"),

    /**
     * <h3>{@code AES} 算法</h3>
     */
    AES(1, "AES"),

    /**
     * <h3>{@code RSA} 算法</h3>
     */
    RSA(2, "RSA");

    private final int key;
    private final String label;
}
