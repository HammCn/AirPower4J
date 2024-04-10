package cn.hamm.airpower.query;

import cn.hamm.airpower.model.Page;
import cn.hamm.airpower.root.RootModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <h1>查询分页请求</h1>
 *
 * @param <M> 数据模型
 * @author Hamm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class QueryPageRequest<M extends RootModel<M>> extends QueryRequest<M> {
    /**
     * <h2>分页</h2>
     */
    private Page page = new Page();
}
