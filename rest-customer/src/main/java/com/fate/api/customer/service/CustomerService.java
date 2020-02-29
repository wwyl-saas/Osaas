package com.fate.api.customer.service;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.fate.api.customer.dto.CustomerDto;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.dao.CustomerApplicationDao;
import com.fate.common.dao.CustomerDao;
import com.fate.common.dao.MerchantWxCardDao;
import com.fate.common.entity.*;
import com.fate.common.enums.CustomerSource;
import com.fate.common.enums.Gender;
import com.fate.common.enums.WxCardType;
import com.fate.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Optional;

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
    CustomerApplicationDao customerApplicationDao;
    @Resource
    CustomerDao customerDao;
    @Resource
    MerchantWxCardDao merchantWxCardDao;


    @CacheEvict(value="customers",key="#customer.id")
    public boolean updateCustomer(Customer customer) {
       return customerDao.updateById(customer);
    }

    public Customer findById(long userId) {
        return customerDao.getById(userId);
    }


    /**
     * 获取用户个人中心页信息
     *
     * @return
     */
    public CustomerDto getPersonIndex() {
        Customer customer = CurrentCustomerUtil.getCustomer();
        CustomerDto result = BeanUtil.mapper(customer,CustomerDto.class);

        Optional<CustomerAccount> customerAccount = Optional.ofNullable(customerAccountService.findByCustomerId(customer.getId()));
        customerAccount.ifPresent(account -> {
            result.setBalance(account.getBalance());
            result.setConsumeScore(account.getConsumeScore());
            result.setMemberId(account.getWxCardCode());
            result.setMemberLevel(account.getMemberLevel().getCode());
            result.setMemberLevelName(account.getMemberLevel().getDesc());
        });

        if (StringUtils.isBlank(result.getMemberId())){
            MerchantWxCard merchantWxCard=merchantWxCardDao.getByCardType(WxCardType.MEMBER_CARD);
            Assert.notNull(merchantWxCard,"无有效的会员卡套");
            result.setWxMemberCardId(merchantWxCard.getCardId());
        }
        return result;
    }

    /**
     * 创建用户对象
     *
     * @param userInfo
     * @param unionid
     * @return
     */
    public Customer createCustomer(WxMaUserInfo userInfo, String unionid) {
        Customer customer = new Customer()
                .setAvatar(userInfo.getAvatarUrl())
                .setGender(Gender.getEnum(Integer.parseInt(userInfo.getGender())))
                .setLastLoginTime(LocalDateTime.now())
                .setNickname(userInfo.getNickName())
                .setWxUnoinid(unionid)
                .setSource(CustomerSource.WX_ONLINE);
//        private String province;
        //customer.setCityId(0);//TODO
        customerDao.save(customer);
        return customer;
    }

    /**
     * 修改用户对象
     *
     * @param customer
     * @param userInfo
     * @param unionid
     * @return
     */
    public Customer changeCustomerInfo(Customer customer, WxMaUserInfo userInfo, String unionid) {
        customer.setWxUnoinid(unionid);
        customer.setNickname(userInfo.getNickName());
        customer.setAvatar(userInfo.getAvatarUrl());
        customer.setGender(Gender.getEnum(Integer.parseInt(userInfo.getGender())));
        customer.setLastLoginTime(LocalDateTime.now());
        customer.setMerchantId(null);//租户插件有问题，导致赋值两次
        //customer.setCityId(0);//TODO
        return customer;
    }


    /**
     * 通过应用ID和用户UnionID查询用户（最多一条）
     *
     * @param union
     * @return
     */
    public Customer getByUnionId(String union) {
        return customerDao.getByUnionId(union);
    }

    /**
     * 查询商户当前应用下的用户（最多一条,当一条也不存在时sql会报错）
     *
     * @param openid
     * @param applicationId
     * @param merchantId
     * @return
     */
    public Customer getByOpenId(String openid, Long applicationId, Long merchantId) {
        return customerDao.getByOpenId(openid, applicationId, merchantId);
    }

    /**
     * 查询商户当前应用下的用户（最多一条）
     *
     * @param openid
     * @param applicationId
     * @return
     */
    public Customer getByOpenId(String openid, Long applicationId) {
        Customer customer = null;
        CustomerApplication customerApplication = customerApplicationDao.getByApplicationIdAndOpenId(openid, applicationId);
        if (customerApplication != null) {//用户存在
            customer = customerDao.getById(customerApplication.getCustomerId());
            Assert.notNull(customer, "用户应用关联表查询的用户不存在");
        }
        return customer;
    }

    /**
     * 用户Id获取customerApplication
     *
     * @param customerId
     * @param applicationId
     * @return
     */
    @Cacheable(value = "customerApplications", unless = "#result == null", key = "T(String).valueOf(#customerId).concat(':').concat(#applicationId)")
    public CustomerApplication getCustomerApplicationByCustomerIdAndApplicationId(Long customerId, Long applicationId) {
        return customerApplicationDao.getByCustomerIdAndApplicationId(customerId, applicationId);
    }

    /**
     * 应用的openId获取customerApplication
     *
     * @param wxOpenId
     * @param applicationId
     * @return
     */
    public CustomerApplication getCustomerApplicationByApplicationIdAndOpenId(String wxOpenId, Long applicationId) {
        return customerApplicationDao.getByApplicationIdAndOpenId(wxOpenId, applicationId);
    }

    /**
     * 用户Id获取用户信息
     *
     * @param userId
     * @return
     */
    @Cacheable(value = "customers", unless = "#result == null", key = "#userId")
    public Customer getByCustomerId(long userId) {
        return customerDao.getById(userId);
    }

}
