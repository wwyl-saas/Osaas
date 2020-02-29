package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Comment;
import com.fate.common.model.StatisticModel;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 服务评论表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface CommentDao extends IService<Comment> {

    Integer getCommentCountByGoodsId(Long goodsId);

    Comment getLastCommentByGoodsId(Long goodsId);

    List<Comment> getListByGoodsId(Long goodsId);

    IPage<Comment> getCommentByShopIdAndUserId(Page page, Long shopId, Long merchantUserId);

    Integer getCommentCountByUserId(Long id);

    IPage<Comment> getCommentByUserId(Long pageIndex, Long pageSize, Long id);

    List<Comment> getUserLastCommentsLimit(Long merchantUserId, int limit);

    IPage<Comment> getPageCommentByUserId(Page<Comment> page, Long id);

    IPage<Comment> getCommentByUserIdAndGradeAndPictureAndDate(Integer commentType, Integer havePicture, LocalDate createDateStart, LocalDate createDateEnd, Long pageIndex, Long pageSize, Long userId);

    List<StatisticModel> getAverageEvaluate(Long merchantId);

    List<StatisticModel> getShopAverageEvaluate(Long merchantId);

    IPage<Comment>  getPageBy(Long goodsId, Integer type, Integer havePicture, Long pageIndex, Long pageSize);
}
