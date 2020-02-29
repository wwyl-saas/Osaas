package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.CustomerCharge;
import com.fate.common.model.StatisticModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 会员充值表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-14
 */
public interface CustomerChargeDao extends IService<CustomerCharge> {

    List<CustomerCharge> selectByCustomerId(Long customerId);

    Integer getNewChargeCountBy(Long shopId, LocalDate date);

    BigDecimal getFactCharge(Long shopId, LocalDate now, Long userId);

    BigDecimal getTodayGiftCharge(Long shopId, LocalDate now, Long userId);

    IPage<CustomerCharge> getPageBy(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize);

    List<Long> getCustomerIdsBy(Long shopId, LocalDate date);

    List<CustomerCharge> getGroupSumByShopId(Long shopId, LocalDate date);

    List<StatisticModel> getChargeCount(Long merchantId, LocalDate date);

    List<StatisticModel> getChargeAmount(Long merchantId, LocalDate date);

    List<StatisticModel> getFactChargeAmount(Long merchantId, LocalDate date);

    List<StatisticModel> getGiftChargeAmount(Long merchantId, LocalDate date);

    IPage<CustomerCharge> getPageByShopIds(List<Long> list);

    IPage<CustomerCharge> getPageByMerchant(Integer pageIndex, Integer pageSize);
}
