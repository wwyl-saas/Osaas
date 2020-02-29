package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.api.merchant.dto.MerchantShopDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.query.MerchantShopQuery;
import com.fate.common.dao.*;
import com.fate.common.entity.MerchantShop;
import com.fate.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 职称相关
 * @author:
 * @create: 2019-06-02 11:00
 **/
@Service
@Slf4j
public class MerchantShopService {
    @Resource
    MerchantShopDao merchantShopDao;
    @Resource
    MerchantUserRoleDao merchantUserRoleDao;
    @Resource
    GoodsShopDao goodsShopDao;

    @Resource
    GoodsDao goodsDao;
    @Resource
    GoodsIssueDao goodsIssueDao;
    @Resource
    GoodsAttributeDao goodsAttributeDao;

    /**
     * 新建商铺
     * @param merchantShopQuery
     */
    public void saveMerchantShop(MerchantShopQuery merchantShopQuery) {
        MerchantShop merchantShop =
        MerchantShop.builder().address(merchantShopQuery.getAddress()).enabled(merchantShopQuery.getEnabled()).latitude(merchantShopQuery.getLatitude())
       .longitude(merchantShopQuery.getLongitude()).name(merchantShopQuery.getName())
                .picture(merchantShopQuery.getPicture()).telephone(merchantShopQuery.getTelephone()).build();
        Assert.isTrue(merchantShop.insert(),"插入数据失败");

    }

    /**
     * 修改商铺信息
     * @param merchantShopQuery
     */
    public void updateMerchantShop(MerchantShopQuery merchantShopQuery) {
        Assert.notNull(merchantShopQuery.getId(),"id不能为空！");
        MerchantShop merchantShop=merchantShopDao.getById(merchantShopQuery.getId());
        Assert.notNull(merchantShop,"商铺不存在！");
        merchantShop.setTelephone(merchantShopQuery.getTelephone());

        merchantShop.setPicture(merchantShopQuery.getPicture());
        merchantShop.setLatitude(merchantShopQuery.getLatitude());
        merchantShop.setLongitude(merchantShopQuery.getLongitude());
        merchantShop.setName(merchantShopQuery.getName());
        merchantShop.setEnabled(merchantShopQuery.getEnabled());
        merchantShop.setAddress(merchantShopQuery.getAddress());
        Assert.isTrue(merchantShop.updateById(),"更新数据失败");


    }

    /**
     * 删除某个店铺
     * @param id
     */
    public void deleteMerchantShop(Long id) {
        Assert.notNull(id,"id不能为空！");
        MerchantShop merchantShop=merchantShopDao.getById(id);
        Assert.notNull(merchantShop,"店铺不存在！");
        //删除角色店铺关系
        //merchantUserRoleDao.removeByShopId(merchantShop.getId());
        List<Long> goodsIds=goodsShopDao.getGoodsIdsByShopId(merchantShop.getId());
        //删除店铺服务关系
        //goodsShopDao.removeByShopId(merchantShop.getId());
        //删除店铺中的服务
        //goodsDao.removeByIds(goodsIds);
        //删除服务规格
        //goodsIssueDao.removeByGoodIds(goodsIds);
        //删除服务问题
        //goodsAttributeDao.removeByGoodsIds(goodsIds);
        Assert.isTrue(merchantShop.deleteById(),"删除失败！"); ;
    }

    /**
     * 根据店铺名称查询商户下的相关店铺
     * @param pageIndex
     * @param pageSize
     * @return
     */

    public PageDto<MerchantShopDto> queryMerchantShopByName(Integer pageIndex, Integer pageSize,String name) {
        IPage<MerchantShop> page=merchantShopDao.getMerchantShopByNamePage(pageIndex,pageSize,name);
        List<MerchantShopDto> result= Lists.emptyList();
        if (page!=null && CollectionUtils.isNotEmpty(page.getRecords())) {
            List<MerchantShop> merchantShops = page.getRecords();
            result = merchantShops.stream().map(merchantShop -> {
                MerchantShopDto merchantShopDto = BeanUtil.mapper(merchantShop, MerchantShopDto.class);
                return merchantShopDto;
            }).collect(Collectors.toList());
        }
        return new PageDto<>(pageIndex, pageSize, page.getTotal(), page.getPages(), result);

    }

    /**
     * 查询某商铺信息
     * @param id
     * @return
     */
    public MerchantShopDto queryOneMerchantShop(Long id) {
        Assert.notNull(id,"id不能为空！");
        MerchantShop merchantShop=merchantShopDao.getById(id);
        MerchantShopDto merchantShopDto = BeanUtil.mapper(merchantShop, MerchantShopDto.class);
        return merchantShopDto;
    }
}
