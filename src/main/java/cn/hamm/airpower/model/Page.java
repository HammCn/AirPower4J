package cn.hamm.airpower.model;

import cn.hamm.airpower.config.GlobalConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <h1>分页类</h1>
 *
 * @author Hamm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Page {
    /**
     * <h2>当前页码</h2>
     */
    private int pageNum = 1;

    /**
     * <h2>分页条数</h2>
     */
    private int pageSize = GlobalConfig.defaultPageSize;
}
