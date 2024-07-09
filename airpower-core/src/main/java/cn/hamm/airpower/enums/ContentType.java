package cn.hamm.airpower.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;

/**
 * <h1>请求的数据类型</h1>
 *
 * @author Hamm.cn
 */
@Getter
@AllArgsConstructor
public enum ContentType {
    /**
     * <h2>{@code JSON}</h2>
     */
    JSON(MediaType.APPLICATION_JSON_VALUE),

    /**
     * <h2>{@code HTML}</h2>
     */
    HTML(MediaType.TEXT_HTML_VALUE),

    /**
     * <h2>{@code PLAIN}</h2>
     */
    PLAIN(MediaType.TEXT_PLAIN_VALUE),

    /**
     * <h2>{@code XML}</h2>
     */
    XML(MediaType.TEXT_XML_VALUE),

    /**
     * <h2>{@code FORM_URLENCODED}</h2>
     */
    FORM_URLENCODED(MediaType.APPLICATION_FORM_URLENCODED_VALUE),

    /**
     * <h2>{@code MULTIPART_FORM_DATA}</h2>
     */
    MULTIPART_FORM_DATA(MediaType.MULTIPART_FORM_DATA_VALUE);

    private final String value;
}
