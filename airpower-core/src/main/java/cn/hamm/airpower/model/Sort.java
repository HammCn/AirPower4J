package cn.hamm.airpower.model;

import cn.hamm.airpower.annotation.Description;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>查询排序</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
@Description("查询排序对象")
public class Sort {
    /**
     * <h3>排序字段</h3>
     */
    @Description("排序字段")
    private String field;

    /**
     * <h3>排序方法</h3>
     */
    @Description("排序方向")
    private String direction;
}
