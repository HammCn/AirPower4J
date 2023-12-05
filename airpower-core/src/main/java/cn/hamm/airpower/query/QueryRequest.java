package cn.hamm.airpower.query;

import cn.hamm.airpower.model.Sort;
import cn.hamm.airpower.root.RootEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <h1>查询请求</h1>
 *
 * @param <E> 数据模型
 * @author Hamm
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QueryRequest<E extends RootEntity<E>> {
    /**
     * 排序对象
     */
    private Sort sort = null;

    /**
     * 搜索过滤器
     */
    private E filter = null;

    /**
     * 关键词搜索
     */
    private String keyword;

    /**
     * 设置过滤器
     *
     * @param filter 过滤器
     * @return 请求
     */
    public QueryRequest<E> setFilter(E filter) {
        this.filter = filter;
        return this;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     * @return 请求
     */
    public QueryRequest<E> setSort(Sort sort) {
        this.sort = sort;
        return this;
    }

    /**
     * 设置查询关键词
     *
     * @param keyword 关键词
     * @return 请求
     */
    @SuppressWarnings("unused")
    public QueryRequest<E> setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
}
