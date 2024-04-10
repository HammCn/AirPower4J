package cn.hamm.airpower.query;

import cn.hamm.airpower.model.Page;
import cn.hamm.airpower.model.Sort;
import cn.hamm.airpower.root.RootModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>响应分页类</h1>
 *
 * @author Hamm
 */
@Data
@Accessors(chain = true)
public class QueryPageResponse<M extends RootModel<M>> {
    /**
     * <h2>总数量</h2>
     */
    private int total = 0;

    /**
     * <h2>总页数</h2>
     */
    private int pageCount = 0;

    /**
     * <h2>数据信息</h2>
     */
    private List<M> list = new ArrayList<>();

    /**
     * <h2>分页信息</h2>
     */
    private Page page = new Page();

    /**
     * <h2>排序信息</h2>
     */
    private Sort sort = new Sort();
}
