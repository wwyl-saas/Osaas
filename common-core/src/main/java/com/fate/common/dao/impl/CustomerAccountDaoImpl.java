package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CustomerAccountDao;
import com.fate.common.entity.CustomerAccount;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.CustomerAccountMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * C端用户账户表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class CustomerAccountDaoImpl extends ServiceImpl<CustomerAccountMapper, CustomerAccount> implements CustomerAccountDao {

    @Override
    public CustomerAccount findByCustomerId(Long customerId) {
        Assert.notNull(customerId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectOne(new QueryWrapper<CustomerAccount>().eq(CustomerAccount.CUSTOMER_ID,customerId));
    }

    @Override
    public Integer getNewMemberCount(LocalDate date) {
        return baseMapper.selectCount(new QueryWrapper<CustomerAccount>().between(CustomerAccount.CREATE_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59)));
    }

    @Override
    public List<Long> getCustomerIdsBy(LocalDate date) {
        List<Long> result= Lists.emptyList();
        QueryWrapper queryWrapper=new QueryWrapper<QueryWrapper>().select(CustomerAccount.CUSTOMER_ID)
                .between(CustomerAccount.CREATE_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59));
        List<CustomerAccount> accounts=baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(accounts)){
            result=accounts.stream().map(customerAccount -> customerAccount.getCustomerId()).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public CustomerAccount findByCustomerId(Long customerId, Long merchantId) {
        return baseMapper.selectOne(new QueryWrapper<CustomerAccount>().eq(CustomerAccount.CUSTOMER_ID,customerId).eq(CustomerAccount.MERCHANT_ID,merchantId));
    }

    @Override
    public Integer getNewMemberCount(Long merchantId, LocalDate date) {
        return baseMapper.selectCount(new QueryWrapper<CustomerAccount>().between(CustomerAccount.CREATE_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59)).eq(CustomerAccount.MERCHANT_ID,merchantId));
    }


}
