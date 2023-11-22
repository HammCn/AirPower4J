package cn.hamm.airpower.query;

import cn.hamm.airpower.model.Page;
import cn.hamm.airpower.root.RootEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <h1>查询分页请求</h1>
 *
 * @param <E> 数据模型
 * @author Hamm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class QueryPageRequest<E extends RootEntity<E>> extends QueryRequest<E> {
    /**
     * <h2>分页</h2>
     */
    private Page page = new Page();
}
