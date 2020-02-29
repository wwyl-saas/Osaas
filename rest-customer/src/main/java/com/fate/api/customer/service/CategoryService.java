package com.fate.api.customer.service;

import com.fate.api.customer.dto.CategoryDto;
import com.fate.api.customer.dto.GoodsDto;
import com.fate.common.dao.CategoryDao;
import com.fate.common.entity.Category;
import com.fate.common.entity.Goods;
import com.fate.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 分类相关
 * @author: chenyixin
 * @create: 2019-06-04 23:23
 **/
@Service
@Slf4j
public class CategoryService {
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Resource
    CategoryDao categoryDao;
    @Autowired
    GoodsService goodsService;

    /**
     * 获取商户对应的商品服务列表
     * @return
     */
    public List<CategoryDto> getCategoryDtoList() {
        List<CategoryDto> result= Lists.emptyList();
        List<Category> categories=categoryDao.findEnableAllOrdered();
        if (CollectionUtils.isNotEmpty(categories)){
            List<Long> list=categories.stream().map(category -> category.getId()).collect(Collectors.toList());
            Map<Long,List<GoodsDto>> categoryMap=goodsService.getByCategoryIds(list);
            result=categories.stream().map(category -> {
                CategoryDto categoryDto= CategoryDto.builder().id(category.getId()).name(category.getName()).icon(cdnDomain+category.getIcon()).iconOn(cdnDomain+category.getIconOn()).build();
                categoryDto.setGoodsList(categoryMap.get(category.getId()));
                return categoryDto;
            }).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 关键字搜索商品
     * @param keyWords
     * @return
     */
    public List<GoodsDto> searchCategoryGoods(String keyWords) {
        List<GoodsDto> result=Lists.emptyList();
        List<Goods> goodsList=goodsService.getOnsaleBriefByKeyWords(keyWords);
        if (CollectionUtils.isNotEmpty(goodsList)){
            result=goodsList.stream().map(goods ->{
                GoodsDto dto=BeanUtil.mapper(goods,GoodsDto.class);
                dto.setBriefPicUrl(cdnDomain+dto.getBriefPicUrl());
                return dto;
            } ).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 查询分类列表
     * @return
     */
    public List<Category> getCategoryList() {
        return categoryDao.findEnableAllOrdered();
    }
}
