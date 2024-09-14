package cn.hamm.airpower.model;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.root.RootModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <h1>分页类</h1>
 *
 * @author Hamm.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Description("分页类")
public class Page extends RootModel {
    /**
     * <h2>当前页码</h2>
     */
    @Description("当前页码")
    private Integer pageNum = 1;

    /**
     * <h2>分页条数</h2>
     */
    @Description("分页条数")
    private Integer pageSize;
}
