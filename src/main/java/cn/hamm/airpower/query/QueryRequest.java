package cn.hamm.airpower.query;

import cn.hamm.airpower.model.Sort;
import cn.hamm.airpower.root.RootModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <h1>查询请求</h1>
 *
 * @param <M> 数据模型
 * @author Hamm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class QueryRequest<M extends RootModel<?>> {
    /**
     * <h1>排序对象</h1>
     */
    private Sort sort = null;

    /**
     * <h1>搜索过滤器</h1>
     */
    private M filter = null;

    /**
     * <h1>关键词搜索</h1>
     */
    private String keyword;
}
