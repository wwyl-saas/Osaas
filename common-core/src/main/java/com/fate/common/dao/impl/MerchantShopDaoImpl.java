package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.MerchantShopDao;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.MerchantShop;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.MerchantShopMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户门店表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class MerchantShopDaoImpl extends ServiceImpl<MerchantShopMapper, MerchantShop> implements MerchantShopDao {

    @Override
    public List<MerchantShop> selectAll() {
        return baseMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public List<MerchantShop> getByWithShopRoles(Long merchantId,Long userId) {
        Assert.notNull(merchantId, ResponseInfo.PARAM_NULL.getMsg());
        Assert.notNull(userId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.getShopsWithRole(merchantId,userId);
    }

    @Override
    public IPage<MerchantShop> getMerchantShopByNamePage(Integer pageIndex, Integer pageSize, String name) {
        QueryWrapper<MerchantShop> queryWrapper = new QueryWrapper<MerchantShop>();
        if(name!=null && name!=""){
            queryWrapper.like(MerchantShop.NAME,name);
        }
        return  baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);


    }

    @Override
    public List<Map<String, Object>> getIdAndNameByGoodsId(Long goodsId) {
        return  baseMapper.getIdAndNameByGoodsId(goodsId);

    }

    @Override
    public List<Map<String, Object>> getshopNamesByIds(List<Long> shopIdsList) {
        return baseMapper.getshopNamesByIds(shopIdsList);
    }

    @Override
    public List<MerchantShop> getAllEnable(Long merchantId) {
        return baseMapper.selectList(new QueryWrapper<MerchantShop>().eq(MerchantShop.MERCHANT_ID,merchantId).eq(MerchantShop.ENABLED,true));
    }

    @Override
    public List<MerchantShop> getByIds(List<Long> shopIds) {
        Assert.notEmpty(shopIds,ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<MerchantShop>().in(MerchantShop.ID,shopIds));
    }
}
