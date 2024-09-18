package cn.hamm.airpower.model;

import cn.hamm.airpower.annotation.Description;
import cn.hamm.airpower.annotation.Document;
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
     * <h2>排序字段</h2>
     */
    @Description("排序字段")
    private String field;

    /**
     * <h2>排序方法</h2>
     */
    @Description("排序方法")
    @Document("asc / desc")
    private String direction;
}
