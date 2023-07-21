package cn.hamm.airpower.interfaces;

/**
 * <h1>枚举标准接口</h1>
 *
 * @author hamm
 */
public interface IEnum {
    /**
     * <h1>获取枚举的key</h1>
     *
     * @return key
     */
    int getValue();

    /**
     * <h1>获取枚举的描述</h1>
     *
     * @return 描述
     */
    @SuppressWarnings("unused")
    String getLabel();
}
