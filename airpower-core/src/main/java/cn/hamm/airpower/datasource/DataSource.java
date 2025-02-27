package cn.hamm.airpower.datasource;

import lombok.Data;
import lombok.experimental.Accessors;

import static cn.hamm.airpower.util.RequestUtil.LOCAL_IP_ADDRESS;

/**
 * <h1>数据源信息</h1>
 *
 * @author Hamm.cn
 */
@Data
@Accessors(chain = true)
public class DataSource {
    /**
     * <h3>数据库名称</h3>
     * 租户的 {@code key}
     */
    private String database;

    /**
     * <h3>数据库地址</h3>
     */
    private String host = LOCAL_IP_ADDRESS;

    /**
     * <h3>数据库端口</h3>
     */
    private int port = 3306;

    /**
     * <h3>数据库用户名</h3>
     */
    private String user = "root";

    /**
     * <h3>数据库密码</h3>
     */
    private String password;
}
