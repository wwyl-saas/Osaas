package com.fate.api.customer.service;

import com.beust.jcommander.internal.Maps;
import com.fate.api.customer.dto.*;
import com.fate.common.dao.*;
import com.fate.common.entity.*;
import com.fate.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
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
    private static final String BRIEF_SIZE = "-cj200";
    private static final String PRIMARY_SIZE = "-cj420";
    @Autowired
    MerchantService merchantService;
    @Autowired
    CommentService commentService;
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




    /**
     * 查找一定数量的新品
     * @param limit
     * @return
     */
    public List<GoodsDto> getOnSaleNewGoodsLimit(Integer limit){
        List<GoodsDto> goodsDtos=Lists.emptyList();
        List<Goods> goodsList=goodsDao.getBySaleAndNewLimit(true,true,limit);
        if (CollectionUtils.isNotEmpty(goodsList)){
            goodsDtos=goodsList.stream().map(goods ->{
                GoodsDto dto= BeanUtil.mapper(goods,GoodsDto.class);
                dto.setListPicUrls(null);
                pictureOperate(dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return goodsDtos;
    }

    /**
     * 查找一定数量的热卖品
     * @param limit
     * @return
     */
    public List<GoodsDto> getOnSaleHotGoodsLimit(Integer limit){
        List<GoodsDto> goodsDtos=Lists.emptyList();
        List<Goods> goodsList=goodsDao.getBySaleAndHotLimit(true,true,limit);
        if (CollectionUtils.isNotEmpty(goodsList)){
            goodsDtos=goodsList.stream().map(goods ->{
                GoodsDto dto= BeanUtil.mapper(goods,GoodsDto.class);
                dto.setListPicUrls(null);
                pictureOperate(dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return goodsDtos;
    }

    /**
     * 获取商家推荐品
     * @return
     */
    public List<GoodsDto> getOnSaleRecomendGoods(Integer limit){
        List<GoodsDto> goodsDtos=Lists.emptyList();
        List<Goods> goodsList=goodsDao.getBySaleAndRecommendLimit(true,true,limit);
        if (CollectionUtils.isNotEmpty(goodsList)){
            goodsDtos=goodsList.stream().map(goods ->{
                GoodsDto dto= BeanUtil.mapper(goods,GoodsDto.class);
                dto.setListPicUrls(null);
                pictureOperate(dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return goodsDtos;
    }

    /**
     * 根据分类获取商品简介列表
     * @param categoryId
     * @return
     */
    public List<GoodsDto> getOnSaleBrifeByCategoryId(Long categoryId) {
        List<GoodsDto> goodsDtos=Lists.emptyList();
        List<Goods> goodsList=goodsDao.getBriefBySaleAndCategoryId(true,categoryId);
        if (CollectionUtils.isNotEmpty(goodsList)){
            goodsDtos=goodsList.stream().map(goods ->{
                GoodsDto dto= BeanUtil.mapper(goods,GoodsDto.class);
                dto.setPrimaryPicUrls(null);
                dto.setListPicUrls(null);
                pictureOperate(dto);
                return dto;
            }).collect(Collectors.toList());
        }
        return goodsDtos;
    }

    /**
     * 根据关键字搜索商品列表
     * @param keyWords
     * @return
     */
    public List<Goods> getOnsaleBriefByKeyWords(String keyWords) {
        return goodsDao.getBriefBySaleAndKeyWords(true,keyWords);
    }


    /**
     * 获取店铺出售的商品列表
     * @param shopId
     * @return
     */
    public List<Goods> getGoodsByShopId(Long shopId) {
        List<Goods> goods= Lists.emptyList();
        List<Long> goodsIds=getGoodsIdsByShopId(shopId);
        if (CollectionUtils.isNotEmpty(goodsIds)){
            goods=goodsDao.getBySaleAndGoodIds(true,goodsIds);
        }
        return goods;
    }



    /**
     * 获取店铺出售的商品ID列表
     * @param shopId
     * @return
     */
    public List<Long> getGoodsIdByShopId(Long shopId) {
        List<Long> goodsIds= Lists.emptyList();
        List<GoodsShop> goodsShops=getGoosByShopId(shopId);
        if (CollectionUtils.isNotEmpty(goodsShops)){
            goodsIds=goodsShops.parallelStream().map(GoodsShop::getGoodsId).collect(Collectors.toList());
        }
        return goodsIds;
    }


    /**
     * 获取店铺出售的商品ID列表
     * @param shopId
     * @return
     */
    public List<Long> getGoodsIdsByShopId(Long shopId) {
        return goodsShopDao.getGoodsIdsByShopId(shopId);
    }

    /**
     * 通过店铺ID获取其出售的商品
     * @param shopId
     * @return
     */
    public List<GoodsShop> getGoosByShopId(Long shopId){
        return goodsShopDao.getGoodsByShopId(shopId);
    }

    /**
     * 查询商户用户对应的商品ID
     * @param merchantUserId
     * @return
     */
    public List<Long> getGoodsIdByMerchantUserId(Long merchantUserId) {
        List<Long> goodsIds=Lists.emptyList();
        if (merchantUserId!=null){
            List<GoodsMerchantUser> goodsMerchantUsers=getGoodsMerchatUserByMerchantUserId(merchantUserId);
            if (CollectionUtils.isNotEmpty(goodsMerchantUsers)){
                goodsIds=goodsMerchantUsers.parallelStream().map(GoodsMerchantUser::getGoodsId).collect(Collectors.toList());
            }
        }
        return goodsIds;
    }

    /**
     * 根据商户用户ID
     * @param merchantUserId
     * @return
     */
    public List<GoodsMerchantUser> getGoodsMerchatUserByMerchantUserId(Long merchantUserId){
        return goodsMerchantUserDao.getByMerchantUserId(merchantUserId);
    }


    /**
     * 根据商品Id获取商品详情
     * @param goodsId
     * @return
     */
    public GoodsDetailDto getGoodsDetail(Long goodsId) {
        Goods goods=goodsDao.getById(goodsId);
        Assert.notNull(goods,"商品不能为空");

        List<Long> shopIds=getShopIdsByGoodsId(goodsId);
        Assert.notEmpty(shopIds,"商品对应店铺为空");
        List<MerchantShopDto> shopDtos=merchantService.getMerchantShopsByShopIds(shopIds);
        Assert.notNull(shopDtos,"店铺不能为空");

        Integer commentNumber=commentService.getCommentCountByGoodsId(goodsId);
        CommentDto commentDto=commentService.getLastCommentByGoodsId(goodsId);

        GoodsDetailDto goodsDetailDto=GoodsDetailDto.builder()
                .goodsAttributes(getGoodsAttributeDtos(goodsId))
                .goodsDesc(goods.getGoodsDesc())
                .goodsIssues(getGoodsIssueDtos(goodsId))
                .goodsName(goods.getName())
                .goodsTags(Lists.list("到店付","会员折扣"))
                .commentNum(commentNumber)
                .lastComment(commentDto)
                .counterPrice(goods.getCounterPrice())
                .marketPrice(goods.getMarketPrice())
                .merchantShops(shopDtos).build();

        List<String> primaryPictureUrls= Arrays.stream(goods.getPrimaryPicUrls().split(",")).map(url->cdnDomain+url+PRIMARY_SIZE).collect(Collectors.toList());
        goodsDetailDto.setPrimaryPics(primaryPictureUrls);

        List<String> listPictureUrls= Arrays.stream(goods.getListPicUrls().split(",")).map(url->cdnDomain+url).collect(Collectors.toList());
        goodsDetailDto.setDetailPics(listPictureUrls);

        return goodsDetailDto;
    }


    /**
     * 根据商品ID查询店铺ID
     * @param goodsId
     * @return
     */
    private List<Long> getShopIdsByGoodsId(Long goodsId){
        return goodsShopDao.getShopIdsByGoodsId(goodsId);
    }


    /**
     * 图片处理
     * @param dto
     */
    private void pictureOperate(GoodsDto dto){
        dto.setBriefPicUrl(cdnDomain+dto.getBriefPicUrl()+BRIEF_SIZE);
        if (dto.getPrimaryPicUrls()!=null){
            String primaryUrls=dto.getPrimaryPicUrls().replace(",",PRIMARY_SIZE+","+cdnDomain);
            dto.setPrimaryPicUrls(cdnDomain+primaryUrls+PRIMARY_SIZE);
        }
        if (dto.getListPicUrls()!=null){
            String listUrls=dto.getListPicUrls().replace(",",","+cdnDomain);
            dto.setListPicUrls(cdnDomain+listUrls);
        }
    }

    /**
     * 根据分类ID列表查询商品列表
     * @param list
     * @return
     */
    public Map<Long,List<GoodsDto>> getByCategoryIds(List<Long> list) {
        Map<Long,List<GoodsDto>> result= Maps.newHashMap();
        List<Goods> goodsList=goodsDao.getBriefBySaleAndCategoryIds(true,list);
        if (CollectionUtils.isNotEmpty(goodsList)){
            result=goodsList.stream().map(goods ->{
                GoodsDto dto= BeanUtil.mapper(goods,GoodsDto.class);
                pictureOperate(dto);
                return dto;
            } ).collect(Collectors.groupingBy(GoodsDto::getCategoryId));
        }
        return result;
    }


    /**
     * 查询商品属性列表
     * @param goodsId
     * @return
     */
    private List<AttributeDto> getGoodsAttributeDtos(Long goodsId){
        List<AttributeDto> result=Lists.emptyList();
        List<GoodsAttribute> goodsAttributes=goodsAttributeDao.getOrderedByGoodsId(goodsId);
        if (CollectionUtils.isNotEmpty(goodsAttributes)){
            result=goodsAttributes.stream().map(goodsAttribute -> BeanUtil.mapper(goodsAttribute,AttributeDto.class)).collect(Collectors.toList());
        }
        return result;
    }


    /**
     * 查询商品问题列表
     * @param goodsId
     * @return
     */
    private List<GoodsIssueDto> getGoodsIssueDtos(Long goodsId){
        List<GoodsIssueDto> result=Lists.emptyList();
        List<GoodsIssue> goodsIssues=goodsIssueDao.getByGoodsId(goodsId);
        if (CollectionUtils.isNotEmpty(goodsIssues)){
            result=goodsIssues.stream().map(goodsIssue -> BeanUtil.mapper(goodsIssue,GoodsIssueDto.class)).collect(Collectors.toList());
        }
        return result;
    }


    /**
     * 查询评率列表
     * @param goodsId
     * @return
     */
    public List<CommentDto> getGoodsCommentList(Long goodsId) {
        return commentService.getListByGoodsId(goodsId);
    }

    public List<Goods> getGoodsByIds(List<Long> ids) {
        return goodsDao.getGoodsByIds(ids);
    }


    public Goods getGoodsById(Long goodsId) {
        return goodsDao.getById(goodsId);
    }
}
