package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Goods;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务信息表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface GoodsDao extends IService<Goods> {

    List<Goods> getBySaleAndNewLimit(Boolean onSale,Boolean isNew,Integer limit);
    List<Goods> getBySaleAndHotLimit(Boolean onSale,Boolean isNew,Integer limit);
    List<Goods> getBriefBySaleAndCategoryId(Boolean onSale,Long categoryId);
    List<Goods> getBriefBySaleAndKeyWords(Boolean onSale,String keyWords);
    List<Goods> getBySaleAndGoodIds(Boolean onSale,List<Long> goodsIds);
    List<Goods> getBySaleAndRecommendLimit(Boolean onSale,Boolean isRecommend,Integer limit);
    List<Goods> getBriefBySaleAndCategoryIds(Boolean onSale,List<Long> list);
    List<Goods> getGoodsByIds(List<Long> ids);
    List<Goods> getBySaleOrderBySellVolumeLimit(boolean onSale, int limit);
    List<Goods>  getGoodsByCategoryId(Long categoryId);

    IPage<Goods> getGoodsBykeyWordAndGoodsNameAndOnSale(String keyWord, String goodsName, Boolean onSale, Integer pageIndex, Integer pageSize);

    List<Goods> getAll();


}
