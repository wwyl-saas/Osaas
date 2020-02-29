package com.fate.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.CustomerConsume;
import com.fate.common.enums.ConsumeType;
import com.fate.common.model.StatisticModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 会员充值消费记录表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface CustomerConsumeMapper extends BaseMapper<CustomerConsume> {

    BigDecimal getFactConsumeAmount(@Param("shopId") Long shopId,@Param("startTime") LocalDateTime startTime,@Param("endTime") LocalDateTime endTime);

    BigDecimal getStoreConsumeAmount(@Param("shopId") Long shopId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    BigDecimal getFactConsumeAmountByOrderIds(@Param("shopId") Long shopId,@Param("orderIds")  List<Long> orderIds);

    BigDecimal getStoreConsumeAmountByOrderIds(@Param("shopId") Long shopId,@Param("orderIds")  List<Long> orderIds);

    List<Long> getOrderIdsBy(@Param("shopId") Long shopId, @Param("consumeType")Integer consumeType, @Param("startTime") LocalDateTime startTime,@Param("endTime") LocalDateTime endTime);

    BigDecimal getConsumeAmountByCustomerId(Long customerId);

    List<StatisticModel> getConsumeAmount(@Param("merchantId")Long merchantId, @Param("startTime") LocalDateTime startTime,@Param("endTime") LocalDateTime endTime,@Param("consumeType") Integer consumeType);

    BigDecimal getTodayFactConsumeAmount(@Param("shopId") Long shopId,@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    BigDecimal getTodayStoreConsumeAmount(@Param("shopId") Long shopId,@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
