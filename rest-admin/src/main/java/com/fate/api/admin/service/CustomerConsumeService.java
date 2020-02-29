package com.fate.api.admin.service;

import com.fate.common.dao.CustomerConsumeDao;
import com.fate.common.entity.CustomerConsume;
import com.fate.common.enums.ConsumeType;
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
 * @create: 2019-09-25 18:02
 **/
@Service
@Slf4j
public class CustomerConsumeService {
    @Resource
    CustomerConsumeDao customerConsumeDao;

    /**
     * 获取消费记录
     * @param id
     * @param merchantId
     * @return
     */
    public CustomerConsume getById(Long id, Long merchantId) {
        return customerConsumeDao.getByIdAndMerchantId(id,merchantId);
    }
    public List<StatisticModel> getFactConsumeAmount(Long merchantId, LocalDate date) {
        return customerConsumeDao.getConsumeAmount(merchantId,date, ConsumeType.WX_PAY);
    }

    public List<StatisticModel> getRemainderConsumeAmount(Long merchantId, LocalDate date) {
        return customerConsumeDao.getConsumeAmount(merchantId,date, ConsumeType.REMAINDER);
    }

    public List<StatisticModel> getConsumeAmount(Long merchantId, LocalDate date) {
        return customerConsumeDao.getConsumeAmount(merchantId,date, null);
    }
}
