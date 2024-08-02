package cn.hamm.airpower.datasource;

import cn.hamm.airpower.config.Constant;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>数据源信息</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
public class DataSource {
    /**
     * <h2>数据库名称</h2>
     * 租户的 {@code key}
     */
    private String database;

    /**
     * <h2>数据库地址</h2>
     */
    private String host = Constant.LOCAL_IP_ADDRESS;

    /**
     * <h2>数据库端口</h2>
     */
    private int port = 3306;

    /**
     * <h2>数据库用户名</h2>
     */
    private String user = "root";

    /**
     * <h2>数据库密码</h2>
     */
    private String password;
}
