package cn.hamm.airpower.model;

import cn.hamm.airpower.config.GlobalConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <h1>查询排序</h1>
 *
 * @author Hamm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Sort {
    /**
     * <h1>排序字段</h1>
     */
    private String field = GlobalConfig.defaultSortField;

    /**
     * <h1>排序方法</h1>
     */
    private String direction = GlobalConfig.defaultSortDirection;
}
