package com.fate.api.admin.scheduler;

import com.fate.api.admin.scheduler.job.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @program: parent
 * @description: 定时任务调度
 * 秒（0~59）
 * 分钟（0~59）
 * 小时（0~23）
 * 天（月）（0~31，但是你需要考虑你月的天数）
 * 月（0~11）
 * 天（星期）（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）
 * 年份（1970－2099）
 * @author: chenyixin
 * @create: 2019-06-13 06:58
 **/
@Component
@Slf4j
public class Scheduler {
    @Autowired
    CancelOrderJob cancelOrderJob;
    @Autowired
    CleanAppointmentJob cleanAppointmentJob;
    @Autowired
    CleanCancelOrderJob cleanCancelOrderJob;
    @Autowired
    CleanPayForCodeJob cleanPayForCodeJob;
    @Autowired
    CloseExpiredCoupon closeExpiredCoupon;
    @Autowired
    CreateDateStatistcJob createDateStatistcJob;
    @Autowired
    CreateWeekStatisticJob createWeekStatisticJob;
    @Autowired
    CreateMonthStatisticJob createMonthStatisticJob;
    @Autowired
    CreateQuarterStatisticJob createQuarterStatisticJob;


    @Scheduled(cron = "5 0 0 * * *")
    public void executionDateStatistic(){
        createDateStatistcJob.execute();
    }

    @Scheduled(cron = "0 15 0 ? * MON")
    public void executionWeekStatistic(){
        createWeekStatisticJob.execute();
    }

    @Scheduled(cron = "0 30 0 1 * ?")
    public void executionMonthStatistic(){
        createMonthStatisticJob.execute();
    }

    @Scheduled(cron = "5 0 1 1 1/3 ?")
    public void executionQuarterStatistic(){
        createQuarterStatisticJob.execute();
    }

    @Scheduled(cron = "0 18 0/1 * * *")
    public void executionOrderCancel(){
        cancelOrderJob.execute();
    }

    @Scheduled(cron = "0 20 3 * * *")
    public void executionCleanAppointments(){
        cleanAppointmentJob.execute();
    }

    @Scheduled(cron = "0 10 3 * * *")
    public void executionOrderClean(){
        cleanCancelOrderJob.execute();
    }

    @Scheduled(cron = "0 12 3 * * *")
    public void executionCleanPayCode(){
        cleanPayForCodeJob.execute();
    }

    @Scheduled(cron = "0 14 3 * * *")
    public void executionCloseCoupon(){
        closeExpiredCoupon.execute();
    }

    //todo 卡过期
}
