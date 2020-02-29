package com.fate.api.admin.service;

import com.fate.common.dao.OrderDao;
import com.fate.common.entity.Order;
import com.fate.common.enums.OrderStatus;
import com.fate.common.model.StatisticModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-25 18:01
 **/
@Service
@Slf4j
public class OrderService {
    @Resource
    OrderDao orderDao;

    public List<Order> getByStatusAndCreateTimeBefore(OrderStatus status, LocalDateTime dateTime,Long merchantId) {
        return orderDao.getByStatusAndCreateTimeBefore(status,dateTime,merchantId);
    }


    /**
     * 批量更新
     * @param newList
     */
    public void updateBatch(List<Order> newList) {
        orderDao.updateBatchById(newList);
    }

    /**
     * 批量删除
     * @param idList
     */
    public void deleteBatch(List<Long> idList) {
        orderDao.removeByIds(idList);
    }

    public List<StatisticModel> getOrderCount(Long merchantId, LocalDate date) {
        return orderDao.getOrderCount(merchantId,date);
    }
}
