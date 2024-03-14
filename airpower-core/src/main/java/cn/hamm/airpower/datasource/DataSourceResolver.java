package cn.hamm.airpower.datasource;

import cn.hamm.airpower.config.GlobalConfig;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DataSourceResolver extends AbstractRoutingDataSource {
    /**
     * 数据库驱动协议
     */
    private static final String DATASOURCE_SCHEME = "jdbc:mysql://";

    /**
     * 驱动类名称
     */
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    /**
     * 其他信息配置
     */
    private static final String DATASOURCE_CONFIG = "?allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false";

    /**
     * 线程
     */
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 数据源列表
     */
    public static final Map<Object, Object> DATA_SOURCE_LIST = new HashMap<>();

    /**
     * 初始化空列表
     */
    public DataSourceResolver() {
        super.setTargetDataSources(DATA_SOURCE_LIST);
    }

    /**
     * 获取数据源参数
     *
     * @return 数据源参数
     */
    public static String getDataSourceParam() {
        return THREAD_LOCAL.get();
    }

    /**
     * 设置数据源参数
     *
     * @param param 参数
     */
    public static void setDataSourceParam(String param) {
        THREAD_LOCAL.set(param);
    }

    /**
     * 清空数据源参数
     */
    public static void clearDataSourceParam() {
        THREAD_LOCAL.remove();
    }

    /**
     * 获取数据源包含数据库的地址
     *
     * @param dataSource 数据源
     * @return 数据源地址
     */
    private static String getDataSourceUrl(DataSource dataSource) {
        return getServerUrl(dataSource) + "/" + GlobalConfig.databasePrefix + dataSource.getDatabase() + DATASOURCE_CONFIG;
    }

    /**
     * 获取数据源不包含数据库的地址
     *
     * @param dataSource 数据源
     * @return 数据源地址
     */
    private static String getServerUrl(DataSource dataSource) {
        return DATASOURCE_SCHEME + dataSource.getHost() + ":" + dataSource.getPort();
    }

    /**
     * 创建数据库
     *
     * @param dataSource 数据源信息
     */
    public void createDatabase(DataSource dataSource) {
        Statement statement = null;
        Connection connection = null;
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(getServerUrl(dataSource)
                    , dataSource.getUser(), dataSource.getPassword());

            statement = connection.createStatement();
            //noinspection SqlSourceToSinkFlow
            statement.execute(
                    "CREATE DATABASE IF NOT EXISTS " + GlobalConfig.databasePrefix + dataSource.getDatabase() +
                            " DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci"
            );
        } catch (Exception exception) {
            log.error(exception.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception exception) {
                log.error(exception.getMessage());
            }
        }
    }

    /**
     * 创建数据源
     *
     * @param dataSourceInfo 数据源
     */
    public void createDataSource(DataSource dataSourceInfo) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setDriverClassName(DRIVER_NAME);
        dataSource.setUrl(getDataSourceUrl(dataSourceInfo));
        dataSource.setUsername(dataSourceInfo.getUser());
        dataSource.setPassword(dataSourceInfo.getPassword());
        DATA_SOURCE_LIST.put(GlobalConfig.databasePrefix + dataSourceInfo.getDatabase(), dataSource);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceResolver.getDataSourceParam();
    }
}
