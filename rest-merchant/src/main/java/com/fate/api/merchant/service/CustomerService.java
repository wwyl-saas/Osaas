package com.fate.api.merchant.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.api.merchant.dto.CustomerConsumeDto;
import com.fate.api.merchant.dto.CustomerDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.common.dao.CustomerDao;
import com.fate.common.entity.Customer;
import com.fate.common.entity.CustomerAccount;
import com.fate.common.entity.CustomerConsume;
import com.fate.common.enums.CustomerSource;
import com.fate.common.enums.MemberLevel;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: C端客户相关
 * @author: chenyixin
 * @create: 2019-06-02 11:00
 **/
@Service
@Slf4j
public class CustomerService {
    @Autowired
    CustomerAccountService customerAccountService;
    @Resource
    CustomerDao customerDao;
    @Autowired
    ChargeService chargeService;


    /**
     * 获取会员用户列表，包含会员级别信息
     * @param pageIndex
     * @param pageSize
     * @param query
     * @return
     */
    public PageDto<CustomerDto> getCustomerList(Long pageIndex, Long pageSize, String query, MemberLevel memberLevel, CustomerSource customerSource) {
        Long merchantId= CurrentMerchantUtil.getMerchant().getId();
        List<CustomerDto> list= Lists.emptyList();
        IPage<Customer> page;
        String mobile=null;
        String customerName=null;
        if (StringUtils.isNotBlank(query)){
            if (StringUtils.isNumeric(query)){
                mobile=query;
            }else {
                customerName=query;
            }
        }

        page = customerDao.getPageByMobileAndNameAndLevelAndSource(pageIndex,pageSize,mobile,customerName,memberLevel,customerSource,merchantId);
        if (CollectionUtils.isNotEmpty(page.getRecords())){
            list=page.getRecords().stream().map(customer -> {
                CustomerDto dto= BeanUtil.mapper(customer,CustomerDto.class);
                if (customer.getMemberLevel()!=null){
                    dto.setMemberLevelCode(customer.getMemberLevel().getCode());
                }
                return dto;
            }).collect(Collectors.toList());
        }
        return new PageDto<>(pageIndex,pageSize,page.getTotal(),page.getPages(),list);
    }


    /**
     * 获取用户详情
     * @param userId
     * @return
     */
    public CustomerDto detail(Long userId) {
        Customer customer = customerDao.getById(userId);
        Assert.notNull(customer,"会员不存在");
        CustomerDto dto=BeanUtil.mapper(customer,CustomerDto.class);
        CustomerAccount customerAccount=customerAccountService.findByCustomerId(customer.getId());
        if (customerAccount!=null){
            dto.setBalance(customerAccount.getBalance());
            dto.setConsumeScore(customerAccount.getConsumeScore());
            dto.setMemberId(customerAccount.getWxCardCode());
            dto.setMemberLevel(customerAccount.getMemberLevel());
            dto.setMemberLevelCode(customerAccount.getMemberLevel().getCode());
            dto.setConsumeNum(customerAccount.getConsumeNum());
            dto.setChargeNum(customerAccount.getChargeNum());
            dto.setCumulativeRecharge(customerAccount.getCumulativeRecharge());
            dto.setCumulativeConsume(customerAccount.getCumulativeConsume());
        }
        return dto;
    }

    public Customer getById(Long customerId) {
        return customerDao.getById(customerId);
    }



    /**
     * 通过手机号查询商户下面唯一的用户
     * @param customerPhone
     * @return
     */
    public Customer getCustomerByPhone(String customerPhone) {
        return customerDao.getByPhone(customerPhone);
    }

    /**
     * 获取今日访问量
     * @return
     */
    public Integer getTodayVisitCount() {
        return customerDao.getVisitCount(LocalDate.now());
    }

    /**
     * 查询今日新增会员数量
     * @return
     */
    public Integer getTodayNewCustomerCount() {
        return customerDao.getNewCustomerCount(LocalDate.now());
    }

    /**
     * 获取新充值会员列表
     * @param shopId
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public IPage<Customer> getRechargePage(Long shopId, LocalDate date, Integer pageIndex, Integer pageSize) {
        IPage<Customer> result=new Page<>();
        List<Long> customerIds=chargeService.getCustomerIdsBy(shopId,date);
        if (CollectionUtils.isNotEmpty(customerIds)){
            result=customerDao.getPageBy(customerIds,pageIndex,pageSize);
        }
        return result;
    }

    /**
     * 获取新激活会员列表
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public IPage<Customer> getNewMemberPage(LocalDate date, Integer pageIndex, Integer pageSize) {
        IPage<Customer> result=new Page<>();
        List<Long> customerIds=customerAccountService.getCustomerIdsBy(date);
        if (CollectionUtils.isNotEmpty(customerIds)){
            result=customerDao.getPageBy(customerIds,pageIndex,pageSize);
        }
        return result;
    }

    /**
     * 获取新授权会员
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public IPage<Customer> getNewcustomerPage(LocalDate date, Integer pageIndex, Integer pageSize) {
        return customerDao.getPageByCreateDate(date,pageIndex,pageSize);
    }

    public IPage<Customer> getLoginCustomer(LocalDate date, Integer pageIndex, Integer pageSize) {
        return customerDao.getpageByLastloginDate(date,pageIndex,pageSize);
    }

    public List<Customer> getListByIds(List<Long> customerIds) {
        return (List<Customer>) customerDao.listByIds(customerIds);
    }

    /**
     * 根据手机号查询会员信息
     * @param pageIndex
     * @param pageSize
     * @param mobile
     * @return
     */
    public PageDto<CustomerDto> getCustomerListBymobile(Long pageIndex, Long pageSize, String mobile) {
        List<CustomerDto> list= Lists.emptyList();
        IPage<Customer> page;
        page = customerDao.getPageByMobile(pageIndex,pageSize,mobile);
        if (CollectionUtils.isNotEmpty(page.getRecords())){
            list=page.getRecords().stream().map(customer -> {
                CustomerDto dto= BeanUtil.mapper(customer,CustomerDto.class);
                if (customer.getMemberLevel()!=null){
                    dto.setMemberLevelCode(customer.getMemberLevel().getCode());
                }
                dto.setConsumeScore(customer.getConsumeScore());
                dto.setBalance(customer.getBalance());
                return dto;
            }).collect(Collectors.toList());
        }
        return new PageDto<>(pageIndex,pageSize,page.getTotal(),page.getPages(),list);

    }


    public List<Map<String,Object>> getCustmerNamesByIds(List<Long> customerIdsList) {
       return  customerDao.getCustmerNamesByIds(customerIdsList);
    }




}
