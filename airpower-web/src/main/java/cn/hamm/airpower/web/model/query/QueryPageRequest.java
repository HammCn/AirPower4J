package cn.hamm.airpower.web.model.query;

import cn.hamm.airpower.core.annotation.Description;
import cn.hamm.airpower.core.model.RootModel;
import cn.hamm.airpower.web.model.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h1>查询分页请求</h1>
 *
 * @param <M> 数据模型
 * @author Hamm.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Description("查询分页请求")
public class QueryPageRequest<M extends RootModel<M>> extends QueryListRequest<M> {
    /**
     * <h3>分页信息</h3>
     */
    @Description("分页信息")
    private Page page = new Page();
}
