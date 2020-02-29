package com.fate.api.admin.service;

import com.fate.common.dao.CustomerDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-25 17:58
 **/
@Service
@Slf4j
public class CustomerService {
    @Resource
    CustomerDao customerDao;

    public Integer getVisitCount(Long merchantId, LocalDate yesterday) {
        return customerDao.getVisitCount(merchantId,yesterday);
    }

    public Integer getNewCustomerCount(Long merchantId, LocalDate yesterday) {
        return customerDao.getNewCustomerCount(merchantId,yesterday);
    }
}
