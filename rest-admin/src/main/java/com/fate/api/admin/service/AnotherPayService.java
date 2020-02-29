package com.fate.api.admin.service;

import com.fate.common.dao.PayForAnotherDao;
import com.fate.common.entity.PayForAnother;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-26 12:23
 **/
@Slf4j
@Service
public class AnotherPayService {
    @Resource
    PayForAnotherDao payForAnotherDao;


    public List<PayForAnother> getUnusedBeforeCreateTime(LocalDateTime datetime) {
        return payForAnotherDao.getUnusedBeforeCreateTime(datetime);
    }

    public void deleteBatch(List<Long> ids) {
        payForAnotherDao.removeByIds(ids);
    }
}
