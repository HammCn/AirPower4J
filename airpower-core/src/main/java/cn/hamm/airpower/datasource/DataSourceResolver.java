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
 * @author Hamm.cn
 */
@Service
@Slf4j
public class DataSourceResolver extends AbstractRoutingDataSource {
    /**
     * <h2>数据库驱动协议</h2>
     */
    private static final String DATASOURCE_SCHEME = "jdbc:mysql://";

    /**
     * <h2>驱动类名称</h2>
     */
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    /**
     * <h2>其他信息配置</h2>
     */
    private static final String DATASOURCE_CONFIG = "?allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false";

    /**
     * <h2>线程</h2>
     */
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * <h2>数据源列表</h2>
     */
    public static final Map<Object, Object> DATA_SOURCE_LIST = new HashMap<>();

    /**
     * <h2>初始化空列表</h2>
     */
    public DataSourceResolver() {
        super.setTargetDataSources(DATA_SOURCE_LIST);
    }

    /**
     * <h2>获取数据源参数</h2>
     *
     * @return 数据源参数
     */
    public static String getDataSourceParam() {
        return THREAD_LOCAL.get();
    }

    /**
     * <h2>设置数据源参数</h2>
     *
     * @param param 参数
     */
    public static void setDataSourceParam(String param) {
        THREAD_LOCAL.set(param);
    }

    /**
     * <h2>清空数据源参数</h2>
     */
    public static void clearDataSourceParam() {
        THREAD_LOCAL.remove();
    }

    /**
     * <h2>获取数据源包含数据库的地址</h2>
     *
     * @param dataSource 数据源
     * @return 数据源地址
     */
    private static String getDataSourceUrl(DataSource dataSource) {
        return getServerUrl(dataSource) + "/" + GlobalConfig.databasePrefix + dataSource.getDatabase() + DATASOURCE_CONFIG;
    }

    /**
     * <h2>获取数据源不包含数据库的地址</h2>
     *
     * @param dataSource 数据源
     * @return 数据源地址
     */
    private static String getServerUrl(DataSource dataSource) {
        return DATASOURCE_SCHEME + dataSource.getHost() + ":" + dataSource.getPort();
    }

    /**
     * <h2>创建数据库</h2>
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
            log.error("SQL语句执行失败", exception);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception exception) {
                log.error("数据库连接关闭失败", exception);
            }
        }
    }

    /**
     * <h2>创建数据源</h2>
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
