package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CommentDao;
import com.fate.common.entity.Comment;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.CommentMapper;
import com.fate.common.model.StatisticModel;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 服务评论表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class CommentDaoImpl extends ServiceImpl<CommentMapper, Comment> implements CommentDao {

    @Override
    public Integer getCommentCountByGoodsId(Long goodsId) {
        return baseMapper.selectCount(new QueryWrapper<Comment>().eq(Comment.GOODS_ID, goodsId));
    }

    @Override
    public Comment getLastCommentByGoodsId(Long goodsId) {
        return baseMapper.selectOne(new QueryWrapper<Comment>().eq(Comment.GOODS_ID, goodsId).last("order by create_time desc limit 1"));
    }

    @Override
    public List<Comment> getListByGoodsId(Long goodsId) {
        return baseMapper.selectList(new QueryWrapper<Comment>().eq(Comment.GOODS_ID, goodsId).last("order by create_time"));
    }

    @Override
    public IPage<Comment> getCommentByShopIdAndUserId(Page page, Long shopId, Long merchantUserId) {
        return baseMapper.selectPage(page, Wrappers.<Comment>lambdaQuery().eq(Comment::getShopId, shopId).eq(Comment::getMerchantUserId, merchantUserId).orderByDesc(Comment::getCreateTime));
    }

    @Override
    public Integer getCommentCountByUserId(Long userId) {
        return baseMapper.selectCount(Wrappers.<Comment>lambdaQuery().eq(Comment::getMerchantUserId, userId));
    }

    @Override
    public IPage<Comment> getCommentByUserId(Long pageIndex, Long pageSize, Long userId) {
        Page<Comment> page = new Page<>(pageIndex, pageSize);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Comment::getMerchantUserId, userId).orderByDesc(Comment::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public List<Comment> getUserLastCommentsLimit(Long merchantUserId, int limit) {
        Assert.notNull(merchantUserId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<Comment>().eq(Comment.MERCHANT_USER_ID, merchantUserId).orderByDesc(Comment.CREATE_TIME).last("limit " + limit));
    }

    @Override
    public IPage<Comment> getPageCommentByUserId(Page<Comment> page, Long userId) {
        return baseMapper.selectPage(page, new QueryWrapper<Comment>().eq(Comment.MERCHANT_USER_ID, userId).orderByDesc(Comment.CREATE_TIME));
    }

    @Override
    public IPage<Comment> getCommentByUserIdAndGradeAndPictureAndDate(Integer commentType, Integer havePicture, LocalDate createDateStart, LocalDate createDateEnd, Long pageIndex, Long pageSize, Long userId) {
        Assert.notNull(userId, ResponseInfo.PARAM_NULL.getMsg());
        Page<Comment> page = new Page<>(pageIndex, pageSize);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq(Comment.MERCHANT_USER_ID, userId);
        translateType(commentType,wrapper);
        if (havePicture!=null){
            if (havePicture==1){
                wrapper.isNotNull(Comment.PICTURE_URLS);
            }else{
                wrapper.isNull(Comment.PICTURE_URLS);
            }
        }
        if (createDateStart!=null && createDateEnd!=null){
            wrapper.between(Comment.CREATE_TIME,createDateStart.atTime(LocalTime.MIN),createDateEnd.atTime(23,59,59));
        }
        wrapper.orderByDesc(Comment.CREATE_TIME);
        return page(page, wrapper);
    }

    @Override
    public List<StatisticModel> getAverageEvaluate(Long merchantId) {
        return baseMapper.getAverageEvaluate(merchantId);
    }

    @Override
    public List<StatisticModel> getShopAverageEvaluate(Long merchantId) {
        return baseMapper.getShopAverageEvaluate(merchantId);
    }

    @Override
    public IPage<Comment>  getPageBy(Long goodsId, Integer type, Integer havePicture, Long pageIndex, Long pageSize) {
        Assert.notNull(goodsId, ResponseInfo.PARAM_NULL.getMsg());
        Page<Comment> page = new Page<>(pageIndex, pageSize);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq(Comment.GOODS_ID, goodsId);
        translateType(type,wrapper);
        if (havePicture!=null){
            if (havePicture==1){
                wrapper.isNotNull(Comment.PICTURE_URLS);
            }else{
                wrapper.isNull(Comment.PICTURE_URLS);
            }
        }
        wrapper.orderByDesc(Comment.CREATE_TIME);
        return page(page, wrapper);
    }

    private void translateType(Integer type,QueryWrapper<Comment> wrapper){
        if (type != null) {
            switch (type) {
                case 0:
                    wrapper.ge(Comment.GRADE, 40);
                    break;
                case 1:
                    wrapper.ge(Comment.GRADE, 30);
                    wrapper.lt(Comment.GRADE, 40);
                    break;
                case 2:
                    wrapper.lt(Comment.GRADE, 30);
                    break;
                default:
                    break;
            }
        }
    }



}
