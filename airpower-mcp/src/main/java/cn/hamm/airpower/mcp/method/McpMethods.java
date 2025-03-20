package cn.hamm.airpower.mcp.method;

import cn.hamm.airpower.core.dictionary.IDictionary;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h1>McpMethods</h1>
 *
 * @author Hamm.cn
 */
@Getter
@AllArgsConstructor
public enum McpMethods implements IDictionary {
    /**
     * <h3>初始化</h3>
     */
    INITIALIZE(1, "initialize"),

    /**
     * <h3>工具列表</h3>
     */
    TOOLS_LIST(2, "tools/list"),

    /**
     * <h3>工具调用</h3>
     */
    TOOLS_CALL(3, "tools/call"),
    ;

    private final int key;

    private final String label;
}
