package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.GoodsShop;

import java.util.List;

/**
 * <p>
 * 服务门店表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface GoodsShopDao extends IService<GoodsShop> {

    List<Long> getShopIdsByGoodsId(Long goodsId);

    List<GoodsShop> getGoodsByShopId(Long shopId);

    List<Long> getGoodsIdsByShopId(Long shopId);

    void removeByShopId(Long id);

    void deleteByGoodsId(Long id);
}
