package com.github.shardingsphere.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 获得主数据源
 *
 */

@Configuration
public class SlaveDataSource {

    @Value("${spring.datasource.druid.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.druid.slave.url}")
    private String url;

    @Value("${spring.datasource.druid.slave.username}")
    private String username;

    @Value("${spring.datasource.druid.slave.password}")
    private String password;

    @Value("${spring.datasource.druid.slave.enabled}")
    private Boolean enabled;

    public boolean isEnabled() {
         return enabled;
    }

    public DruidDataSource make() {
        DruidDataSource result = new DruidDataSource();
        result.setDriverClassName(driverClassName);
        result.setUrl(url);
        result.setUsername(username);
        result.setPassword(password);
        return result;
    }
}