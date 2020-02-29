package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.GoodsMerchantUser;

import java.util.List;

/**
 * <p>
 * 商品和技师表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-06
 */
public interface GoodsMerchantUserDao extends IService<GoodsMerchantUser> {

    List<GoodsMerchantUser> getByGoodsId(Long goodsId);

    List<GoodsMerchantUser> getByMerchantUserId(Long merchantUserId);

    void deleteByUserId(Long id);


    void deleteByGoodsId(Long id);
}
