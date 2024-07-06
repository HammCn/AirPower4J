package cn.hamm.airpower.open;

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
public enum OpenArithmeticType implements IDictionary {
    /**
     * <h2><code>AES</code> 算法</h2>
     */
    AES(1, "AES"),

    /**
     * <h2><code>RSA</code> 算法</h2>
     */
    RSA(2, "RSA"),

    /**
     * <h2>不加密</h2>
     */
    NO(3, "NO");

    private final int key;
    private final String label;
}
