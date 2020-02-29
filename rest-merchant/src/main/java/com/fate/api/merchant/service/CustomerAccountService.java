package com.fate.api.merchant.service;

import com.fate.common.dao.CustomerAccountDao;
import com.fate.common.dao.MemberDao;
import com.fate.common.entity.CustomerAccount;
import com.fate.common.entity.Member;
import com.fate.common.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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


    public Member getByMerchantMember(Integer memberLevel) {
        return memberDao.findByMemberLevel(memberLevel);
    }

    public boolean updateAccount(CustomerAccount customerAccount) {
        return customerAccountDao.updateById(customerAccount);
    }

    /**
     * 计算订单折扣金额
     * @param order
     */
    public BigDecimal caculateDiscount(Order order) {
        //TODO 会员折扣计算
        return BigDecimal.valueOf(10L);
    }

    /**
     * 获取今日新增长会员数量
     * @return
     */
    public Integer getTodayNewMemberCount() {
        return customerAccountDao.getNewMemberCount(LocalDate.now());
    }

    public List<Long> getCustomerIdsBy(LocalDate date) {
        return customerAccountDao.getCustomerIdsBy(date);
    }

    /**
     * 账户余额消费
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
}
