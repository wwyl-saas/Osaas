package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.GoodsAttribute;

import java.util.List;

/**
 * <p>
 * 服务规格表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface GoodsAttributeDao extends IService<GoodsAttribute> {

    /**
     * 获取排序的商品属性列表
     * @param goodsId
     * @return
     */
    List<GoodsAttribute> getOrderedByGoodsId(Long goodsId);

    /**
     *根据商品id商品属性列表
     * @param goodsId
     * @return
     */
    void removeByGoodsId(Long goodsId);

    /**
     *根据商品id商品属性列表
     * @param
     * @return
     */
    void removeByGoodsIds(List<Long> goodsIds);
}
