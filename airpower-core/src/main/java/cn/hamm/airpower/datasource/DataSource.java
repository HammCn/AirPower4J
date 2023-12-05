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
     * 数据库名称(租户的key)
     */
    private String database;

    /**
     * 数据库地址
     */
    private String host = "127.0.0.1";

    /**
     * 数据库端口
     */
    private int port = 3306;

    /**
     * 数据库用户名
     */
    private String user = "root";

    /**
     * 数据库密码
     */
    private String password;
}
