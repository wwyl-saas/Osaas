package com.fate.common.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * shardingJDBC数据源,分库分表,最首要的问题是解决分布式条件下全局唯一id的问题，前期采用数据库，后期采用预备id池
 */
@Configuration
@ConfigurationProperties(prefix = "sharding.jdbc")
@Getter
@Setter
@EnableTransactionManagement
public class ShardingConfig {
    private JdbcProperties one=new JdbcProperties();


    /**
     * 配置第一个数据源
     *
     * @return
     */
    public DataSource one() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(one.getDriverClassName());
        dataSource.setJdbcUrl(one.getJdbcUrl());
        dataSource.setUsername(one.getUsername());
        dataSource.setPassword(one.getPassword());
        dataSource.setConnectionTestQuery(one.getConnectionTestQuery());
        return dataSource;
    }

    /**
     * 配置Order表分片规则
     *
     * @return
     */
    @Bean("orderTableRuleConfig")
    public TableRuleConfiguration getOrderTableRuleConfig() {
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration("t_order_","ds0.t_order_${0..5}");
        // 配置分库 + 分表策略
//        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("tenant_id", "ds${tenant_id % 2}"));
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("merchant_id", "t_order_${merchant_id % 6}"));
        return orderTableRuleConfig;
    }

    /**
     * 配置OrderDesc表分片规则
     *
     * @return
     */
    @Bean("orderDescTableRuleConfig")
    public TableRuleConfiguration getOrderItemTableRuleConfig() {
        TableRuleConfiguration orderItemTableRuleConfig = new TableRuleConfiguration("t_order_desc_","ds0.t_order_desc_${0..5}");
        // 配置分库 + 分表策略
//        orderItemTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("tenant_id", "ds${tenant_id % 2}"));
        orderItemTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("merchant_id", "t_order_desc_${merchant_id % 6}"));
        return orderItemTableRuleConfig;
    }

    /**
     * 配置用户支付充值表分片规则
     *
     * @return
     */
    @Bean("consumeLogTableRuleConfig")
    public TableRuleConfiguration getCustomerConsumeLogTableRuleConfig() {
        TableRuleConfiguration orderItemTableRuleConfig = new TableRuleConfiguration("t_customer_consume_","ds0.t_customer_consume_${0..5}");
        // 配置分库 + 分表策略
//        orderItemTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("tenant_id", "ds${tenant_id % 2}"));
        orderItemTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("merchant_id", "t_customer_consume_${merchant_id % 6}"));
        return orderItemTableRuleConfig;
    }


    /**
     * 配置用户支付充值表分片规则
     *
     * @return
     */
    @Bean("statisticTableRuleConfig")
    public TableRuleConfiguration getStatisticTableRuleConfig() {
        TableRuleConfiguration statisticTableRuleConfig = new TableRuleConfiguration("t_statistic_","ds0.t_statistic_${0..5}");
        // 配置分库 + 分表策略
//        orderItemTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("tenant_id", "ds${tenant_id % 2}"));
        statisticTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("merchant_id", "t_statistic_${merchant_id % 6}"));
        return statisticTableRuleConfig;
    }


    /**
     * 配置分片规则
     *
     * @return
     */
    @Bean("shardingRuleConfig")
    public ShardingRuleConfiguration shardingRuleConfiguration(@Qualifier("orderTableRuleConfig") TableRuleConfiguration orderTableRuleConfig,
                                                               @Qualifier("orderDescTableRuleConfig") TableRuleConfiguration orderItemTableRuleConfig,
                                                               @Qualifier("consumeLogTableRuleConfig") TableRuleConfiguration consumeLogTableRuleConfig,
                                                               @Qualifier("statisticTableRuleConfig") TableRuleConfiguration statisticTableRuleConfig) {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        shardingRuleConfig.getTableRuleConfigs().add(orderItemTableRuleConfig);
        shardingRuleConfig.getTableRuleConfigs().add(consumeLogTableRuleConfig);
        shardingRuleConfig.getTableRuleConfigs().add(statisticTableRuleConfig);
        shardingRuleConfig.getBindingTableGroups().add("t_order_, t_order_desc_,t_customer_consume_");

//        shardingRuleConfig.getBroadcastTables().add("t_enums");//全局广播表
        return shardingRuleConfig;
    }

    /**
     * 配置真实数据源,获取数据源对象
     *
     * @return
     */
    @Bean("dataSource")
    @Primary
    public DataSource dataSource(@Qualifier("shardingRuleConfig") ShardingRuleConfiguration shardingRuleConfig) throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("ds0",one());
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());
        return dataSource;
    }


    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource){
        DataSourceTransactionManager dataSourceTransactionManager=new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }
}
