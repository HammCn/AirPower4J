package cn.hamm.airpower.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>分页类</h1>
 *
 * @author Hamm
 */
@Data
@Accessors(chain = true)
public class Page {
    /**
     * <h2>当前页码</h2>
     */
    private Integer pageNum = 1;

    /**
     * <h2>分页条数</h2>
     */
    private Integer pageSize;
}
