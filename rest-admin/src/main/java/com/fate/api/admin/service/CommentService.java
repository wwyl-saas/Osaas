package com.fate.api.admin.service;

import com.fate.common.dao.CommentDao;
import com.fate.common.model.StatisticModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-25 17:57
 **/
@Service
@Slf4j
public class CommentService {
    @Resource
    CommentDao commentDao;

    public List<StatisticModel> getAverageEvaluate(Long merchantId) {
        return commentDao.getAverageEvaluate(merchantId);
    }

    public List<StatisticModel> getShopAverageEvaluate(Long merchantId) {
        return commentDao.getShopAverageEvaluate(merchantId);
    }
}
