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
     * 总数量
     */
    private int total = 0;

    /**
     * 总页数
     */
    private int pageCount = 0;

    /**
     * 数据信息
     */
    private List<M> list = new ArrayList<>();

    /**
     * 分页信息
     */
    private Page page = new Page();

    /**
     * 排序信息
     */
    private Sort sort = new Sort();
}
