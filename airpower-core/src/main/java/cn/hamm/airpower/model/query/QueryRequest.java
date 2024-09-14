package cn.hamm.airpower.model.query;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.root.RootModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <h1>查询请求</h1>
 *
 * @param <M> 数据模型
 * @author Hamm.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Description("查询请求")
public class QueryRequest<M extends RootModel> extends RootModel {
    /**
     * <h2>搜索过滤器</h2>
     */
    @Description("过滤器")
    private M filter = null;

    /**
     * <h2>设置过滤器</h2>
     *
     * @param filter 过滤器
     * @return 请求
     */
    public <Q extends QueryRequest<M>> Q setFilter(M filter) {
        this.filter = filter;
        //noinspection unchecked
        return (Q) this;
    }
}
