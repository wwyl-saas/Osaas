package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.CustomerAccount;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * C端用户账户表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface CustomerAccountDao extends IService<CustomerAccount> {

    CustomerAccount findByCustomerId(Long customerId);

    Integer getNewMemberCount(LocalDate date);

    List<Long> getCustomerIdsBy(LocalDate date);

    CustomerAccount findByCustomerId(Long customerId, Long merchantId);

    Integer getNewMemberCount(Long merchantId, LocalDate date);
}
