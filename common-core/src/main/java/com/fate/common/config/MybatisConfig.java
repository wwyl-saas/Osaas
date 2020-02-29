package com.fate.common.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.fate.common.config.async.AsyncTaskConfig;
import com.fate.common.interceptor.SqlPrintComponent;
import com.fate.common.interceptor.SqlTimeInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;



@Configuration
@Slf4j
@ComponentScan(basePackages={"com.fate.common.dao.impl"})
@MapperScan(basePackages = MybatisConfig.PACKAGE)
@Import({ShardingConfig.class, JacksonConfig.class, AsyncTaskConfig.class})
public class MybatisConfig {
    static final String PACKAGE = "com.fate.common.mapper";
    static final String ENUM_PACKAGE = "com.fate.common.enums";
    static final String TYPE_PACKAGE = "com.fate.common.entity";
    static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

    @Autowired
    Interceptor[] interceptors;
    /**
     * 设置数据源为sharding jdbc
     *
     * @return
     */
    @Bean("mybatisSqlSessionFactoryBean")
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(@Qualifier("dataSource") DataSource dataSource) throws IOException {
        MybatisSqlSessionFactoryBean mysqlplus = new MybatisSqlSessionFactoryBean();
        mysqlplus.setDataSource(dataSource);
        mysqlplus.setTypeEnumsPackage(ENUM_PACKAGE);
        mysqlplus.setPlugins(interceptors);
        mysqlplus.setTypeAliasesPackage(TYPE_PACKAGE);
        mysqlplus.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return mysqlplus;
    }

    /**
     * 分页和多租户配置,相关 多租户可通过 @SqlParser(filter=true)在mapper类或者方法上排除 SQL 解析
     *
     * @return
     */
    @Bean("paginationInterceptor")
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        TenantSqlParser tenantSqlParser = new TenantSqlParser().setTenantHandler(new MyTenantHandler());
        paginationInterceptor.setSqlParserList(Arrays.asList(tenantSqlParser));
        return paginationInterceptor;
    }


    /**
     * 乐观锁，配合@Version注解使用，涉及到金钱交易时的状态变更
     *
     * @return
     */
    @Bean("optimisticLockerInterceptor")
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * SQL打印插件
     */
    @Profile({"dev"})
    @Bean("sqlTimeInterceptor")
    public SqlTimeInterceptor sqlTimeInterceptor(@Qualifier("sqlPrintComponent") SqlPrintComponent sqlPrintComponent) {
        return new SqlTimeInterceptor(sqlPrintComponent);
    }

    @Profile({"dev"})
    @Bean("sqlPrintComponent")
    public SqlPrintComponent sqlPrintComponent(){
        return new SqlPrintComponent();
    }

}
