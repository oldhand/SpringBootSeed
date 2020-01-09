package com.github.shardingsphere.config;


import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
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

    @Autowired
    private SlaveDataSource slavedatasource;

    private Boolean slaveenabled;

    @Bean
    public DataSource getDataSource() throws SQLException {
        return buildDataSource();
    }


    private DataSource buildDataSource() throws SQLException {
        slaveenabled = slavedatasource.isEnabled();
        Map<String, DataSource> result = new HashMap<>();
        result.put(DataSourceType.MASTER.name(), druidproperties.dataSource(masterdatasource.make()));
        if (slaveenabled) {
            result.put(DataSourceType.SLAVE.name(), druidproperties.dataSource(slavedatasource.make()));
        }

        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.setDefaultDataSourceName(DataSourceType.MASTER.name());
        if (slaveenabled) {
            shardingRuleConfig.getTableRuleConfigs().add(YearContentIdsTableRuleConfiguration());
            shardingRuleConfig.getTableRuleConfigs().add(YearMonthContentIdsTableRuleConfiguration());
        }
        shardingRuleConfig.setDefaultDataSourceName(DataSourceType.MASTER.name());  return ShardingDataSourceFactory.createDataSource(result, shardingRuleConfig, new Properties());
    }

    TableRuleConfiguration YearContentIdsTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("yearcontent_ids", DataSourceType.SLAVE.name()+".yearcontent_ids");
        return result;
    }
    TableRuleConfiguration YearMonthContentIdsTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("yearmonthcontent_ids", DataSourceType.SLAVE.name()+".yearmonthcontent_ids");
        return result;
    }
}