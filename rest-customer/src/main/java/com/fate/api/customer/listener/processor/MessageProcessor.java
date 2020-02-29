package com.fate.api.customer.listener.processor;

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
