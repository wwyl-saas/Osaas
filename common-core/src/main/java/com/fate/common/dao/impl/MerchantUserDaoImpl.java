package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.MerchantUserDao;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.MerchantUser;
import com.fate.common.entity.MerchantUserRole;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.MerchantUserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户用户表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class MerchantUserDaoImpl extends ServiceImpl<MerchantUserMapper, MerchantUser> implements MerchantUserDao {

    @Override
    public List<MerchantUser> getMerchantUserByIds(List<Long> ids) {
        Assert.notEmpty(ids, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<MerchantUser>().in(MerchantUser.ID,ids));
    }

    @Override
    public MerchantUser getByAppletOpenId(String openId) {
        Assert.notNull(openId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.getByAppletOpenId(openId);
    }

    @Override
    public MerchantUser getByMobile(String phone) {
        Assert.notNull(phone, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.getByMobile(phone);
    }

    @Override
    public void updatePsw(Long userId, String psw) {
        MerchantUser user = new MerchantUser();
        user.setPassword(psw);
        baseMapper.update(user, Wrappers.<MerchantUser>lambdaUpdate().eq(MerchantUser::getId, userId));
    }

    @Override
    public void updateUser(MerchantUser user) {
        baseMapper.updateUser(user);
    }

    @Override
    public MerchantUser getByUserId(Long userId) {
        return baseMapper.getByUserId(userId);
    }

    @Override
    public IPage<MerchantUser> getPageByShopId(Integer pageIndex, Integer pageSize,  Long shopId,Long merchantId) {
        Assert.notNull(shopId,ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.getPageByShopId(new Page<>(pageIndex,pageSize),new MerchantUserRole().setShopId(shopId).setMerchantId(merchantId));
    }

    @Override
    public List<MerchantUser> getByShopId(Long shopId,Long merchantId) {
        Assert.notNull(shopId,ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.getByShopId(shopId, merchantId);
    }

    @Override
    public MerchantUser getByMerchantMobile(String mobile) {
        return baseMapper.selectOne(new QueryWrapper<MerchantUser>().eq(MerchantUser.MOBILE,mobile));
    }

    @Override
    public List<MerchantUser> getByShopIdAndName(Long shopId, String userName, Long merchantId) {
        Assert.notNull(shopId,ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.getByShopIdAndName(shopId,userName, merchantId);
    }

    @Override
    public IPage<MerchantUser> getPageByMobileAndName(Integer pageIndex, Integer pageSize, String mobile, String merchantUserName,Long merchantId) {
        return baseMapper.getPageByMobileAndName(new Page<>(pageIndex,pageSize), mobile,merchantUserName,merchantId);
    }

    @Override
    public IPage<MerchantUser> queryMerchantUsersByRoleType(Integer pageIndex, Integer pageSize, Integer roleType) {
        return baseMapper.getPageByByRoleType(new Page<>(pageIndex,pageSize),roleType);
    }


    @Override
    public List<MerchantUser> queryUsersByIds(List<Long> userIds) {
        return baseMapper.selectList(new QueryWrapper<MerchantUser>().in(MerchantUser.ID,userIds));
    }

    @Override
    public List<Map<String,Object>> getIdAndNameByGoodsId(Long goodsId) {
        return baseMapper.getIdAndNameByGoodsId(goodsId);

    }

    @Override
    public MerchantUser getByUnionId(String wxUnoinid) {
        return baseMapper.getByUnionId(wxUnoinid);
    }

    @Override
    public List<MerchantUser> getByUserIds(List<Long> userIds) {
        return baseMapper.selectList(new QueryWrapper<MerchantUser>().in(MerchantUser.ID,userIds));
    }

    @Override
    public List<MerchantUser> getAllUsers(Long merchantId) {
        return baseMapper.selectList(new QueryWrapper<MerchantUser>().eq(MerchantUser.MERCHANT_ID,merchantId));
    }


}
