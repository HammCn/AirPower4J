package cn.hamm.airpower.datasource;

import cn.hamm.airpower.config.GlobalConfig;
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
     * <h1>数据库名称(租户的key)</h1>
     */
    private String database = GlobalConfig.primaryDataBase;

    /**
     * <h1>数据库地址</h1>
     */
    private String host = GlobalConfig.defaultDatabaseHost = "127.0.0.1";

    /**
     * <h1>数据库端口</h1>
     */
    private int port = GlobalConfig.defaultDatabasePort = 3306;

    /**
     * <h1>数据库用户名</h1>
     */
    private String user = "root";

    /**
     * <h1>数据库密码</h1>
     */
    private String password;
}
