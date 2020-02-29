package com.fate.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.GoodsShop;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 服务门店表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface GoodsShopMapper extends BaseMapper<GoodsShop> {

    @Select("select shop_id from t_goods_shop where goods_id= #{goodsId}")
    List<Long> selectShopIdsByGoodsId(Long goodsId);

    @Select("select goods_id from t_goods_shop where shop_id= #{shopId}")
    List<Long> getGoodsIdsByShopId(Long shopId);
}
