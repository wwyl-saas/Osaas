package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.MerchantUserRoleDao;
import com.fate.common.entity.MerchantUser;
import com.fate.common.entity.MerchantUserRole;
import com.fate.common.entity.OrderDesc;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.enums.UserRoleType;
import com.fate.common.mapper.MerchantUserRoleMapper;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * 商户用户角色表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class MerchantUserRoleDaoImpl extends ServiceImpl<MerchantUserRoleMapper, MerchantUserRole> implements MerchantUserRoleDao {

    @Override
    public List<MerchantUserRole> getMerchantUserByShopId(Long shopId) {
        Assert.notNull(shopId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<MerchantUserRole>().eq(MerchantUserRole.SHOP_ID,shopId));
    }

    @Override
    public List<Long> getMerchantUserIdsByShopId(Long shopId) {
        return baseMapper.getMerchantUserIdsByShopId(shopId);
    }

    @Override
    public MerchantUserRole getShopRoleByShopAndUserId(Long shopId, Long userId) {
        Assert.notNull(shopId, ResponseInfo.PARAM_NULL.getMsg());
        Assert.notNull(userId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectOne(new QueryWrapper<MerchantUserRole>().eq(MerchantUserRole.SHOP_ID,shopId).eq(MerchantUserRole.USER_ID,userId));
    }

    @Override
    public MerchantUserRole getMerchantRoleByUserId(Long userId) {
        Assert.notNull(userId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectOne(new QueryWrapper<MerchantUserRole>().eq(MerchantUserRole.SHOP_ID,0)
                .eq(MerchantUserRole.USER_ID,userId).in(MerchantUserRole.ROLE_TYPE, Lists.list(UserRoleType.MANAGER,UserRoleType.MANAGER_ASSISTANT)));
    }

    @Override
    public MerchantUserRole getMerchantRoleByUserId(Long userId, Long merchantId) {
        Assert.notNull(userId, ResponseInfo.PARAM_NULL.getMsg());
        Assert.notNull(merchantId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.getMerchantRoleByUserId(userId,merchantId);
    }

    @Override
    public IPage<MerchantUserRole> getByMerchantByRoleTypePage(Integer pageIndex,Integer pageSize,Integer roleType) {
        QueryWrapper<MerchantUserRole> queryWrapper = new QueryWrapper<MerchantUserRole>();
        if(roleType!=null){
            queryWrapper.eq(MerchantUserRole.ROLE_TYPE,roleType);
        }
        return  baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);

    }

    @Override
    public List<Long> queryCandidatesByRole(Integer roleType, Long shopId) {
        return baseMapper.queryCandidatesByRole(roleType,shopId);
    }

    @Override
    public List<Long> queryUsersByRole(Integer roleType, Long shopId) {
        return baseMapper.queryUsersByRole(roleType,shopId);

    }

    @Override
    public void removeByShopId(Long shopId) {
        baseMapper.delete(Wrappers.<MerchantUserRole>lambdaQuery().eq(MerchantUserRole::getShopId,shopId));
    }

    @Override
    public List<MerchantUserRole> getByRoleType(List<UserRoleType> roles) {
        return baseMapper.selectList(new QueryWrapper<MerchantUserRole>().in(MerchantUserRole.ROLE_TYPE,roles));
    }

    @Override
    public List<MerchantUserRole> getAllUserRole(Long merchantId) {
        return baseMapper.selectList(new QueryWrapper<MerchantUserRole>().eq(MerchantUserRole.MERCHANT_ID,merchantId));
    }


    @Override
    public void removeByUserId(Long userId,Long shopId) {
        baseMapper.delete(Wrappers.<MerchantUserRole>lambdaQuery().eq(MerchantUserRole::getUserId, userId).eq(MerchantUserRole::getShopId,shopId));
    }

    @Override
    public List<MerchantUserRole> getByUserId(Long userId) {
        return baseMapper.selectList(Wrappers.<MerchantUserRole>lambdaQuery().eq(MerchantUserRole::getUserId,userId));
    }
}
