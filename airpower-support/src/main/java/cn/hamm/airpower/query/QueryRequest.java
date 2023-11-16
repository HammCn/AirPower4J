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
     * <h2>排序对象</h2>
     */
    private Sort sort = null;

    /**
     * <h2>搜索过滤器</h2>
     */
    private M filter = null;

    /**
     * <h2>关键词搜索</h2>
     */
    private String keyword;
}
