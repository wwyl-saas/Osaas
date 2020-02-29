package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.MerchantInfoDao;
import com.fate.common.entity.MerchantInfo;
import com.fate.common.mapper.MerchantInfoMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 商户详情表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class MerchantInfoDaoImpl extends ServiceImpl<MerchantInfoMapper, MerchantInfo> implements MerchantInfoDao {

    @Override
    public MerchantInfo findMerchantInfo() {
        return baseMapper.selectOne(null);
    }

    @Override
    public MerchantInfo getMerchantInfoByMerchantId(Long merchantId) {
        return baseMapper.getByMerchantId(merchantId);
    }
}
