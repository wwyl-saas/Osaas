package com.fate.api.admin.service;

import com.fate.common.dao.CustomerAccountDao;
import com.fate.common.entity.CustomerAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @program: parent
 * @description: 用户账户
 * @author: chenyixin
 * @create: 2019-09-25 16:53
 **/
@Service
@Slf4j
public class CustomerAccountService {
    @Resource
    CustomerAccountDao customerAccountDao;

    public CustomerAccount findByCustomerId(Long customerId, Long merchantId) {
        return customerAccountDao.findByCustomerId(customerId,merchantId);
    }

    public Integer getNewMemberCount(Long merchantId, LocalDate date) {
        return customerAccountDao.getNewMemberCount(merchantId,date);
    }
}
