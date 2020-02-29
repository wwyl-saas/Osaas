package com.fate.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/***
 * 异步打印sql日志组件
 */
@Slf4j
public class SqlPrintComponent {
    private static Logger LOGGER = LoggerFactory.getLogger("DaoStatMonitor");

    public SqlPrintComponent() {

    }

    /**
     * 打印sql统计日志
     * @param sqlId
     * @param time
     */
    @Async
    public void printStat(String sqlId,long time){
        try {
            String[] array=sqlId.split("\\.");
            StringBuilder stringBuilder=new StringBuilder();
            if (array.length>2){
                stringBuilder.append(array[array.length-2]).append(".")
                        .append(array[array.length-1]);
            }else{
                stringBuilder.append(sqlId);
            }
            if (time > 200){
                stringBuilder.append("|SLOW");
            }
            stringBuilder.append(":").append(time).append("ms");
            LOGGER.info(stringBuilder.toString());
        }catch (Exception e){
            log.error("打印日志拼装报错");
        }

    }

    /**
     * 打印sql明细
     * @param invocation
     * @param mappedStatement
     * @param sqlId
     */
    @Async
    public void printSql(Invocation invocation, MappedStatement mappedStatement, String sqlId){
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        String sql = getSql(configuration, boundSql, sqlId);
        log.info(sql);
    }

    /**
     * 组装sql
     * @param configuration
     * @param boundSql
     * @param sqlId
     * @return
     */
    public String getSql(Configuration configuration, BoundSql boundSql, String sqlId) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        StringBuilder str = new StringBuilder(100);
        str.append(sqlId);
        str.append(":");
        str.append(sql);
        return str.toString();
    }

    /**
     * 获取方法参数
     * @param obj
     * @return
     */
    private String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }}