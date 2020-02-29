package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.CustomerCard;
import com.fate.common.entity.CustomerCoupon;

import java.util.List;

/**
 * <p>
 * C端用户卡项表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
public interface CustomerCardDao extends IService<CustomerCard> {

    List<CustomerCard> findByCustomerId(Long customerId);
}
