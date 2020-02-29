package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.CustomerAppointment;
import com.fate.common.enums.AppointmentStatus;
import com.fate.common.model.StatisticModel;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * C端用户预约表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface CustomerAppointmentDao extends IService<CustomerAppointment> {

    List<CustomerAppointment> getAppointmentByCustomerId(Long customerId);

    List<CustomerAppointment> getByShopAndUserIdAndDate(Long shopId, Long userId, LocalDate date);

    IPage<CustomerAppointment> getByShopAndUserIdAndDatePage(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize);

    Integer getCountByShopAndUserIdAndDate(Long shopId, Long userId, LocalDate now);

    IPage<CustomerAppointment> getByShopAndUserIdAndStatusAndPhoneAndCreateDatePage(Long shopId, Long userId, LocalDate arriveDate, AppointmentStatus appointmentStatus, LocalDate createDateStart, LocalDate createDateEnd, String appointmentPhone, Integer pageIndex, Integer pageSize);

    Integer getCountByShopAndUserIdAndStatusAndPhoneAndCreateDate(Long shopId, Long userId, LocalDate arriveDate, AppointmentStatus status, LocalDate createDateStart, LocalDate createDateEnd, String appointmentPhone);

    List<String> getAppointmentDates(Long shopId, Long userId, LocalDate arriveDate, LocalDate createDateStart, LocalDate createDateEnd, String appointmentPhone, int days);

    Integer getAppointmentCount(Long shopId, Long userId, AppointmentStatus waitingConfirm);

    IPage<CustomerAppointment> getAppointmentByDateRangeAndMerchantUserId(Long shopId, Long merchantUserId,Integer appointStatus, LocalDate startDate, LocalDate endDate, Integer pageIndex, Integer pageSize);

    Integer getAppointmentCountByShopAndDate(Long shopId, LocalDate date,Long userId);

    List<CustomerAppointment> getByArriveDateBefore(LocalDate date);

    List<StatisticModel> getAppointmentCount(Long merchantId, LocalDate date);
}
