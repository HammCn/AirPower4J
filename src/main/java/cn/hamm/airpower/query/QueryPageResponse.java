package cn.hamm.airpower.query;

import cn.hamm.airpower.model.Page;
import cn.hamm.airpower.model.Sort;
import cn.hamm.airpower.root.RootModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>响应分页类</h1>
 *
 * @author Hamm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class QueryPageResponse<M extends RootModel<?>> {
    /**
     * <h1>总数量</h1>
     */
    private int total = 0;

    /**
     * <h1>总页数</h1>
     */
    private int pageCount = 0;

    /**
     * <h1>数据信息</h1>
     */
    private List<M> list = new ArrayList<>();

    /**
     * <h1>分页信息</h1>
     */
    private Page page = new Page();

    /**
     * <h1>排序信息</h1>
     */
    private Sort sort = new Sort();
}
