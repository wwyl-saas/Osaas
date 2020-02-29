package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.GoodsShopDao;
import com.fate.common.entity.GoodsShop;
import com.fate.common.entity.MerchantUserRole;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.GoodsShopMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * 服务门店表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class GoodsShopDaoImpl extends ServiceImpl<GoodsShopMapper, GoodsShop> implements GoodsShopDao {

    @Override
    public List<Long> getShopIdsByGoodsId(Long goodsId) {
        Assert.notNull(goodsId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectShopIdsByGoodsId(goodsId);
    }

    @Override
    public List<GoodsShop> getGoodsByShopId(Long shopId) {
        Assert.notNull(shopId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<GoodsShop>().eq(GoodsShop.SHOP_ID,shopId));
    }

    @Override
    public List<Long> getGoodsIdsByShopId(Long shopId) {
        Assert.notNull(shopId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.getGoodsIdsByShopId(shopId);
    }

    @Override
    public void removeByShopId(Long shopId) {
        Assert.notNull(shopId, ResponseInfo.PARAM_NULL.getMsg());
        baseMapper.delete(Wrappers.<GoodsShop>lambdaQuery().in(GoodsShop::getShopId,shopId));
    }

    @Override
    public void deleteByGoodsId(Long goodsId) {
        Assert.notNull(goodsId, ResponseInfo.PARAM_NULL.getMsg());
        baseMapper.delete(Wrappers.<GoodsShop>lambdaQuery().in(GoodsShop::getGoodsId,goodsId));
    }

}
