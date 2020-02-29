package com.fate.common.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.common.entity.CustomerCharge;
import com.fate.common.model.StatisticModel;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 会员充值表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-14
 */
public interface CustomerChargeMapper extends BaseMapper<CustomerCharge> {
    BigDecimal getFactCharge(@Param("shopId") Long shopId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("userId") Long userId);

    BigDecimal getTodayGiftCharge(@Param("shopId") Long shopId,@Param("startTime") LocalDateTime startTime,@Param("endTime") LocalDateTime endTime, @Param("userId") Long userId);

    List<Long> getCustomerIdsBy(@Param("shopId")Long shopId,@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<CustomerCharge> getGroupSumByShopId(@Param("shopId")Long shopId,@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<StatisticModel> getChargeCount(@Param("merchantId")Long merchantId, @Param("startTime") LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);

    List<StatisticModel> getChargeAmount(@Param("merchantId")Long merchantId, @Param("startTime") LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);

    List<StatisticModel> getFactChargeAmount(@Param("merchantId")Long merchantId, @Param("startTime") LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);

    List<StatisticModel> getGiftChargeAmount(@Param("merchantId")Long merchantId, @Param("startTime") LocalDateTime startTime, @Param("endTime")LocalDateTime endTime);

    List<StatisticModel> get(@Param("merchantId")Long merchantId);

    IPage<CustomerCharge> getPageByShopIds(QueryWrapper queryWrapper);
}
