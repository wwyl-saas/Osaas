package com.fate.api.admin.listener.processor;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息处理器
 * @param <T>
 */
public interface MessageProcessor<T> {
    /**
     * 此方法注意要做幂等
     * @param message
     * @return
     */
    boolean handle(T message);
}
