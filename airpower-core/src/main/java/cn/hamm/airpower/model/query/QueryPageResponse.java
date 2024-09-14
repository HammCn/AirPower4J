package cn.hamm.airpower.model.query;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.model.Page;
import cn.hamm.airpower.model.Sort;
import cn.hamm.airpower.root.RootModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>响应分页类</h1>
 *
 * @author Hamm.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Description("分页查询响应对象")
public class QueryPageResponse<M extends RootModel> extends RootModel {
    /**
     * <h2>总数量</h2>
     */
    @Description("总数量")
    private int total = 0;

    /**
     * <h2>总页数</h2>
     */
    @Description("总页数")
    private int pageCount = 0;

    /**
     * <h2>数据信息</h2>
     */
    @Description("数据列表")
    private List<M> list = new ArrayList<>();

    /**
     * <h2>分页信息</h2>
     */
    @Description("分页信息")
    private Page page = new Page();

    /**
     * <h2>排序信息</h2>
     */
    @Description("排序信息")
    private Sort sort = new Sort();
}
