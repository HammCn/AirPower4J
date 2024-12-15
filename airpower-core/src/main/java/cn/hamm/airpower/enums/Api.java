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
     * <h3>添加</h3>
     */
    Add(1, "添加", "add"),

    /**
     * <h3>删除</h3>
     */
    Delete(2, "删除", "delete"),

    /**
     * <h3>禁用</h3>
     */
    Disable(3, "禁用", "disable"),

    /**
     * <h3>启用</h3>
     */
    Enable(4, "启用", "enable"),

    /**
     * <h3>查询详情</h3>
     */
    GetDetail(5, "查询详情", "getDetail"),

    /**
     * <h3>列表查询</h3>
     */
    GetList(6, "列表查询", "getList"),

    /**
     * <h3>分页查询</h3>
     */
    GetPage(7, "分页查询", "getPage"),

    /**
     * <h3>修改</h3>
     */
    Update(8, "修改", "update"),

    /**
     * <h3>创建导出任务</h3>
     */
    Export(9, "创建导出任务", "export"),

    /**
     * <h3>查询异步导出结果</h3>
     */
    QueryExport(10, "查询异步导出结果", "queryExport");

    private final int key;
    private final String label;

    /**
     * <h3>绑定方法的名称</h3>
     */
    private final String methodName;
}
