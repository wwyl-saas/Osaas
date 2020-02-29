package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.OrderDesc;

import java.util.List;

/**
 * <p>
 * 订单详情表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface OrderDescDao extends IService<OrderDesc> {

    List<OrderDesc> getByOrderId(Long orderId);

    OrderDesc findOrderDescFrist(Long orderId);

    List<OrderDesc> getByOrderIds(List<Long> orderIds);
}
