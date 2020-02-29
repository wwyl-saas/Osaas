package com.fate.api.merchant.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.api.merchant.dto.CommentDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.dao.CommentDao;
import com.fate.common.dao.OrderDao;
import com.fate.common.entity.Comment;
import com.fate.common.entity.MerchantUser;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 评论相关
 * @author: chenyixin
 * @create: 2019-06-08 17:25
 **/
@Service
@Slf4j
public class CommentService {
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Resource
    private CommentDao commentDao;


    /**
     * 获取用户在门店下评论数量
     * @return
     */
    public Integer countByShopIdAndUserId() {
        MerchantUser merchantUser = CurrentUserUtil.getMerchantUser();
        Integer count = commentDao.getCommentCountByUserId(merchantUser.getId());
        return count ;
    }


    /**
     * 获取评论列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageDto<CommentDto> getCommentList(Integer commentType, Integer havePicture, LocalDate createDateStart,LocalDate createDateEnd, Long pageIndex, Long pageSize) {
        List<CommentDto> result= Lists.emptyList();
        IPage<Comment> commentIPage = commentDao.getCommentByUserIdAndGradeAndPictureAndDate(commentType,havePicture,createDateStart,createDateEnd,pageIndex,pageSize,CurrentUserUtil.getMerchantUser().getId());
        if (CollectionUtils.isNotEmpty(commentIPage.getRecords())){
            result=commentIPage.getRecords().stream().map(comment -> {
                CommentDto commentDto= BeanUtil.mapper(comment,CommentDto.class);
                if (StringUtils.isNotBlank(comment.getPictureUrls())){
                    List<String> list= Arrays.stream(comment.getPictureUrls().split(",")).map(url->cdnDomain+url).collect(Collectors.toList());
                    commentDto.setPictureList(list);
                }
                if (comment.getGrade()!=null){
                    BigDecimal grade=BigDecimal.valueOf(comment.getGrade().longValue()).divide(BigDecimal.TEN).setScale(1,BigDecimal.ROUND_HALF_UP);
                    commentDto.setGradeString(grade.toPlainString());
                }
                return commentDto;
            }).collect(Collectors.toList());
        }
       return new PageDto<>(pageIndex,pageSize,commentIPage.getTotal(),commentIPage.getPages(),result);
    }

//    /**
//     * 获取评论列表
//     * @param pageIndex
//     * @param pageSize
//     * @return
//     */
//    public PageDto<CommentDto> getCommentList(Long pageIndex, Long pageSize) {
//        List<CommentDto> result= Lists.emptyList();
//        Page<Comment> page=new Page<>(pageIndex,pageSize);
//        IPage<Comment> commentIPage = commentDao.getPageCommentByUserId(page,CurrentUserUtil.getMerchantUser().getId());
//        result=commentIPage.getRecords().stream().map(comment -> {
//                CommentDto commentDto= BeanUtil.mapper(comment,CommentDto.class);
//            return commentDto;
//            }).collect(Collectors.toList());
//        return new PageDto<>(pageIndex,pageSize,page.getTotal(),page.getPages(),result);
//    }

    public List<Comment> getUserLastCommentsLimit(Long merchantUserId, int limit) {
        return commentDao.getUserLastCommentsLimit(merchantUserId,limit);
    }
}
