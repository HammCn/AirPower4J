package cn.hamm.airpower.mcp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <h1>MCP错误</h1>
 *
 * @author Hamm.cn
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class McpException extends Exception {
    /**
     * <h3>错误代码</h3>
     */
    private Integer code;

    /**
     * <h3>错误信息</h3>
     */
    private String message;
}
