package com.fate.api.customer.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;




/**
 * @program: parent
 * @description: session和用户关系存储类，目前一个用户在小程序中只会开启一个长连接，所以一对一的关系
 * @author: chenyixin
 * @create: 2019-05-25 22:50
 **/
//TODO 分布式部署的问题
@Slf4j
public class CustomerSessionUtil {
    private static final DualHashBidiMap<Long, Session> customerSessionMap=new DualHashBidiMap<>();

    /**
     * onopen对应的方法,用户新开的连接，如果存在老的session则自动关闭，只记录新的session
     *
     * @param customerId
     * @param session
     */
    public static void add(Long customerId,Session session){
        Session old=customerSessionMap.get(customerId);
        if (old!=null){//存在连接，则关闭并删除
            log.error("用户存在未关闭的连接"+customerId);
            ReentrantLock lock=new ReentrantLock();
            lock.lock();
            try {
                old.close();
            } catch (IOException e) {
                log.error("同一个用户老的session关闭出错",e);
            }
            customerSessionMap.remove(old);
            lock.unlock();
        }
        customerSessionMap.put(customerId,session);
        log.info("新增用户连接，剩余session数"+customerSessionMap.size());
    }

    /**
     * 当关闭连接时，移除信息
     * @param session
     */
    public static void remove(Session session){
        log.debug("关闭客户长连接"+customerSessionMap.getKey(session));
        customerSessionMap.removeValue(session);
        log.info("删除用户连接，剩余session数"+customerSessionMap.size());
    }

    /**
     * 获取用户Id
     * @param session
     * @return
     */
    public static Optional<Long> getCustomerId(Session session){
        return Optional.ofNullable(customerSessionMap.getKey(session));
    }

    /**
     * 获取用户session
     * @param customerId
     * @return
     */
    public static Optional<Session> getSession(Long customerId){
        return Optional.ofNullable(customerSessionMap.get(customerId));
    }

}
