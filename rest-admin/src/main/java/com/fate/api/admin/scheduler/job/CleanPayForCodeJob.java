package com.fate.api.admin.scheduler.job;

import com.fate.api.admin.service.AnotherPayService;
import com.fate.common.entity.PayForAnother;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 定时清理昨日x点前生成的代付码
 * @author: songjinghuan
 * @create:
 **/
@Component
@Slf4j
public class CleanPayForCodeJob extends Job{
    @Autowired
    AnotherPayService anotherPayService;

    @Override
    void work() {
        log.info("定时清理昨日x点前生成的代付码，开始执行......");
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();
        try {
            List<PayForAnother> payForAnothers=anotherPayService.getUnusedBeforeCreateTime(LocalDateTime.now().minusDays(1));
            if (CollectionUtils.isNotEmpty(payForAnothers)){
                List<Long> ids=payForAnothers.parallelStream().map(payForAnother -> payForAnother.getId()).collect(Collectors.toList());
                anotherPayService.deleteBatch(ids);
            }
            stopWatch.stop();
            log.info("定时清理昨日x点前生成的代付码，执行完毕，用时{}ms",stopWatch.getTotalTimeMillis());
        }catch (Exception e){
            log.error("定时清理昨日x点前生成的代付码：",e);
        }
    }
}
