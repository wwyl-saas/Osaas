package com.fate.api.admin.service;

import com.fate.common.dao.CustomerAppointmentDao;
import com.fate.common.entity.CustomerAppointment;
import com.fate.common.model.StatisticModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-25 18:01
 **/
@Service
@Slf4j
public class CustomerAppointmentService {
    @Resource
    CustomerAppointmentDao customerAppointmentDao;

    public List<CustomerAppointment> getByArriveDateBefore(LocalDate date) {
        return customerAppointmentDao.getByArriveDateBefore(date);
    }

    /**
     * 批量删除
     * @param appointmentIds
     */
    public void deleteBatchByIds(List<Long> appointmentIds) {
        customerAppointmentDao.removeByIds(appointmentIds);
    }

    public List<StatisticModel> getAppointmentCount(Long merchantId, LocalDate date) {
        return customerAppointmentDao.getAppointmentCount(merchantId,date);
    }
}
