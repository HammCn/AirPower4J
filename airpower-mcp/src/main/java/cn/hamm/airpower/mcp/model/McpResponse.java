package cn.hamm.airpower.mcp.model;

import cn.hamm.airpower.mcp.exception.McpException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <h1>MCP响应</h1>
 *
 * @author Hamm.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class McpResponse extends McpJson {
    /**
     * <h3>结果</h3>
     */
    private Object result;

    /**
     * <h3>错误</h3>
     */
    private McpException error;
}
