package cn.hamm.airpower.core.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h1>请求的数据类型</h1>
 *
 * @author Hamm.cn
 */
@Getter
@AllArgsConstructor
public enum ContentType {
    /**
     * <h3>{@code JSON}</h3>
     */
    JSON("application/json"),

    /**
     * <h3>{@code HTML}</h3>
     */
    HTML("text/html"),

    /**
     * <h3>{@code PLAIN}</h3>
     */
    PLAIN("text/plain"),

    /**
     * <h3>{@code XML}</h3>
     */
    XML("text/xml"),

    /**
     * <h3>{@code FORM_URLENCODED}</h3>
     */
    FORM_URLENCODED("application/x-www-form-urlencoded"),

    /**
     * <h3>{@code MULTIPART_FORM_DATA}</h3>
     */
    MULTIPART_FORM_DATA("multipart/form-data"),

    /**
     * <h3>{@code stream}</h3>
     */
    STREAM("application/octet-stream");

    private final String value;
}
