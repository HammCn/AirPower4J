package cn.hamm.airpower.web.model.query;

import cn.hamm.airpower.core.annotation.Description;
import cn.hamm.airpower.core.model.RootModel;
import cn.hamm.airpower.web.model.Page;
import cn.hamm.airpower.web.model.Sort;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>分页查询响应类</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
@Description("分页查询响应类")
public class QueryPageResponse<M extends RootModel<M>> {
    /**
     * <h3>总数量</h3>
     */
    @Description("总数量")
    private int total = 0;

    /**
     * <h3>总页数</h3>
     */
    @Description("总页数")
    private int pageCount = 0;

    /**
     * <h3>数据信息</h3>
     */
    @Description("数据列表")
    private List<M> list = new ArrayList<>();

    /**
     * <h3>分页信息</h3>
     */
    @Description("分页信息")
    private Page page = new Page();

    /**
     * <h3>排序信息</h3>
     */
    @Description("排序信息")
    private Sort sort = new Sort();
}
