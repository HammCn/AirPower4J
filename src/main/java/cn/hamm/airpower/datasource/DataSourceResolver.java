package cn.hamm.airpower.datasource;

import cn.hamm.airpower.config.GlobalConfig;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1>数据源操作类</h1>
 *
 * @author Hamm
 */
@Service
public class DataSourceResolver extends AbstractRoutingDataSource {
    /**
     * <h1>数据库驱动协议</h1>
     */
    private static final String DATASOURCE_SCHEME = "jdbc:mysql://";

    /**
     * <h1>驱动类名称</h1>
     */
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    /**
     * <h1>其他信息配置</h1>
     */
    private static final String DATASOURCE_CONFIG = "?allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false";

    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * <h1>数据源列表</h1>
     */
    public static Map<Object, Object> dataSourceList = new HashMap<>();

    /**
     * <h1>初始化空列表</h1>
     */
    public DataSourceResolver() {
        super.setTargetDataSources(dataSourceList);
    }

    /**
     * <h1>获取数据源参数</h1>
     *
     * @return 数据源参数
     */
    public static String getDataSourceParam() {
        return THREAD_LOCAL.get();
    }

    /**
     * <h1>设置数据源参数</h1>
     *
     * @param param 参数
     */
    public static void setDataSourceParam(String param) {
        THREAD_LOCAL.set(param);
    }

    /**
     * <h1>清空数据源参数</h1>
     */
    public static void clearDataSourceParam() {
        THREAD_LOCAL.remove();
    }

    /**
     * <h1>获取数据源包含数据库的地址</h1>
     *
     * @param dataSource 数据源
     * @return 数据源地址
     */
    private static String getDataSourceUrl(DataSource dataSource) {
        return getServerUrl(dataSource) + "/" + GlobalConfig.databasePrefix + dataSource.getDatabase() + DATASOURCE_CONFIG;
    }

    /**
     * <h1>获取数据源不包含数据库的地址</h1>
     *
     * @param dataSource 数据源
     * @return 数据源地址
     */
    private static String getServerUrl(DataSource dataSource) {
        return DATASOURCE_SCHEME + dataSource.getHost() + ":" + dataSource.getPort();
    }

    /**
     * <h1>创建数据库</h1>
     *
     * @param dataSource 数据源信息
     */
    @SuppressWarnings({"unused", "SqlNoDataSourceInspection"})
    public void createDatabase(DataSource dataSource) {
        Statement statement = null;
        Connection connection = null;
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(getServerUrl(dataSource)
                    , dataSource.getUser(), dataSource.getPassword());

            statement = connection.createStatement();
            statement.execute(
                    "CREATE DATABASE IF NOT EXISTS " + GlobalConfig.databasePrefix + dataSource.getDatabase() +
                            " DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci"
            );
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * <h1>创建数据源</h1>
     *
     * @param dataSourceInfo 数据源
     */
    @SuppressWarnings("unused")
    public void createDataSource(DataSource dataSourceInfo) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setDriverClassName(DRIVER_NAME);
        dataSource.setUrl(getDataSourceUrl(dataSourceInfo));
        dataSource.setUsername(dataSourceInfo.getUser());
        dataSource.setPassword(dataSourceInfo.getPassword());
        dataSourceList.put(GlobalConfig.databasePrefix + dataSourceInfo.getDatabase(), dataSource);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceResolver.getDataSourceParam();
    }
}
