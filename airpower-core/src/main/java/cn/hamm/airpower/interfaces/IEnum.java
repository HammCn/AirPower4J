package cn.hamm.airpower.interfaces;

/**
 * <h1>枚举标准接口</h1>
 *
 * @author hamm
 */
public interface IEnum {
    /**
     * <h2>获取枚举的key</h2>
     *
     * @return key
     */
    int getValue();

    /**
     * <h2>获取枚举的描述</h2>
     *
     * @return 描述
     */
    String getLabel();
}
