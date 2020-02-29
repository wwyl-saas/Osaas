package com.fate.api.admin.service;

import com.fate.common.dao.MerchantShopDao;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.MerchantShop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-27 00:13
 **/
@Service
@Slf4j
public class MerchantShopService {
    @Resource
    MerchantShopDao merchantShopDao;

    public List<MerchantShop> getAllEnable(Long merchantId) {
        return merchantShopDao.getAllEnable(merchantId);
    }
}
