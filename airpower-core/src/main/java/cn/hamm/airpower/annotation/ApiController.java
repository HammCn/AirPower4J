package cn.hamm.airpower.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * <h1>{@code API} 控制器</h1>
 *
 * @author Hamm.cn
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@RestController
@RequestMapping
public @interface ApiController {
    /**
     * <h2><code>Api</code> 的路径</h2>
     */
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String value();
}
