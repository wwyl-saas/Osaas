package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.FeedbackDao;
import com.fate.common.entity.Feedback;
import com.fate.common.mapper.CustomerFeedbackMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 会员反馈表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class FeedbackDaoImpl extends ServiceImpl<CustomerFeedbackMapper, Feedback> implements FeedbackDao {


}
