package com.fate.api.admin.service;

import com.fate.common.dao.CustomerChargeDao;
import com.fate.common.model.StatisticModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-25 18:01
 **/
@Service
@Slf4j
public class CustomerChargeService {
    @Resource
    CustomerChargeDao customerChargeDao;

    public List<StatisticModel> getChargeCount(Long merchantId, LocalDate date) {
        return customerChargeDao.getChargeCount(merchantId,date);
    }

    public List<StatisticModel> getChargeAmount(Long merchantId, LocalDate date) {
        return customerChargeDao.getChargeAmount(merchantId,date);
    }

    public List<StatisticModel> getFactChargeAmount(Long merchantId, LocalDate date) {
        return customerChargeDao.getFactChargeAmount(merchantId,date);
    }

    public List<StatisticModel> getGiftChargeAmount(Long merchantId, LocalDate date) {
        return customerChargeDao.getGiftChargeAmount(merchantId,date);
    }
}
