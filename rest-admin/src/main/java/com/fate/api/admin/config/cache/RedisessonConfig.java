package com.fate.api.admin.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @program: parent
 * @description: redisson配置
 * @author: chenyixin
 * @create: 2019-08-12 18:45
 **/
@Configuration
@Slf4j
public class RedisessonConfig {
    @Autowired
    private RedisProperties redisProperties;


    /**
     * redisson客户端,使用单实例模式
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException{
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setDatabase(redisProperties.getDatabase())
                .setPassword(redisProperties.getPassword())
                .setConnectTimeout((int) redisProperties.getTimeout().toMillis())
                .setConnectionPoolSize(24)
                .setConnectionMinimumIdleSize(1)
                .setSubscriptionConnectionPoolSize(2)
                .setSubscriptionConnectionMinimumIdleSize(1);
        return Redisson.create(config);
    }

}
