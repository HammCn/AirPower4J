package cn.hamm.airpower.datasource;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <h1>数据源信息</h1>
 *
 * @author Hamm
 */
@Data
@Accessors(chain = true)
public class DataSource {
    /**
     * <h2>数据库名称(租户的key)</h2>
     */
    private String database;

    /**
     * <h2>数据库地址</h2>
     */
    private String host = "127.0.0.1";

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
