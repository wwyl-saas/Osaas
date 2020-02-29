package com.fate.api.customer.service;

import com.fate.common.dao.CustomerAccountDao;
import com.fate.common.dao.MemberDao;
import com.fate.common.entity.Customer;
import com.fate.common.entity.CustomerAccount;
import com.fate.common.entity.Member;
import com.fate.common.enums.MemberLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @program: parent
 * @description: 会员及账户相关
 * @author: chenyixin
 * @create: 2019-06-02 13:14
 **/
@Service
@Slf4j
public class CustomerAccountService {
    @Resource
    CustomerAccountDao customerAccountDao;
    @Resource
    MemberDao memberDao;

    public CustomerAccount findByCustomerId(Long customerId) {
        return customerAccountDao.findByCustomerId(customerId);
    }


    public boolean updateAccount(CustomerAccount customerAccount) {
        return customerAccountDao.updateById(customerAccount);
    }

    /**
     * 创建会员账户
     *
     * @param customer
     * @param initBonus
     * @param cardCode
     * @return
     */
    public CustomerAccount createAccount(Customer customer, Integer initBonus, String cardCode) {
        CustomerAccount customerAccount = customerAccountDao.findByCustomerId(customer.getId());
        Assert.isNull(customerAccount, "账户已经存在");
        customerAccount = new CustomerAccount();
        customerAccount.setConsumeScore(initBonus);
        customerAccount.setCumulativeRecharge(BigDecimal.ZERO);
        customerAccount.setBalance(BigDecimal.ZERO);
        customerAccount.setConsumeNum(0);
        customerAccount.setChargeNum(0);
        customerAccount.setCustomerId(customer.getId());
        customerAccount.setMemberLevel(MemberLevel.COMMON);
        Member member=memberDao.findByMemberLevel(MemberLevel.COMMON.getCode());
        Assert.notNull(member,"商户会员等级未配置");
        customerAccount.setMemberId(member.getId());
        customerAccount.setWxCardCode(cardCode);
        Assert.isTrue(customerAccount.insert(), "数据插入失败");
        return customerAccount;
    }


    /**
     * 账户余额支付
     * @param payAmount
     * @param customerAccount
     */
    public CustomerAccount consumeByBalance(BigDecimal payAmount, CustomerAccount customerAccount) {
        customerAccount.setBalance(customerAccount.getBalance().subtract(payAmount));
        customerAccount.setConsumeNum(customerAccount.getConsumeNum() + 1);
        customerAccount.setConsumeScore(customerAccount.getConsumeScore()+payAmount.intValue());
        customerAccount.setCumulativeConsume(customerAccount.getCumulativeConsume().add(payAmount));
        customerAccount.setUpdateTime(null);
        Assert.isTrue(customerAccount.updateById(), "数据更新失败");
        return customerAccount;
    }

    /**
     * 非账户余额方式支付
     * @param payAmount
     * @param customerAccount
     * @return
     */
    public CustomerAccount consumeNotBalance(BigDecimal payAmount, CustomerAccount customerAccount) {
        //积分以分单位计算
        Integer payment = payAmount.intValue();
        customerAccount.setConsumeScore(customerAccount.getConsumeScore() + payment);
        customerAccount.setConsumeNum(customerAccount.getConsumeNum() + 1);
        customerAccount.setCumulativeConsume(customerAccount.getCumulativeConsume().add(payAmount));
        customerAccount.setUpdateTime(null);
        Assert.isTrue(customerAccount.updateById(), "数据更新失败");
        return customerAccount;
    }
}
