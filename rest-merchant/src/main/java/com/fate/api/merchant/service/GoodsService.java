package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beust.jcommander.internal.Maps;
import com.fate.api.merchant.dto.GoodsDto;
import com.fate.api.merchant.dto.SelectListDto;
import com.fate.api.merchant.dto.*;
import com.fate.api.merchant.query.*;
import com.fate.common.dao.*;
import com.fate.common.entity.*;
import com.fate.common.entity.GoodsAttribute;
import com.fate.common.model.StandardResponse;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 商品相关
 * @create: 2019-05-22 23:47
 **/
@Service
@Slf4j
public class GoodsService {
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Autowired
    MerchantService merchantService;
    @Resource
    GoodsDao goodsDao;
    @Resource
    GoodsShopDao goodsShopDao;
    @Resource
    GoodsMerchantUserDao goodsMerchantUserDao;
    @Resource
    GoodsAttributeDao goodsAttributeDao;
    @Resource
    GoodsIssueDao goodsIssueDao;
    @Resource
    MerchantShopDao merchantShopDao ;
    @Resource
    MerchantUserDao merchantUserDao;




    /**
     * 获取店铺出售的商品ID列表
     *
     * @param shopId
     * @return
     */
    public List<Long> getGoodsIdsByShopId(Long shopId) {
        return goodsShopDao.getGoodsIdsByShopId(shopId);
    }

    /**
     * 通过店铺ID获取其出售的商品
     *
     * @param shopId
     * @return
     */
    public List<GoodsShop> getGoosByShopId(Long shopId) {
        return goodsShopDao.getGoodsByShopId(shopId);
    }

    /**
     * 查询商户用户对应的商品ID
     *
     * @param merchantUserId
     * @return
     */
    public List<Long> getGoodsIdByMerchantUserId(Long merchantUserId) {
        List<Long> goodsIds = Lists.emptyList();
        if (merchantUserId != null) {
            List<GoodsMerchantUser> goodsMerchantUsers = getGoodsMerchatUserByMerchantUserId(merchantUserId);
            if (CollectionUtils.isNotEmpty(goodsMerchantUsers)) {
                goodsIds = goodsMerchantUsers.parallelStream().map(GoodsMerchantUser::getGoodsId).collect(Collectors.toList());
            }
        }
        return goodsIds;
    }

    /**
     * 根据商户用户ID
     *
     * @param merchantUserId
     * @return
     */
    public List<GoodsMerchantUser> getGoodsMerchatUserByMerchantUserId(Long merchantUserId) {
        return goodsMerchantUserDao.getByMerchantUserId(merchantUserId);
    }


    /**
     * 根据商品ID查询店铺ID
     *
     * @param goodsId
     * @return
     */
    private List<Long> getShopIdsByGoodsId(Long goodsId) {
        return goodsShopDao.getShopIdsByGoodsId(goodsId);
    }


    /**
     * 根据分类ID列表查询商品列表
     *
     * @param list
     * @return
     */
    public Map<Long, List<GoodsDto>> getByCategoryIds(List<Long> list) {
        Map<Long, List<GoodsDto>> result = Maps.newHashMap();
        List<Goods> goodsList = goodsDao.getBriefBySaleAndCategoryIds(true, list);
        if (CollectionUtils.isNotEmpty(goodsList)) {
            result = goodsList.stream().map(goods -> {
                GoodsDto dto = BeanUtil.mapper(goods, GoodsDto.class);
                dto.setBriefPicUrl(cdnDomain + dto.getBriefPicUrl());
                return dto;
            }).collect(Collectors.groupingBy(GoodsDto::getCategoryId));
        }
        return result;
    }


    public List<Goods> getGoodsByIds(List<Long> ids) {
        return goodsDao.getGoodsByIds(ids);
    }


    public Goods getGoodsById(Long goodsId) {
        return goodsDao.getById(goodsId);
    }

    /**
     * 查询最畅销的N个商品
     *
     * @param limit
     * @return
     */
    public List<GoodsDto> getBestSellingDtos(int limit) {
        List<GoodsDto> result = Lists.emptyList();
        List<Goods> goods = goodsDao.getBySaleOrderBySellVolumeLimit(true, limit);
        if (CollectionUtils.isNotEmpty(goods)) {
            result = goods.stream().map(goodsOne -> {
                GoodsDto dto = BeanUtil.mapper(goodsOne, GoodsDto.class);
                dto.setBriefPicUrl(cdnDomain + goodsOne.getBriefPicUrl());
                return dto;
            }).collect(Collectors.toList());
        }
        return result;
    }


    /**
     * 查询最畅销的N个商品
     *
     * @param limit
     * @return
     */
    public List<Goods> getBestSelling(int limit) {
        return goodsDao.getBySaleOrderBySellVolumeLimit(true, limit);
    }

    /**
     * 获取店铺出售的商品列表
     *
     * @param shopId
     * @return
     */
    public List<Goods> getGoodsByShopId(Long shopId) {
        List<Goods> goods = Lists.emptyList();
        List<Long> goodsIds = getGoodsIdsByShopId(shopId);
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            goods = goodsDao.getBySaleAndGoodIds(true, goodsIds);
        }
        return goods;
    }

    /**
     * 根据分类id获取商品
     *
     * @param categoryId
     * @return
     */
    public List<Goods> getGoodsByCategoryId(Long categoryId) {
        return goodsDao.getGoodsByCategoryId(categoryId);
    }

    /**
     * 更新商品信息
     *
     * @param goods
     * @return
     */
    public void saveOrUpdateGoods(Goods goods) {
        goodsDao.saveOrUpdate(goods);
    }

    /**
     * 根据关键词，商品名字，是否上架条件查询商品
     *
     * @param
     * @return
     */
    public PageDto<GoodsDto> getGoodsBykeyWordAndGoodsNameAndOnSale(String keyWord, String goodsName, Boolean onSale, Integer pageIndex, Integer pageSize) {
        List<GoodsDto> result = Lists.emptyList();
        IPage<Goods> page = goodsDao.getGoodsBykeyWordAndGoodsNameAndOnSale(keyWord, goodsName, onSale, pageIndex, pageSize);
        if (page != null && CollectionUtils.isNotEmpty(page.getRecords())) {
            List<Goods> goodss = page.getRecords();
            result = goodss.stream().map(goods -> {
                GoodsDto goodsDto = BeanUtil.mapper(goods, GoodsDto.class);
                goodsDto.setBriefPicUrl(cdnDomain+goodsDto.getBriefPicUrl());
                return goodsDto;
            }).collect(Collectors.toList());
            return new PageDto<>(pageIndex, pageSize, page.getTotal(), page.getPages(), result);
        }

        return new PageDto<>(pageIndex, pageSize, 0, 0, result);
    }

    /**
     * @param adminGoodsQuery
     */
    @Transactional
    public void create(AdminGoodsQuery adminGoodsQuery) {
        AdminGoodsMainQuery goodsMainQuery = adminGoodsQuery.getGoodsMainQuery();
        Goods goods = new Goods();
        goods = copyGoodsQueryToGoods(goods, goodsMainQuery);
        Assert.isTrue( goods.insert(), "数据插入失败！");
        Long goodsId=goods.getId();

        List<GoodsAttributeQuery> goodsAttributeQueryList = adminGoodsQuery.getGoodsAttributeList();
        List<GoodsAttribute>  goodsAttributeList=new ArrayList<GoodsAttribute>();
        goodsAttributeQueryList.stream().forEach(attributeQuery -> {
            GoodsAttribute attribute = new GoodsAttribute();
            attribute.setGoodsId(goodsId);
            attribute.setName(attributeQuery.getName());
            attribute.setValue(attributeQuery.getValue());
            goodsAttributeList.add(attribute);
        });
        if(CollectionUtils.isNotEmpty(goodsAttributeList)){
            goodsAttributeDao.saveBatch(goodsAttributeList);
        }
        List<GoodsIssueQuery> goodsIssuesQueryList = adminGoodsQuery.getGoodsIssueList();
        List<GoodsIssue> goodsIssuesList = new ArrayList<GoodsIssue>();

        goodsIssuesQueryList.stream().forEach(goodIssueQuery -> {
            GoodsIssue issue = new GoodsIssue();
            issue.setGoodsId(goodsId);
            issue.setQuestion(goodIssueQuery.getQuestion());
            issue.setAnswer(goodIssueQuery.getAnswer());
            goodsIssuesList.add(issue);

        });
        if(CollectionUtils.isNotEmpty(goodsIssuesList)){
            goodsIssueDao.saveBatch(goodsIssuesList);
        }

        List<Long> userQueryList=adminGoodsQuery.getMerchantUserList();
        List<GoodsMerchantUser> merchantUserList = new ArrayList<GoodsMerchantUser>();
        userQueryList.stream().forEach(id -> {
            GoodsMerchantUser goodsMerchantUser  = new GoodsMerchantUser();
            goodsMerchantUser.setGoodsId(goodsId);
            goodsMerchantUser.setMerchantUserId(id);
            merchantUserList.add(goodsMerchantUser);

        });
        goodsMerchantUserDao.saveBatch(merchantUserList);

        List<Long> shopQueryList=adminGoodsQuery.getShopList();
        List<GoodsShop> shopList = new ArrayList<GoodsShop>();
        shopQueryList.stream().forEach(shopId -> {
            GoodsShop goodsShop  = new GoodsShop();
            goodsShop.setGoodsId(goodsId);
            goodsShop.setShopId(shopId);
            shopList.add(goodsShop);

        });
       goodsShopDao.saveBatch(shopList) ;
    }

    /**
     * 根据商品ID查询商品详情
     *
     * @param goodsId
     * @return
     */
    public AdminGoodsDto getGoodDetailsById(Long goodsId) {
        AdminGoodsDto adminGoodsDto = new AdminGoodsDto();
        List<Long> goodsIdList = new ArrayList<Long>();
        goodsIdList.add(goodsId);
        List<Goods> goodsList = goodsDao.getGoodsByIds(goodsIdList);

        Assert.isTrue(CollectionUtils.isNotEmpty(goodsList),"此商品不存在！");

        if (CollectionUtils.isNotEmpty(goodsList)) {
            Goods goods = goodsList.get(0);
            GoodsDto goodsDto = BeanUtil.mapper(goods, GoodsDto.class);
            adminGoodsDto.setGoodDto(goodsDto);
        }
        List<GoodsAttribute> attributesList = goodsAttributeDao.getOrderedByGoodsId(goodsId);
        List<AttributeDto> attributeDtoList = Lists.emptyList();
        attributeDtoList = attributesList.stream().map(attribute -> {
            AttributeDto attributeDto = BeanUtil.mapper(attribute, AttributeDto.class);
            return attributeDto;

        }).collect(Collectors.toList());
        adminGoodsDto.setGoodsAtrributeList(attributeDtoList);

        List<GoodsIssue> issuesList = goodsIssueDao.getByGoodsId(goodsId);
        List<GoodsIssueDto> issuesDtoList = Lists.emptyList();
        issuesDtoList = issuesList.stream().map(issue -> {
            GoodsIssueDto issueDto = BeanUtil.mapper(issue, GoodsIssueDto.class);
            return issueDto;
        }).collect(Collectors.toList());
        adminGoodsDto.setGoodsIssueList(issuesDtoList);

        List<Map<String,Object>> shopId2NameMapList= merchantShopDao.getIdAndNameByGoodsId(goodsId);

        List<MerchantShopDto> merchantshopDtoList = new ArrayList<MerchantShopDto>();

        for(Map<String,Object> map:shopId2NameMapList){
            MerchantShopDto merchantShopDto=new MerchantShopDto();
            merchantShopDto.setId(Long.valueOf(String.valueOf(map.get("id"))));
            merchantShopDto.setName(map.get("name").toString());
            merchantshopDtoList.add(merchantShopDto);

        }
        adminGoodsDto.setShopList(merchantshopDtoList);
        List<MerchantUserDto> merchantUserDtoList= new ArrayList<MerchantUserDto>();
        List<Map<String,Object>> userId2NameMapList=merchantUserDao.getIdAndNameByGoodsId(goodsId);

        for(Map<String,Object> map:userId2NameMapList){
            MerchantUserDto merchantUserDto=new MerchantUserDto();
            merchantUserDto.setId(Long.valueOf(String.valueOf(map.get("id"))));
            merchantUserDto.setName(map.get("name").toString());
            merchantUserDtoList.add(merchantUserDto);

        }
        adminGoodsDto.setMerchantUserList(merchantUserDtoList);
        return adminGoodsDto;
    }


    /**
     * 获取门店下所有服务列表
     *
     * @param shopId
     * @return
     */
    public List<SelectListDto> serviceList(Long shopId) {
        List<SelectListDto> result = Lists.emptyList();
        List<Long> goodsIds = getGoodsIdsByShopId(shopId);
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            List<Goods> goods = goodsDao.getBySaleAndGoodIds(true, goodsIds);
            if (CollectionUtils.isNotEmpty(goods)) {
                result = goods.stream().map(goods1 -> SelectListDto.builder().name(goods1.getName()).code(goods1.getId()).build()).collect(Collectors.toList());
            }
        }
        return result;
    }


    public void updateGoods(AdminGoodsQuery adminGoodsQuery) {
        AdminGoodsMainQuery goodsMainQuery = adminGoodsQuery.getGoodsMainQuery();
        Assert.notNull(goodsMainQuery.getId(), "更新的商品不能为空！");
        Goods goods = goodsDao.getById(goodsMainQuery.getId());
        Assert.notNull(goods, "更新的商品不存在！");

        goods = copyGoodsQueryToGoods(goods, goodsMainQuery);
        Assert.isTrue(goods.updateById(), "数据更新失败！");

        List<GoodsAttributeQuery> goodsAttributeQueryList = adminGoodsQuery.getGoodsAttributeList();
        goodsAttributeDao.removeByGoodsId(goods.getId());

        goodsAttributeQueryList.stream().forEach(attributeQuery -> {
            GoodsAttribute attribute = new GoodsAttribute();
            attribute.setGoodsId(goodsMainQuery.getId());
            attribute.setName(attributeQuery.getName());
            attribute.setValue(attributeQuery.getValue());
            Assert.isTrue(attribute.insert(), "数据插入失败！");
        });
        List<GoodsIssueQuery> goodsIssuesQueryList = adminGoodsQuery.getGoodsIssueList();
        goodsIssueDao.removeByGoodsId(goods.getId());

        goodsIssuesQueryList.stream().forEach(goodIssueQuery -> {
            GoodsIssue issue = new GoodsIssue();
            issue.setGoodsId(goodsMainQuery.getId());
            issue.setQuestion(goodIssueQuery.getQuestion());
            issue.setAnswer(goodIssueQuery.getAnswer());
            Assert.isTrue(issue.insert(), "数据插入失败！");
        });

        goodsShopDao.deleteByGoodsId(goodsMainQuery.getId());
        List<Long> userQueryList=adminGoodsQuery.getMerchantUserList();
        List<GoodsMerchantUser> merchantUserList = new ArrayList<GoodsMerchantUser>();
        userQueryList.stream().forEach(id -> {
            GoodsMerchantUser goodsMerchantUser  = new GoodsMerchantUser();
            goodsMerchantUser.setGoodsId(goodsMainQuery.getId());
            goodsMerchantUser.setMerchantUserId(id);
            merchantUserList.add(goodsMerchantUser);

        });
        goodsMerchantUserDao.deleteByGoodsId(goodsMainQuery.getId());
        goodsMerchantUserDao.saveBatch(merchantUserList);

        List<Long> shopQueryList=adminGoodsQuery.getShopList();
        List<GoodsShop> shopList = new ArrayList<GoodsShop>();
        shopQueryList.stream().forEach(shopId -> {
            GoodsShop goodsShop  = new GoodsShop();
            goodsShop.setGoodsId(goodsMainQuery.getId());
            goodsShop.setShopId(shopId);
            shopList.add(goodsShop);

        });
        goodsShopDao.saveBatch(shopList) ;

    }

    private Goods copyGoodsQueryToGoods(Goods goods, AdminGoodsMainQuery goodsMainQuery) {
        goods.setCategoryId(goodsMainQuery.getCategoryId());
        goods.setOnSale(goodsMainQuery.getOnSale());
        goods.setBriefPicUrl(goodsMainQuery.getBriefPicUrl());
        goods.setCounterPrice(goodsMainQuery.getCounterPrice());
        goods.setGoodsBrief(goodsMainQuery.getGoodsBrief());
        goods.setIsHot(goodsMainQuery.getIsHot());
        goods.setGoodsDesc(goodsMainQuery.getGoodsDesc());
        goods.setIsNew(goodsMainQuery.getIsNew());
        goods.setMarketPrice(goodsMainQuery.getMarketPrice());
        goods.setSellVolume(goodsMainQuery.getSellVolume());
        goods.setName(goodsMainQuery.getName());
        goods.setIsRecommend(goodsMainQuery.getIsRecommend());
        goods.setSortOrder(goodsMainQuery.getSortOrder());
        goods.setKeywords(goodsMainQuery.getKeywords());
        goods.setListPicUrls(goodsMainQuery.getListPicUrls());
        goods.setPrimaryPicUrls(goodsMainQuery.getPrimaryPicUrls());
        goods.setSellVolume(goodsMainQuery.getSellVolume());
        goods.setUpdateTime(LocalDateTime.now());
        return goods;
    }

    public void delete(Long goodsId) {
        Assert.notNull(goodsId, "商品id不能为空！");
        Goods goods = goodsDao.getById(goodsId);
        Assert.notNull(goods, "此商品不存在！");
        goodsIssueDao.removeByGoodsId(goodsId);
        goodsAttributeDao.removeByGoodsId(goodsId);
        goodsShopDao.deleteByGoodsId(goodsId);
        goodsMerchantUserDao.deleteByGoodsId(goodsId);
        Assert.isTrue(goods.deleteById(),"删除失败！");

    }

    /**
     * 查询某个店铺的所有产品
     * @return
     */
    public List<GoodsDto> queryGoodsByMerchant() {
       List<Goods>  list=goodsDao.getAll();
       List<GoodsDto> result=Lists.emptyList();

       if (CollectionUtils.isNotEmpty(list)) {
            result=list.stream().map(goods -> {
                GoodsDto dto=BeanUtil.mapper(goods,GoodsDto.class);
                if(goods.getBriefPicUrl()!=null){
                    dto.setBriefPicUrl(cdnDomain+goods.getBriefPicUrl());
                }

                return dto;
            }).collect(Collectors.toList());
        }
       return result;


    }
}
