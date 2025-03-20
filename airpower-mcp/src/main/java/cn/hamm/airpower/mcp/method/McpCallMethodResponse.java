package cn.hamm.airpower.mcp.method;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>MCP调用方法响应</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
public class McpCallMethodResponse {
    /**
     * <h3>内容</h3>
     */
    private List<Text> content = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    public static class Text {
        /**
         * <h3>文本</h3>
         */
        private String text;

        /**
         * <h3>类型</h3>
         */
        private String type = "text";
    }
}
