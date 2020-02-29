package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.GoodsDao;
import com.fate.common.entity.CustomerAppointment;
import com.fate.common.entity.Goods;
import com.fate.common.enums.AppointmentStatus;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.GoodsMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * 服务信息表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class GoodsDaoImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsDao {

    @Override
    public List<Goods> getBySaleAndNewLimit(Boolean onSale, Boolean isNew, Integer limit) {
        Assert.notNull(limit, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<Goods>().eq(Goods.ON_SALE,onSale).eq(Goods.IS_NEW,isNew).last("limit "+limit));
    }


    @Override
    public List<Goods> getBySaleAndHotLimit(Boolean onSale, Boolean isNew, Integer limit) {
        Assert.notNull(limit, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<Goods>().eq(Goods.ON_SALE,onSale).eq(Goods.IS_HOT,isNew).last("limit "+limit));
    }


    @Override
    public List<Goods> getBriefBySaleAndCategoryId(Boolean onSale, Long categoryId) {
        Assert.notNull(categoryId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<Goods>().eq(Goods.ON_SALE,onSale).eq(Goods.CATEGORY_ID,categoryId).last("order by "+Goods.SORT_ORDER));
    }


    @Override
    public List<Goods> getBriefBySaleAndKeyWords(Boolean onSale, String keyWords) {
        Assert.notNull(keyWords, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<Goods>().select(Goods.ID,Goods.BRIEF_PIC_URL,Goods.NAME,Goods.MARKET_PRICE,Goods.COUNTER_PRICE)
                .nested(i -> i.like(Goods.NAME,keyWords).or().like(Goods.KEYWORDS,keyWords)).and(i-> i.eq(Goods.ON_SALE,onSale)));
    }


    @Override
    public List<Goods> getBySaleAndGoodIds(Boolean onSale, List<Long> goodsIds) {
        Assert.notEmpty(goodsIds, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<Goods>().eq(Goods.ON_SALE,onSale).in(Goods.ID,goodsIds).last("order by "+Goods.SORT_ORDER));
    }


    @Override
    public List<Goods> getBySaleAndRecommendLimit(Boolean onSale, Boolean isRecommend, Integer limit) {
        Assert.notNull(limit, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<Goods>().eq(Goods.ON_SALE,onSale).eq(Goods.IS_RECOMMEND,isRecommend).last("limit "+limit));
    }


    @Override
    public List<Goods> getBriefBySaleAndCategoryIds(Boolean onSale, List<Long> list) {
        Assert.notEmpty(list, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<Goods>().select(Goods.ID,Goods.CATEGORY_ID,Goods.BRIEF_PIC_URL,Goods.NAME,Goods.MARKET_PRICE,Goods.COUNTER_PRICE).eq(Goods.ON_SALE,onSale).in(Goods.CATEGORY_ID,list));
    }

    @Override
    public List<Goods> getGoodsByIds(List<Long> ids) {
        Assert.notEmpty(ids,ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<Goods>().in(Goods.ID,ids));
    }

    @Override
    public List<Goods> getBySaleOrderBySellVolumeLimit(boolean onSale, int limit) {
        return baseMapper.selectList(new QueryWrapper<Goods>().eq(Goods.ON_SALE,onSale).last("order by "+Goods.SELL_VOLUME+" limit "+limit));
    }
    @Override
    public List<Goods>  getGoodsByCategoryId(Long categoryId){
        return baseMapper.selectList(new QueryWrapper<Goods>().eq(Goods.CATEGORY_ID,categoryId));
    }

    @Override
    public IPage<Goods> getGoodsBykeyWordAndGoodsNameAndOnSale(String keyWord, String goodsName, Boolean onSale, Integer pageIndex, Integer pageSize) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<Goods>();
        if (null!=keyWord){
            queryWrapper.eq(Goods.KEYWORDS,keyWord);
        }
        if (null!=goodsName){
            queryWrapper.eq(Goods.NAME,goodsName);
        }
        if(null!=onSale){
            queryWrapper.eq(Goods.ON_SALE,onSale);
        }
        queryWrapper.last("order by "+Goods.SORT_ORDER);
        return baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);

    }

    @Override
    public List<Goods> getAll() {
        return baseMapper.selectList(new QueryWrapper<Goods>().eq(Goods.ON_SALE,true));
    }



}
