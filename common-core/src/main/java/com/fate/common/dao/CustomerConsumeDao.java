package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.CustomerConsume;
import com.fate.common.enums.ConsumeType;
import com.fate.common.model.StatisticModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 会员充值消费记录表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface CustomerConsumeDao extends IService<CustomerConsume> {

    List<CustomerConsume> selectByCustomerAndType(Long customerId, Integer type);

    List<CustomerConsume> selectByCustomerId(Long customerId);

    CustomerConsume selectByOrderId(Long orderId);

    BigDecimal getFactConsumeAmount(Long shopId, LocalDate date);

    BigDecimal getStoreConsumeAmount(Long shopId, LocalDate now);

    BigDecimal getFactConsumeAmountByOrderIds(Long shopId, List<Long> orderIds);

    BigDecimal getStoreConsumeAmountByOrderIds(Long shopId, List<Long> orderIds);

    List<Long> getOrderIdsBy(Long shopId, LocalDate date, ConsumeType consumeType);

    BigDecimal getConsumeAmountByCustomerId(Long customerId);

    CustomerConsume getByIdAndMerchantId(Long id, Long merchantId);

    List<StatisticModel> getConsumeAmount(Long merchantId, LocalDate date, ConsumeType type);

    BigDecimal getTodayFactConsumeAmount(Long shopId, Long userId, LocalDate date);

    BigDecimal getTodayStoreConsumeAmount(Long shopId, Long userId, LocalDate date);




    IPage<CustomerConsume> getPageByMerchant(Long pageIndex, Long pageSize);

    IPage<CustomerConsume> getPageByShopIds(Long pageIndex, Long pageSize, List<Long> list);
}
