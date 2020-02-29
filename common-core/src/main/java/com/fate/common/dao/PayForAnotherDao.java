package com.fate.common.dao;

import com.fate.common.entity.PayForAnother;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-04
 */
public interface PayForAnotherDao extends IService<PayForAnother> {

    PayForAnother getByPayCode(Long code);

    List<PayForAnother> getUnusedBeforeCreateTime(LocalDateTime datetime);
}
