package cn.hamm.airpower.api;

/**
 * <h1>父类接口枚举</h1>
 *
 * @author Hamm.cn
 * @apiNote 可通过 {@link Extends} 注解为子控制器的类标记需要继承或过滤父类控制器提供的这些方法
 */
public enum Api {
    /**
     * <h2>添加</h2>
     */
    Add,

    /**
     * <h2>删除</h2>
     */
    Delete,

    /**
     * <h2>禁用</h2>
     */
    Disable,

    /**
     * <h2>启用</h2>
     */
    Enable,

    /**
     * <h2>查询详情</h2>
     */
    GetDetail,

    /**
     * <h2>列表查询</h2>
     */
    GetList,

    /**
     * <h2>分页查询</h2>
     */
    GetPage,

    /**
     * <h2>修改</h2>
     */
    Update,
}
