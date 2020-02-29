package com.fate.api.customer.service;

import com.fate.common.dao.GoodsMerchantUserDao;
import com.fate.common.dao.MerchantPostTitleDao;
import com.fate.common.dao.MerchantUserDao;
import com.fate.common.dao.MerchantUserRoleDao;
import com.fate.common.entity.*;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 商户员工相关
 * @create: 2019-05-23 08:18
 **/
@Service
@Slf4j
public class MerchantUserService {
    @Resource
    MerchantUserDao merchantUserDao;
    @Resource
    MerchantUserRoleDao merchantUserRoleDao;
    @Resource
    GoodsMerchantUserDao goodsMerchantUserDao;
    @Resource
    MerchantPostTitleDao merchantPostTitleDao;

    /**
     * 根据店铺获取员工列表
     * @param shopId
     * @return
     */
    public List<MerchantUser> getByShopId(Long shopId) {
        Assert.notNull(shopId,"Shopid不能为空");
        List<MerchantUser> merchantUsers= Lists.emptyList();
        //获取店铺所有技师
        List<Long> shopUserIds=getMerchantUsersIdsByShopId(shopId);
        if (CollectionUtils.isNotEmpty(shopUserIds)){
            merchantUsers= (List<MerchantUser>) merchantUserDao.listByIds(shopUserIds);
        }
        return merchantUsers;
    }

    /**
     * 根据店铺ID获取商户用户权限信息
     * @param shopId
     * @return
     */
    public  List<MerchantUserRole> getMerchantUserByShopId(Long shopId){
        return merchantUserRoleDao.getMerchantUserByShopId(shopId);
    }

    /**
     * 根据店铺ID获取商户用户权限信息
     * @param shopId
     * @return
     */
    public  List<Long> getMerchantUsersIdsByShopId(Long shopId){
        return merchantUserRoleDao.getMerchantUserIdsByShopId(shopId);
    }


    /**
     * 根据商品获取技师列表
     * @param goodsId
     * @return
     */
    public List<GoodsMerchantUser> getByGoodsId(Long goodsId){
         return goodsMerchantUserDao.getByGoodsId(goodsId);
    }

    /**
     * 根据技师id列表获取技师列表
     * @param ids
     * @return
     */
    public List<MerchantUser> getMerchantUserByIds(List<Long> ids) {
        List<MerchantUser> result = Lists.emptyList();
        if (CollectionUtils.isNotEmpty(ids)) {
            result = merchantUserDao.getMerchantUserByIds(ids);
        }
        return result;
    }


    /**
     * 根据预约商品ID获取技师Id
     * @param goodsId
     * @return
     */
    public List<Long> getMerchantUserIdsByGoodsId(Long goodsId){
        List<Long> goodsMerchantUserIds=Lists.emptyList();
        if (goodsId!=null){
            List<GoodsMerchantUser> goodsMerchantUsers=getByGoodsId(goodsId);
            if (CollectionUtils.isNotEmpty(goodsMerchantUsers)){
                goodsMerchantUserIds=goodsMerchantUsers.stream().map(GoodsMerchantUser::getMerchantUserId).collect(Collectors.toList());
            }
        }
        return goodsMerchantUserIds;
    }

    /**
     * 获取商户的职称map
     * @return
     */
    public Map<Long,String> getPostTitleNameMap(){
        Map<Long,String> postTitleMap= Maps.newHashMap();
        List<MerchantPostTitle> postTitles=merchantPostTitleDao.list();
        if (CollectionUtils.isNotEmpty(postTitles)){
            postTitleMap=postTitles.stream().collect(Collectors.toMap(MerchantPostTitle::getId, title -> title.getName(),(id1, id2)->id1));
        }
        return postTitleMap;
    }

    /**
     * 通过ID查询merchantUser
     * @param merchantUserId
     * @return
     */
    public MerchantUser getMerchantUserById(Long merchantUserId) {
        return merchantUserDao.getById(merchantUserId);
    }

}
