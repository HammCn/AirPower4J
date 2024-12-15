package cn.hamm.airpower.model;

import cn.hamm.airpower.annotation.Description;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>分页类</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
@Description("分页类")
public class Page {
    /**
     * <h3>当前页码</h3>
     */
    @Description("当前页码")
    private Integer pageNum = 1;

    /**
     * <h3>分页条数</h3>
     */
    @Description("分页条数")
    private Integer pageSize;
}
