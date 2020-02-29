package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.MerchantWxCardDao;
import com.fate.common.entity.MerchantWxCard;
import com.fate.common.enums.WxCardType;
import com.fate.common.mapper.MerchantWxCardMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 商户微信卡券表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-07
 */
@Repository
public class MerchantWxCardDaoImpl extends ServiceImpl<MerchantWxCardMapper, MerchantWxCard> implements MerchantWxCardDao {

    @Override
    public MerchantWxCard getByCardType(WxCardType type) {
        return baseMapper.selectOne(new QueryWrapper<MerchantWxCard>().eq(MerchantWxCard.CARD_TYPE,type).eq(MerchantWxCard.ENABLED,true));
    }

    @Override
    public MerchantWxCard getByCardType(WxCardType type, Long merchantId) {
        return baseMapper.selectOne(new QueryWrapper<MerchantWxCard>().eq(MerchantWxCard.CARD_TYPE,type).eq(MerchantWxCard.ENABLED,true).eq(MerchantWxCard.MERCHANT_ID,merchantId));
    }
}
