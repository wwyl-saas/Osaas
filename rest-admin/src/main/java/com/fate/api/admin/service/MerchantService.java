package com.fate.api.admin.service;

import com.fate.common.dao.MerchantDao;
import com.fate.common.entity.Merchant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-05 10:17
 **/
@Service
@Slf4j
public class MerchantService {
    @Resource
    MerchantDao merchantDao;

    @Cacheable(value = "merchants" ,unless="#result == null", key="#merchantId")
    public Merchant getById(long merchantId) {
        return merchantDao.getById(merchantId);
    }

    /**
     * 查询所有可用的商户
     * @return
     */
    public List<Merchant> getAllEnable() {
        return merchantDao.getAll(true);
    }

}
