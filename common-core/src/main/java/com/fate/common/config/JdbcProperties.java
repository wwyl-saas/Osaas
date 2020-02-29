package com.fate.common.config;

import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * @program: parent
 * @description: JDBC配置信息
 * @author: zxb
 * @create: 2019-04-29 23:27
 **/
@Setter
@Getter
public class JdbcProperties {
    private static boolean unitTest;
    private  String catalog;
    private  long connectionTimeout;
    private  long validationTimeout;
    private  long idleTimeout;
    private  long leakDetectionThreshold;
    private  long maxLifetime;
    private  int maxPoolSize;
    private  int minIdle;
    private  String username;
    private  String password;
    private long initializationFailTimeout;
    private String connectionInitSql;
    private String connectionTestQuery;
    private String dataSourceClassName;
    private String dataSourceJndiName;
    private String driverClassName;
    private String jdbcUrl;
    private String poolName;
    private String schema;
    private String transactionIsolationName;
    private boolean isAutoCommit;
    private boolean isReadOnly;
    private boolean isIsolateInternalQueries;
    private boolean isRegisterMbeans;
    private boolean isAllowPoolSuspension;
    private DataSource dataSource;
    private Properties dataSourceProperties;
    private ThreadFactory threadFactory;
    private ScheduledExecutorService scheduledExecutor;
    private MetricsTrackerFactory metricsTrackerFactory;
    private Object metricRegistry;
    private Object healthCheckRegistry;
    private Properties healthCheckProperties;
    private  boolean sealed;

}
