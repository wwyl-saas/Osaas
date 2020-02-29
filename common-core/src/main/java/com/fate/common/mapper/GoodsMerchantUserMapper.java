package com.fate.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.GoodsMerchantUser;

import java.util.List;

/**
 * <p>
 * 商品和技师表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-06
 */
public interface GoodsMerchantUserMapper extends BaseMapper<GoodsMerchantUser> {

    List<Long> getUserIdsByGoodsId(Long goodsId);
}
