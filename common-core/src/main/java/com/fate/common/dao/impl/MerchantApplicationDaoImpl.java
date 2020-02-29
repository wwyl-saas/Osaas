package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.MerchantApplicationDao;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.enums.ApplicationType;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.MerchantApplicationMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * 商户app表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class MerchantApplicationDaoImpl extends ServiceImpl<MerchantApplicationMapper, MerchantApplication> implements MerchantApplicationDao {

    @Override
    public List<MerchantApplication> findAllEnable(List<ApplicationType> asList) {
        Assert.notEmpty(asList, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectApplicationList(new QueryWrapper<MerchantApplication>().eq(MerchantApplication.ENABLED,true).in(MerchantApplication.TYPE,asList));
    }

    @Override
    public MerchantApplication getMpApplication(Long merchantId) {
        return baseMapper.selectOne(new QueryWrapper<MerchantApplication>().eq(MerchantApplication.MERCHANT_ID,merchantId).eq(MerchantApplication.TYPE,ApplicationType.WX_PUBLIC));
    }
}
