package cn.hamm.airpower.model.query;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.model.Page;
import cn.hamm.airpower.root.RootModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <h1>查询分页请求</h1>
 *
 * @param <M> 数据模型
 * @author Hamm.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Description("查询分页请求")
public class QueryPageRequest<M extends RootModel<M>> extends QueryListRequest<M, QueryPageRequest<M>> {
    /**
     * <h2>分页</h2>
     */
    @Description("分页信息")
    private Page page = new Page();
}
