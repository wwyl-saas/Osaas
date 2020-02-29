package com.fate.api.merchant.service;

import com.fate.api.merchant.dto.MerchantDto;
import com.fate.api.merchant.dto.MerchantShopDto;
import com.fate.api.merchant.dto.SelectListDto;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.dao.MerchantDao;
import com.fate.common.dao.MerchantPostTitleDao;
import com.fate.common.dao.MerchantShopDao;
import com.fate.common.dao.MerchantUserRoleDao;
import com.fate.common.entity.*;
import com.fate.common.enums.UserRoleType;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentMerchantUtil;
import com.fate.common.util.GeographyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 商户相关
 * @create: 2019-05-22 16:55
 **/
@Service
@Slf4j
public class MerchantService {
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Resource
    private MerchantDao merchantDao;
    @Resource
    private MerchantShopDao merchantShopDao;
    @Resource
    private MerchantPostTitleDao merchantPostTitleDao;
    @Resource
    MerchantUserRoleDao merchantUserRoleDao;


    /**
     * 获取商户基本信息
     * @param merchantId
     * @return
     */
    @Cacheable(value = "merchants" ,unless="#result == null", key="#merchantId")
    public Merchant getMerchantById(Long merchantId) {
        return merchantDao.getById(merchantId);
    }


    /**
     * 获取最近的门店
     * @param latitude
     * @param longitude
     * @return
     */
    public MerchantShopDto getShopsWithRoleAndNearest(Double latitude, Double longitude) {
        MerchantShopDto result=new MerchantShopDto();
        List<MerchantShop> merchantShops=null;
        Set<String> userShops=CurrentUserUtil.getMerchantUserRoleTypeMap().keySet();
        if (CollectionUtils.isNotEmpty(userShops)) {
            merchantShops = merchantShopDao.getByIds(userShops.stream().map(shopId->Long.parseLong(shopId)).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(merchantShops)){
                result=merchantShops.stream().map(shop->{
                    MerchantShopDto merchantShopDto= BeanUtil.mapper(shop,MerchantShopDto.class);
                    //计算距离
                    merchantShopDto.setDistance(GeographyUtil.getDistance(new Point2D.Double(latitude,longitude),new Point2D.Double(shop.getLatitude(),shop.getLongitude())));
                    return merchantShopDto;
                }).sorted(Comparator.comparing(MerchantShopDto::getDistance)).collect(Collectors.toList()).get(0);
            }
        }
        return result;
    }


    /**
     * 查询门店信息
     * @param shopId
     * @return
     */
    public MerchantShop getShopById(Long shopId) {
        return merchantShopDao.getById(shopId);
    }

    @Cacheable(value = "merchants" ,unless="#result == null", key="#merchantId")
    public Merchant getById(long merchantId) {
        return merchantDao.getById(merchantId);
    }

    /**
     * 获取商户的职称列表
     * @return
     */
    public List<SelectListDto> positionList() {
        List<SelectListDto> result=Lists.emptyList();
        List<MerchantPostTitle> merchantPostTitles=merchantPostTitleDao.getAll();
        if (CollectionUtils.isNotEmpty(merchantPostTitles)){
            result=merchantPostTitles.stream().map(merchantPostTitle -> {
                return SelectListDto.builder().code(merchantPostTitle.getId()).name(merchantPostTitle.getName()).build();
            }).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取有权限的门店列表
     * @return
     */
    public List<MerchantShopDto> getShopDtoList() {
        List<MerchantShopDto> result= Lists.emptyList();
        List<MerchantShop> merchantShops=null;
        Set<String> userShops=CurrentUserUtil.getMerchantUserRoleTypeMap().keySet();
        if (CollectionUtils.isNotEmpty(userShops)){
            merchantShops=merchantShopDao.getByIds(userShops.stream().map(shopId->Long.parseLong(shopId)).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(merchantShops)){
                result=merchantShops.stream().map(merchantShop -> BeanUtil.mapper(merchantShop,MerchantShopDto.class)).collect(Collectors.toList());
            }
        }
        return result;
    }


    public List<MerchantShop> getShopList(){
        return merchantShopDao.selectAll();
    }


    /**
     * 获取商家列表
     * @return
     */
    public List<MerchantDto> getMerchantIdAndName() {
        List<MerchantDto> merchantDtoList=new ArrayList<>();
        List<Map<String,Object>> list=merchantDao.getMerchantIdAndName();
        for(Map<String,Object> map:list){
            MerchantDto merchantDto=new MerchantDto();
            merchantDto.setId(Long.valueOf(map.get("id").toString()));
            merchantDto.setName(map.get("name").toString());
            merchantDtoList.add(merchantDto);

        }
        return merchantDtoList;


    }
}
