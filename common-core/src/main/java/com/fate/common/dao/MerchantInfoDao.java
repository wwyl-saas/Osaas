package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.MerchantInfo;

/**
 * <p>
 * 商户详情表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MerchantInfoDao extends IService<MerchantInfo> {

    MerchantInfo findMerchantInfo();

    MerchantInfo getMerchantInfoByMerchantId(Long merchantId);
}
