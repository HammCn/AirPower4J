package cn.hamm.airpower.interfaces;

/**
 * <h1>枚举标准接口</h1>
 *
 * @author hamm
 */
public interface IEnum {
    /**
     * 获取枚举的key
     *
     * @return key
     */
    @SuppressWarnings("unused")
    int getValue();

    /**
     * 获取枚举的描述
     *
     * @return 描述
     */
    @SuppressWarnings("unused")
    String getLabel();
}
