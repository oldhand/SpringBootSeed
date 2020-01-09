package com.github.shardingsphere.config;


import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


@Configuration
public class DataSourceConfiguration {

    @Autowired
    private DruidProperties druidproperties;

    @Autowired
    private MasterDataSource masterdatasource;

    @Bean
    public DataSource getDataSource() throws SQLException {
        return buildDataSource();
    }


    private DataSource buildDataSource() throws SQLException {
        Map<String, DataSource> result = new HashMap<>();
        result.put(DataSourceType.MASTER.name(), druidproperties.dataSource(masterdatasource.make()));
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        System.out.println("----------------------------");
        shardingRuleConfig.setDefaultDataSourceName(DataSourceType.MASTER.name());  return ShardingDataSourceFactory.createDataSource(result, shardingRuleConfig, new Properties());
    }
}