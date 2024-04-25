package cn.hamm.airpower.enums;

import cn.hamm.airpower.annotation.Extends;
import cn.hamm.airpower.interfaces.IDictionary;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h1>父类接口枚举</h1>
 *
 * @author Hamm.cn
 * @apiNote 可通过 {@link Extends} 注解为子控制器的类标记需要继承或过滤父类控制器提供的这些方法
 */
@Getter
@AllArgsConstructor
public enum Api implements IDictionary {
    /**
     * <h2>添加</h2>
     */
    Add(1, "add"),

    /**
     * <h2>删除</h2>
     */
    Delete(2, "delete"),

    /**
     * <h2>禁用</h2>
     */
    Disable(3, "disable"),

    /**
     * <h2>启用</h2>
     */
    Enable(4, "enable"),

    /**
     * <h2>查询详情</h2>
     */
    GetDetail(5, "getDetail"),

    /**
     * <h2>列表查询</h2>
     */
    GetList(6, "getList"),

    /**
     * <h2>分页查询</h2>
     */
    GetPage(7, "getPage"),

    /**
     * <h2>修改</h2>
     */
    Update(8, "update");

    private final int key;
    private final String label;
}
