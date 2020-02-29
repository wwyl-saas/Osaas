package com.fate.api.admin.scheduler.job;

import com.fate.api.admin.service.CustomerAppointmentService;
import com.fate.common.entity.CustomerAppointment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 定时清理过期预约(从前天的开始删除)
 * @author: chenyixin
 * @create: 2019-09-25 15:30
 **/
@Component
@Slf4j
public class CleanAppointmentJob extends Job{
    @Autowired
    CustomerAppointmentService customerAppointmentService;


    //todo 用户当日具有预约店预约范围内（可以加大1个小时范围）的订单 则给用户赠送1预约积分
    @Override
    void work() {
        log.info("定时清理两天前过期预约，开始执行......");
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();
        try {
            List<CustomerAppointment> customerAppointments=customerAppointmentService.getByArriveDateBefore(LocalDate.now().minusDays(1));
            if (CollectionUtils.isNotEmpty(customerAppointments)){
                List<Long> appointmentIds=customerAppointments.parallelStream().map(appointment -> appointment.getId()).collect(Collectors.toList());
                customerAppointmentService.deleteBatchByIds(appointmentIds);
            }
            stopWatch.stop();
            log.info("定时清理两天前过期预约，执行完毕，用时{}ms",stopWatch.getTotalTimeMillis());
        }catch (Exception e){
            log.error("清理取消和已过期预约报错：",e);
        }
    }
}
