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
    Add(1, "添加", "add"),

    /**
     * <h2>删除</h2>
     */
    Delete(2, "删除", "delete"),

    /**
     * <h2>禁用</h2>
     */
    Disable(3, "禁用", "disable"),

    /**
     * <h2>启用</h2>
     */
    Enable(4, "启用", "enable"),

    /**
     * <h2>查询详情</h2>
     */
    GetDetail(5, "查询详情", "getDetail"),

    /**
     * <h2>列表查询</h2>
     */
    GetList(6, "列表查询", "getList"),

    /**
     * <h2>分页查询</h2>
     */
    GetPage(7, "分页查询", "getPage"),

    /**
     * <h2>修改</h2>
     */
    Update(8, "修改", "update"),

    /**
     * <h2>创建导出任务</h2>
     */
    Export(9, "创建导出任务", "export"),

    /**
     * <h2>查询异步导出结果</h2>
     */
    QueryExport(10, "查询异步导出结果", "queryExport");

    private final int key;
    private final String label;

    /**
     * <h2>绑定方法的名称</h2>
     */
    private final String methodName;
}
