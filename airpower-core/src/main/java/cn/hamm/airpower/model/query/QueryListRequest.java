package cn.hamm.airpower.model.query;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.model.Sort;
import cn.hamm.airpower.root.RootModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <h1>查询列表请求</h1>
 *
 * @param <M> 数据模型
 * @author Hamm.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Description("查询列表请求")
public class QueryListRequest<M extends RootModel<M>> extends QueryRequest<M> {
    /**
     * <h2>排序对象</h2>
     */
    @Description("排序对象")
    private Sort sort = null;

    /**
     * <h2>设置排序</h2>
     *
     * @param sort 排序
     * @return 请求
     */
    public <Q extends QueryListRequest<M>> Q setSort(Sort sort) {
        this.sort = sort;
        //noinspection unchecked
        return (Q) this;
    }
}
