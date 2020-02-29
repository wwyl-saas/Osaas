package com.fate.api.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.api.customer.dto.CommentDto;
import com.fate.api.customer.dto.PageDto;
import com.fate.api.customer.query.CommentCreateQuery;
import com.fate.api.customer.query.CommentQuery;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.dao.CommentDao;
import com.fate.common.dao.MerchantPostTitleDao;
import com.fate.common.dao.OrderDao;
import com.fate.common.entity.*;
import com.fate.common.enums.OrderStatus;
import com.fate.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Resource
    private OrderDao orderDao;
    @Autowired
    MerchantUserService merchantUserService;
    @Resource
    MerchantPostTitleDao merchantPostTitleDao;




    /**
     * 根据商品ID获取评论数量
     * @param goodsId
     * @return
     */
    public Integer getCommentCountByGoodsId(Long goodsId) {
        return commentDao.getCommentCountByGoodsId(goodsId);
    }

    /**
     * 根据商品ID获取最新评论
     * @param goodsId
     * @return
     */
    public CommentDto getLastCommentByGoodsId(Long goodsId) {
        CommentDto commentDto=null;
        Comment comment=commentDao.getLastCommentByGoodsId(goodsId);
        if (comment!=null){
            commentDto=BeanUtil.mapper(comment,CommentDto.class);
            if (StringUtils.isNotBlank(comment.getPictureUrls())){
                List<String> list= Arrays.stream(comment.getPictureUrls().split(",")).map(url->cdnDomain+url).collect(Collectors.toList());
                commentDto.setPictureList(list);
            }
        }
        return commentDto;
    }

    /**
     * 查询商品评论列表，按评论时间逆序
     * @param goodsId
     * @return
     */
    public List<CommentDto> getListByGoodsId(Long goodsId) {
        List<CommentDto> result= Lists.emptyList();
        List<Comment> commentList=commentDao.getListByGoodsId(goodsId);
        if (CollectionUtils.isNotEmpty(commentList)){
            result=commentList.stream().map(comment -> {
                CommentDto commentDto= BeanUtil.mapper(comment,CommentDto.class);
                if (StringUtils.isNotBlank(comment.getPictureUrls())){
                    List<String> list= Arrays.stream(comment.getPictureUrls().split(",")).map(url->cdnDomain+url).collect(Collectors.toList());
                    commentDto.setPictureList(list);
                }
                return commentDto;
            }).collect(Collectors.toList());
        }
        return result;
    }


    /**
     * 插入商品评论信息
     * @param coms
     * @return
     */
    public void complishOrderComment(CommentCreateQuery coms) {
        Order ord= orderDao.getById(coms.getOrderId());
        Assert.notNull(ord,"订单不存在");
        Customer customer=CurrentCustomerUtil.getCustomer();
        List<Comment> list=new ArrayList<Comment>();
        coms.getQueries().stream().forEach(createQuery->{
            Comment comment=new Comment();
            comment.setGoodsId(createQuery.getGoodsId());
            comment.setGrade(createQuery.getGrade());
            comment.setRemark(createQuery.getRemark());
            comment.setPictureUrls(createQuery.getPictureUrls());
            comment.setOrderId(coms.getOrderId());
            comment.setMerchantUserId(ord.getMerchantUserId());
            MerchantUser merchantUser=merchantUserService.getMerchantUserById(ord.getMerchantUserId());
            comment.setMerchantUserName(merchantUser.getNickname());
            MerchantPostTitle title = merchantPostTitleDao.getById(merchantUser.getPostTitleId());
            if (title!=null){
                comment.setMerchantUserTitle(title.getName());
            }
            comment.setShopId(ord.getShopId());
            comment.setIsAnonymous(!createQuery.getAnonymousType());
            comment.setCreateTime(LocalDateTime.now());
            comment.setCustomerName(customer.getName());
            if(comment.getAvatar()!=null){
                comment.setAvatar(customer.getAvatar());
            }
            list.add(comment);
        });
        Assert.isTrue(commentDao.saveBatch(list),"插入数据失败");

        ord.setStatus(OrderStatus.COMPLETED);
        Assert.isTrue(ord.updateById(),"更新数据失败");
    }

    /**
     * 获取评价列表
     * @param goodsId
     * @param type
     * @param havePicture
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageDto<CommentDto> getGoodsComments(Long goodsId, Integer type, Integer havePicture, Long pageIndex, Long pageSize) {
        List<CommentDto> result= Lists.emptyList();
        IPage<Comment> commentIPage =commentDao.getPageBy(goodsId,type,havePicture,pageIndex,pageSize);
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
}
