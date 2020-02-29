package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.OrderDescDao;
import com.fate.common.entity.OrderDesc;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.OrderDescMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * 订单详情表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class OrderDescDaoImpl extends ServiceImpl<OrderDescMapper, OrderDesc> implements OrderDescDao {

    @Override
    public List<OrderDesc> getByOrderId(Long orderId) {
        Assert.notNull(orderId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<OrderDesc>().eq(OrderDesc.ORDER_ID,orderId));
    }

    @Override
    public OrderDesc findOrderDescFrist(Long orderId) {
        Assert.notNull(orderId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectOne(new QueryWrapper<OrderDesc>().eq(OrderDesc.ORDER_ID,orderId).last("limit 1"));
    }

    @Override
    public List<OrderDesc> getByOrderIds(List<Long> orderIds) {
        return baseMapper.selectList(new QueryWrapper<OrderDesc>().in(OrderDesc.ORDER_ID,orderIds));
    }
}
