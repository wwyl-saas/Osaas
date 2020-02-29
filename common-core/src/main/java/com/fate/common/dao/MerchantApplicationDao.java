package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.enums.ApplicationType;

import java.util.List;

/**
 * <p>
 * 商户app表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MerchantApplicationDao extends IService<MerchantApplication> {

    List<MerchantApplication> findAllEnable(List<ApplicationType> asList);

    MerchantApplication getMpApplication(Long merchantId);
}
