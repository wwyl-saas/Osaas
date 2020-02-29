package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CustomerCardDao;
import com.fate.common.entity.CustomerCard;
import com.fate.common.entity.CustomerCoupon;
import com.fate.common.enums.CouponStatus;
import com.fate.common.mapper.CustomerCardMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * C端用户卡项表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
@Repository
public class CustomerCardDaoImpl extends ServiceImpl<CustomerCardMapper, CustomerCard> implements CustomerCardDao {

    @Override
    public List<CustomerCard> findByCustomerId(Long customerId) {
        return baseMapper.selectList(new QueryWrapper<CustomerCard>().eq(CustomerCard.CUSTOMER_ID,customerId).ne(CustomerCard.STATUS, CouponStatus.USED));
    }
}
