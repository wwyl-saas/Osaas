package com.fate.api.admin.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.fate.common.config.MyTenantHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-25 16:28
 **/
@Configuration
@Slf4j
public class MybatisAdditionalConfig {

    /**
     * 重新定义不含多租户插件的分页插件
     *
     * @return
     */
    @Primary
    @Bean("admin-paginationInterceptor")
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
