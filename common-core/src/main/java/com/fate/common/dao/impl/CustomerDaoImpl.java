package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CustomerDao;
import com.fate.common.entity.Customer;
import com.fate.common.enums.CustomerSource;
import com.fate.common.enums.MemberLevel;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.CustomerMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * C端用户表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class CustomerDaoImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerDao {

    @Override
    public Customer getByUnionId(String unionId) {
        Assert.notNull(unionId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectOne(new QueryWrapper<Customer>().eq(Customer.WX_UNOINID,unionId));
    }

    @Override
    public Customer getByOpenId(String openid,Long applicationId,Long merchantId) {
        return baseMapper.selectOne(new QueryWrapper<Customer>()
                .inSql(Customer.ID,"select customer_id from t_customer_application where merchant_id = "
                        +merchantId+" and merchant_app_id = "+applicationId+" and wx_openid = "+openid).last(" limit 1 "));
    }

    @Override
    public Integer getNewCustomerCount(LocalDate date) {
        return baseMapper.selectCount(new QueryWrapper<Customer>().between(Customer.CREATE_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59)));
    }

    @Override
    public Customer getByPhone(String customerPhone) {
        return baseMapper.selectOne(new QueryWrapper<Customer>().eq(Customer.MOBILE,customerPhone));
    }

    @Override
    public IPage<Customer> getPageByMobileAndName(Long pageIndex, Long pageSize, String mobile, String customerName, Long merchantId) {
        return baseMapper.getPageByCustomer(new Page(pageIndex,pageSize),new Customer().setMobile(mobile).setName(customerName).setMerchantId(merchantId));
    }

    @Override
    public IPage<Customer> getPageByMobileAndNameAndLevelAndSource(Long pageIndex, Long pageSize, String mobile, String customerName, MemberLevel memberLevel, CustomerSource customerSource, Long merchantId) {
        return baseMapper.getPageByCustomer(new Page(pageIndex,pageSize),new Customer().setMobile(mobile).setName(customerName).setMerchantId(merchantId).setSource(customerSource).setMemberLevel(memberLevel));
    }

    @Override
    public Integer getVisitCount(LocalDate date) {
        return baseMapper.selectCount(new QueryWrapper<Customer>().between(Customer.LAST_LOGIN_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59)));
    }

    @Override
    public IPage<Customer> getPageBy(List<Long> customerIds, Integer pageIndex, Integer pageSize) {
        Assert.notEmpty(customerIds,ResponseInfo.PARAM_NULL.getMsg());
        QueryWrapper queryWrapper=new QueryWrapper<Customer>().in(Customer.ID,customerIds);
        return baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);
    }

    @Override
    public IPage<Customer> getPageByCreateDate(LocalDate date, Integer pageIndex, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageIndex,pageSize),new QueryWrapper<Customer>().between(Customer.CREATE_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59)));
    }

    @Override
    public IPage<Customer> getpageByLastloginDate(LocalDate date, Integer pageIndex, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageIndex,pageSize),new QueryWrapper<Customer>().between(Customer.LAST_LOGIN_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59)));
    }

    @Override
    public IPage<Customer> getPageByMobile(Long pageIndex, Long pageSize, String mobile) {
        return baseMapper.getPageByMobile(new Page(pageIndex,pageSize),mobile);
    }

    @Override
    public List<Map<String, Object>> getCustmerNamesByIds(List<Long> customerIdsList) {
        return baseMapper.getCustmerNamesByIds(customerIdsList);
    }

    @Override
    public Integer getVisitCount(Long merchantId, LocalDate date) {
        return baseMapper.selectCount(new QueryWrapper<Customer>().between(Customer.LAST_LOGIN_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59)).eq(Customer.MERCHANT_ID,merchantId));
    }

    @Override
    public Integer getNewCustomerCount(Long merchantId, LocalDate date) {
        return baseMapper.selectCount(new QueryWrapper<Customer>().between(Customer.CREATE_TIME,date.atTime(LocalTime.MIN),date.atTime(23,59,59)).eq(Customer.MERCHANT_ID,merchantId));
    }


}
