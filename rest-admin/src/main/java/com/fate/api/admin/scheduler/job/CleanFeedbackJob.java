package com.fate.api.admin.scheduler.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: parent
 * @description: 定时删除过期，已经查看的反馈 定时删除文件目录（crontab）
 * @author: chenyixin
 * @create: 2019-09-25 15:32
 **/
@Component
@Slf4j
public class CleanFeedbackJob extends Job{
    @Override
    void work() {

    }
}
