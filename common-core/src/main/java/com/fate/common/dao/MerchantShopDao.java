package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.MerchantShop;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户门店表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MerchantShopDao extends IService<MerchantShop> {

    List<MerchantShop> selectAll();

    List<MerchantShop>  getByIds(List<Long> shopIds);

    List<MerchantShop> getByWithShopRoles(Long merchantId,Long userId);

    IPage<MerchantShop> getMerchantShopByNamePage(Integer pageIndex, Integer pageSize, String name);

    List<Map<String, Object>> getIdAndNameByGoodsId(Long goodsId);

    List<Map<String, Object>> getshopNamesByIds(List<Long> shopIdsList);

    List<MerchantShop> getAllEnable(Long merchantId);
}
