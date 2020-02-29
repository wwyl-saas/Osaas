package com.fate.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.Comment;
import com.fate.common.model.StatisticModel;

import java.util.List;

/**
 * <p>
 * 服务评论表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface CommentMapper extends BaseMapper<Comment> {

    List<StatisticModel> getAverageEvaluate(Long merchantId);

    List<StatisticModel> getShopAverageEvaluate(Long merchantId);
}
