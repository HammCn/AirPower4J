package cn.hamm.airpower.api;

/**
 * <h1>父类接口枚举</h1>
 *
 * @author hamm
 * @apiNote 可通过 {@link Extends} 注解为子控制器的类标记需要继承或过滤父类控制器提供的这些方法
 */
public enum Api {
    /**
     * 添加
     */
    Add,

    /**
     * 删除
     */
    Delete,

    /**
     * 禁用
     */
    Disable,

    /**
     * 启用
     */
    Enable,

    /**
     * 查询详情
     */
    GetDetail,

    /**
     * 列表查询
     */
    GetList,

    /**
     * 分页查询
     */
    GetPage,

    /**
     * 修改
     */
    Update,
}
