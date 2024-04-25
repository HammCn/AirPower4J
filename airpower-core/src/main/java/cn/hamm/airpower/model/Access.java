package cn.hamm.airpower.model;

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
     * <h2>需要登录</h2>
     */
    private boolean login = false;

    /**
     * <h2>需要授权访问</h2>
     */
    private boolean authorize = false;
}
