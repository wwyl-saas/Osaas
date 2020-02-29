package com.fate.common.config.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * @program: parent
 * @description:自定义异常处理类
 * @author: chenyixin
 * @create: 2019-06-13 06:53
 **/
@Slf4j
public class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.info("Exception message - " + ex.getMessage());
        log.info("Method name - " + method.getName());
        for (Object param : params) {
            log.info("Parameter value - " + param);
        }
    }
}
