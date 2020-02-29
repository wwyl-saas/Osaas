package com.fate.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.CustomerAppointment;
import com.fate.common.model.StatisticModel;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * C端用户预约表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface CustomerAppointmentMapper extends BaseMapper<CustomerAppointment> {

    List<String> getAppointmentDates(@Param("shopId") Long shopId, @Param("userId") Long userId, @Param("arriveDateStart") LocalDate arriveDateStart, @Param("arriveDateEnd") LocalDate arriveDateEnd, @Param("createDateStart")LocalDateTime createDateStart, @Param("createDateEnd") LocalDateTime createDateEnd, @Param("appointmentPhone") String appointmentPhone);

    List<StatisticModel> getAppointmentCount(@Param("merchantId")Long merchantId, @Param("date") LocalDate date);
}
