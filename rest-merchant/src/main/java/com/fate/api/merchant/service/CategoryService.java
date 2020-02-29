package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.api.merchant.dto.*;
import com.fate.api.merchant.query.CategoryQuery;
import com.fate.common.dao.CategoryDao;
import com.fate.common.dao.GoodsDao;
import com.fate.common.entity.Category;
import com.fate.common.entity.Goods;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.IdUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.time.LocalDateTime;
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
    @Resource
    GoodsDao goodsDao;

    /**
     * 获取商户对应的商品服务列表(包含快捷列表)
     * @return
     */
    public SettleDto getCategoryDtoList(Long shopId) {
        SettleDto result= new SettleDto();
        List<Goods> bestSellings=goodsService.getBestSelling(10);
        if (CollectionUtils.isNotEmpty(bestSellings)){
            List<Long> goodsIds=bestSellings.stream().map(goods -> goods.getId()).collect(Collectors.toList());
            result.setBestSell(goodsIds);
        }
        List<Category> categories=categoryDao.findEnableAllOrdered();
        if (CollectionUtils.isNotEmpty(categories)){
            List<Long> list=categories.stream().map(category -> category.getId()).collect(Collectors.toList());
            Map<Long,List<GoodsDto>> categoryMap=goodsService.getByCategoryIds(list);
            List<CategoryDto> categoryDtos=categories.stream().map(category -> {
                CategoryDto categoryDto= CategoryDto.builder().id(category.getId()).name(category.getName()).build();
                categoryDto.setGoodsList(categoryMap.get(category.getId()));
                return categoryDto;
            }).filter(categoryDto -> CollectionUtils.isNotEmpty(categoryDto.getGoodsList())).collect(Collectors.toList());
            result.setCategorys(categoryDtos);
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
    /**
     * 更新分类列表
     * @return
     */
    public void updateCategory(CategoryQuery categoryQuery) {
          Assert.notNull(categoryQuery.getId(),"分类id不存在！");
          Category category = categoryDao.getById(categoryQuery.getId());
          Assert.notNull(category, "更新的对象不存在！");
          category.setEnable(categoryQuery.getEnable());
          String icon=categoryQuery.getIcon().contains(cdnDomain)?
                  categoryQuery.getIcon().substring(cdnDomain.length()):categoryQuery.getIcon();
          String iconOn=categoryQuery.getIconOn().contains(cdnDomain)?
                categoryQuery.getIconOn().substring(cdnDomain.length()):categoryQuery.getIconOn();
          category.setIcon(icon);
          category.setIconOn(iconOn);

          boolean flag = category.updateById();
          Assert.isTrue(flag, "数据更新失败！");
    }
    /**
     * admin查询分类列表
     * @return
     */
    public PageDto<CategoryDto> getCategoryListByMercahtIdPage(Integer pageIndex, Integer pageSize){
          IPage<Category> page=categoryDao.getByMerchantIdPage(pageIndex,pageSize);
          List<CategoryDto> result = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(page.getRecords())){
            result=page.getRecords().stream().map(category -> {
                CategoryDto categoryDto= BeanUtil.mapper(category,CategoryDto.class);
                categoryDto.setIcon(cdnDomain+categoryDto.getIcon());
                categoryDto.setIconOn(cdnDomain+categoryDto.getIconOn());
                return categoryDto;
                  }).collect(Collectors.toList());
        }

        return new PageDto<>(pageIndex,pageSize,page.getTotal(),page.getPages(),result);

    }

    /**
     * 删除分类，将分类对应的商品设置为下架
     * @param categoryId
     */
    @Transactional
    public void deleteCategory(Long categoryId) {
        Assert.notNull(categoryId,"id不能为空");
        List<Goods> list=goodsService.getGoodsByCategoryId(categoryId);
        for(int i=0;i<list.size();i++){
            Goods good=list.get(i).setCategoryId(null);
            good.setOnSale(false);
            Assert.isTrue(good.updateById(),"数据更新失败！");
        }
        categoryDao.removeById(categoryId);
    }

    /**
     * 创建分类
     * @param categoryQuery
     */
    public void insertCategory(CategoryQuery categoryQuery) {
        Category   category=  Category.builder().createTime(LocalDateTime.now())
                    .enable(categoryQuery.getEnable())
                    .name(categoryQuery.getName())
                    .sortOrder(categoryQuery.getSortOrder())
                    .iconOn(categoryQuery.getIconOn())
                    .icon(categoryQuery.getIcon())
                    .updateTime(LocalDateTime.now())
                    .build();
            boolean flag= category.insert();
            Assert.isTrue(flag,"数据插入失败！");


        }

    public void updateStatus(Long id, Boolean enable) {
        Category category = categoryDao.getById(id);
        Assert.notNull(category,"分类不存在！");
        category.setEnable(enable);
        boolean flag = category.updateById();
        Assert.isTrue(flag, "数据更新失败！");

    }
}
