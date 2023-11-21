package cn.hamm.airpower.query;

import cn.hamm.airpower.model.Sort;
import cn.hamm.airpower.root.RootModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>查询请求</h1>
 *
 * @param <M> 数据模型
 * @author Hamm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryRequest<M extends RootModel<M>> {
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

    /**
     * <h2>设置过滤器</h2>
     *
     * @param filter 过滤器
     * @return 请求
     */
    public QueryRequest<M> setFilter(M filter) {
        this.filter = filter;
        return this;
    }

    /**
     * <h2>设置排序</h2>
     *
     * @param sort 排序
     * @return 请求
     */
    public QueryRequest<M> setSort(Sort sort) {
        this.sort = sort;
        return this;
    }

    /**
     * <h2>设置查询关键词</h2>
     *
     * @param keyword 关键词
     * @return 请求
     */
    public QueryRequest<M> setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
}
