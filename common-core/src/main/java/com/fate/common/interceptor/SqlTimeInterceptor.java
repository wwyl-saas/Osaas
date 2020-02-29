package com.fate.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StopWatch;
import java.util.Properties;

/**
 * sql拦截器
 */
@Slf4j
@Intercepts( {@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class , RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class , RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type= Executor.class, method = "update", args = {MappedStatement.class,Object.class})})
public class SqlTimeInterceptor implements Interceptor {

    SqlPrintComponent sqlPrintComponent;


    public SqlTimeInterceptor(SqlPrintComponent sqlPrintComponent) {
        this.sqlPrintComponent=sqlPrintComponent;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        Object returnValue = null;
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();
        try {
            returnValue = invocation.proceed();
            stopWatch.stop();
            sqlPrintComponent.printStat(sqlId,stopWatch.getTotalTimeMillis());
            sqlPrintComponent.printSql(invocation,mappedStatement,sqlId);

        }catch (Throwable e){
            log.error("sql执行异常：",e);
            sqlPrintComponent.printSql(invocation,mappedStatement,sqlId);
            throw e;
        }
        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}