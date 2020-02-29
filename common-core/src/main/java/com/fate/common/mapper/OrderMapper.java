package com.fate.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.Order;
import com.fate.common.model.StatisticModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface OrderMapper extends BaseMapper<Order> {

    BigDecimal getFactConsumeAmount(@Param("shopId") Long shopId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<Long> getOrderIdsByShopAndUserAndDate(@Param("shopId") Long shopId,@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<StatisticModel> getOrderCount(@Param("merchantId")Long merchantId,@Param("startTime") LocalDateTime startTime,@Param("endTime") LocalDateTime endTime);
}
