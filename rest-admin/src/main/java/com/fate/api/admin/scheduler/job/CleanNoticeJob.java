package com.fate.api.admin.scheduler.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: parent
 * @description: 定期删除已读消息 //todo 已读的通知公告删除
 * @author: chenyixin
 * @create: 2019-09-25 15:44
 **/
@Component
@Slf4j
public class CleanNoticeJob extends Job{
    @Override
    void work() {

    }
}
