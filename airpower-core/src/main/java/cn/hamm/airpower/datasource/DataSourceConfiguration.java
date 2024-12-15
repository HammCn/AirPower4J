package cn.hamm.airpower.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * <h1>数据源配置类</h1>
 *
 * @author Hamm.cn
 */
@Configuration
public class DataSourceConfiguration {
    /**
     * <h3>数据源配置对象</h3>
     */
    @Autowired
    private DataSourceProperties dataSourceProperties;

    /**
     * <h3>设置主要的数据源</h3>
     *
     * @return 数据源对应操作对象
     */
    @Bean()
    @Primary
    public DataSourceResolver setPrimaryDataSource() {
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        DataSourceResolver dataSourceResolver = new DataSourceResolver();
        dataSourceResolver.setDefaultTargetDataSource(druidDataSource);
        return dataSourceResolver;
    }
}
