package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.MerchantShop;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户门店表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MerchantShopMapper extends BaseMapper<MerchantShop> {
    @SqlParser(filter = true)
    List<MerchantShop> getShopsWithRole(@Param("merchantId") Long merchantId,@Param("userId") Long userId);

    List<Map<String,Object>> getIdAndNameByGoodsId(Long goodsId);

    List<Map<String, Object>> getshopNamesByIds(List<Long> shopIdsList);
}
