package com.fate.api.customer.service;

import com.fate.api.customer.dto.MerchantShopDto;
import com.fate.common.dao.MerchantDao;
import com.fate.common.entity.Merchant;
import com.fate.common.util.CurrentMerchantUtil;
import com.fate.common.util.GeographyUtil;
import com.fate.common.dao.MerchantApplicationDao;
import com.fate.common.dao.MerchantInfoDao;
import com.fate.common.dao.MerchantShopDao;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.entity.MerchantInfo;
import com.fate.common.entity.MerchantShop;
import com.fate.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 商户相关
 * @create: 2019-05-22 16:55
 **/
@Service
@Slf4j
public class MerchantService {
    @Resource
    MerchantDao merchantDao;
    @Resource
    private MerchantApplicationDao merchantApplicationDao;
    @Resource
    private MerchantShopDao merchantShopDao;
    @Resource
    private MerchantInfoDao merchantInfoDao;
    @Value("${cdn.domain}")
    private String cdnDomain;


    /**
     * 获取所有商铺
     * @return
     */
    public List<MerchantShop>  getMerchantShops(){
        return merchantShopDao.selectAll();
    }

    /**
     * 获取商户信息
     * @return
     */
    public MerchantInfo getMerchantInfo() {
        return merchantInfoDao.findMerchantInfo();
    }

    /**
     * 获取应用信息
     * @param applicationId
     * @return
     */
    @Cacheable(value = "merchantApplications" ,unless="#result == null", key="#applicationId")
    public MerchantApplication findApplicationById(long applicationId) {
        return merchantApplicationDao.getById(applicationId);
    }

    /**
     * 获取商户信息
     * @param merchantId
     * @return
     */
    private MerchantInfo getMerchantInfoByMerchantId(Long merchantId) {
        return merchantInfoDao.getMerchantInfoByMerchantId(merchantId);
    }

    /**
     * 获取店铺ID对应的门店列表
     * @param shopIds
     * @return
     */
    public List<MerchantShopDto> getMerchantShopsByShopIds(List<Long> shopIds) {
        List<MerchantShopDto> result=Lists.emptyList();
        List<MerchantShop> shopList=(List<MerchantShop>) merchantShopDao.listByIds(shopIds);
        if (CollectionUtils.isNotEmpty(shopList)){
            result=shopList.stream().map(shop->{
                MerchantShopDto dto=BeanUtil.mapper(shop,MerchantShopDto.class);
                dto.setPicture(cdnDomain+dto.getPicture());
                return dto;
            }).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取店铺ID对应的门店列表
     * @param shopIds
     * @return
     */
    public List<MerchantShop> getShopsByShopIds(List<Long> shopIds) {
        return (List<MerchantShop>) merchantShopDao.listByIds(shopIds);
    }

    /**
     * 查询店铺信息及距离信息
     * @param longitude
     * @param latitude
     * @return
     */
    public List<MerchantShopDto> getMerchantShopDtos(Double longitude,Double latitude) {
        List<MerchantShopDto> result= Lists.emptyList();
        List<MerchantShop> merchantShops=merchantShopDao.selectAll();
        if (CollectionUtils.isNotEmpty(merchantShops)){
            result=merchantShops.stream().map(shop->{
                shop.setPicture(cdnDomain+shop.getPicture());
                MerchantShopDto merchantShopDto= BeanUtil.mapper(shop,MerchantShopDto.class);
                //计算距离
                if (latitude!=null && longitude!=null){
                    merchantShopDto.setDistance(GeographyUtil.getDistance(new Point2D.Double(latitude,longitude),new Point2D.Double(shop.getLatitude(),shop.getLongitude())));
                }else {
                    merchantShopDto.setDistance(BigDecimal.ZERO);
                }
                return merchantShopDto;
            }).sorted(Comparator.comparing(MerchantShopDto::getDistance)).collect(Collectors.toList());
            //标识最近
            if (CollectionUtils.isNotEmpty(result)){
                result.get(0).setIfNearest(true);
            }
        }
        return result;
    }

    /**
     * 获取商户基本信息
     * @param merchantId
     * @return
     */
    @Cacheable(value = "merchants" ,unless="#result == null", key="#merchantId")
    public Merchant getById(Long merchantId) {
       return merchantDao.getById(merchantId);
    }

    /**
     * 获取公众号应用(前提条件一个商家公众号只能有一个)
     * @return
     */
    public MerchantApplication getMpApplication() {
        return merchantApplicationDao.getMpApplication(CurrentMerchantUtil.getMerchant().getId());
    }

    public MerchantShop getMerchantShopById(Long shopId) {
        return merchantShopDao.getById(shopId);
    }
}
