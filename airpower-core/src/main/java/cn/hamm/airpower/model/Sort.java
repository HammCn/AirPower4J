package cn.hamm.airpower.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>查询排序</h1>
 *
 * @author Hamm
 */
@Data
@Accessors(chain = true)
public class Sort {
    /**
     * <h2>排序字段</h2>
     */
    private String field;

    /**
     * <h2>排序方法</h2>
     */
    private String direction;
}
