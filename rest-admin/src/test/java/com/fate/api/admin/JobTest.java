package com.fate.api.admin;

import com.fate.api.admin.scheduler.job.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-27 18:26
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
public class JobTest {
    @Autowired
    CreateDateStatistcJob createDateStatistcJob;
    @Autowired
    CreateWeekStatisticJob createWeekStatisticJob;
    @Autowired
    CreateMonthStatisticJob createMonthStatisticJob;
    @Autowired
    CreateQuarterStatisticJob createQuarterStatisticJob;
    @Autowired
    CloseExpiredCoupon closeExpiredCoupon;
    @Test
    public void createDateStatisticJob() throws Exception {
        createDateStatistcJob.execute();
    }

    @Test
    public void createWeekStatisticJob() throws Exception {
        createWeekStatisticJob.execute();
    }

    @Test
    public void createMonthStatisticJob() throws Exception {
        createMonthStatisticJob.execute();
    }

    @Test
    public void createQuarterStatisticJob() throws Exception {
        createQuarterStatisticJob.execute();
    }

    @Test
    public void closeExpiredCoupon() throws Exception {
        closeExpiredCoupon.execute();
    }
}
