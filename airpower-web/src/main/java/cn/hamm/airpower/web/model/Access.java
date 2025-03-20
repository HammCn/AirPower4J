package cn.hamm.airpower.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>权限控制配置类</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
public class Access {
    /**
     * <h3>需要登录</h3>
     */
    private boolean login = false;

    /**
     * <h3>需要授权访问</h3>
     */
    private boolean authorize = false;
}
