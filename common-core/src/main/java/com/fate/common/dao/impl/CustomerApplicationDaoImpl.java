package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CustomerApplicationDao;
import com.fate.common.entity.CustomerApplication;
import com.fate.common.mapper.CustomerApplicationMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * C端用户应用来源表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-25
 */
@Repository
public class CustomerApplicationDaoImpl extends ServiceImpl<CustomerApplicationMapper, CustomerApplication> implements CustomerApplicationDao {

    @Override
    public CustomerApplication getByApplicationIdAndOpenId(String openid, Long applicationId) {
        return baseMapper.selectOne(new QueryWrapper<CustomerApplication>().eq(CustomerApplication.WX_OPENID,openid).eq(CustomerApplication.MERCHANT_APP_ID,applicationId));
    }


    @Override
    public CustomerApplication getByCustomerIdAndApplicationId(Long customerId, Long applicationId) {
        return baseMapper.selectOne(new QueryWrapper<CustomerApplication>().eq(CustomerApplication.CUSTOMER_ID,customerId).eq(CustomerApplication.MERCHANT_APP_ID,applicationId));
    }
}
