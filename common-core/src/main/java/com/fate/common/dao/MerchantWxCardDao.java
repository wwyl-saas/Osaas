package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.MerchantWxCard;
import com.fate.common.enums.WxCardType;

/**
 * <p>
 * 商户微信卡券表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-07
 */
public interface MerchantWxCardDao extends IService<MerchantWxCard> {

    MerchantWxCard getByCardType(WxCardType memberCard);

    MerchantWxCard getByCardType(WxCardType memberCard, Long merchantId);
}
