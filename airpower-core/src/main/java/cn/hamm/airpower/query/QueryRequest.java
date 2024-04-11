package cn.hamm.airpower.query;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.model.Sort;
import cn.hamm.airpower.root.RootModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <h1>查询请求</h1>
 *
 * @param <M> 数据模型
 * @author Hamm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Description("查询请求")
public class QueryRequest<M extends RootModel<M>> extends RootModel<QueryRequest<M>> {
    /**
     * <h2>排序对象</h2>
     */
    @Description("排序对象")
    private Sort sort = null;

    /**
     * <h2>搜索过滤器</h2>
     */
    @Description("过滤器")
    private M filter = null;

    /**
     * <h2>关键词搜索</h2>
     */
    @Description("搜索关键词")
    private String keyword;

    /**
     * <h2>设置过滤器</h2>
     *
     * @param filter 过滤器
     * @return 请求
     */
    @SuppressWarnings("UnusedReturnValue")
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
