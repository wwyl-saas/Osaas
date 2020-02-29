package com.fate.api.merchant.service;

import com.fate.common.dao.PayForAnotherDao;
import com.fate.common.entity.PayForAnother;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: parent
 * @description: 代人支付
 * @author: chenyixin
 * @create: 2019-07-22 17:11
 **/
@Service
@Slf4j
public class PayForAnotherService {
    @Resource
    PayForAnotherDao payForAnotherDao;

    public PayForAnother getByPayCode(Long code) {
       return payForAnotherDao.getByPayCode(code);
    }
}
