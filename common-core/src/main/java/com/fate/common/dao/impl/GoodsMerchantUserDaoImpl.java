package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.GoodsMerchantUserDao;
import com.fate.common.entity.GoodsMerchantUser;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.GoodsMerchantUserMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * 商品和技师表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-06
 */
@Repository
public class GoodsMerchantUserDaoImpl extends ServiceImpl<GoodsMerchantUserMapper, GoodsMerchantUser> implements GoodsMerchantUserDao {

    @Override
    public List<GoodsMerchantUser> getByGoodsId(Long goodsId) {
        Assert.notNull(goodsId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<GoodsMerchantUser>().eq(GoodsMerchantUser.GOODS_ID,goodsId));
    }

    @Override
    public List<GoodsMerchantUser> getByMerchantUserId(Long merchantUserId) {
        Assert.notNull(merchantUserId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<GoodsMerchantUser>().eq(GoodsMerchantUser.MERCHANT_USER_ID,merchantUserId));
    }

    @Override
    public void deleteByUserId(Long userId) {
        Assert.notNull(userId, ResponseInfo.PARAM_NULL.getMsg());
        baseMapper.delete(new QueryWrapper<GoodsMerchantUser>().eq(GoodsMerchantUser.MERCHANT_USER_ID,userId));
    }

    @Override
    public void deleteByGoodsId(Long goodsId) {
        Assert.notNull(goodsId, ResponseInfo.PARAM_NULL.getMsg());
        baseMapper.delete(new QueryWrapper<GoodsMerchantUser>().eq(GoodsMerchantUser.GOODS_ID,goodsId));
    }


}
