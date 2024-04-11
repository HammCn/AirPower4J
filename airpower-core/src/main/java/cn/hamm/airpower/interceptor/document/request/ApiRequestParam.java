package cn.hamm.airpower.interceptor.document.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <h1>Api请求参数</h1>
 *
 * @author Hamm
 */
@Data
@Accessors(chain = true)
public class ApiRequestParam {
    private String name;
    private String type;
    private String description;
    private String document;
    private Boolean required = false;
    private List<Map<String, String>> dictionary = new ArrayList<>();
    private List<ApiRequestParam> children = new ArrayList<>();
    private Boolean phone = false;
    private Boolean email = false;
    private Integer maxLength = 0;
}
