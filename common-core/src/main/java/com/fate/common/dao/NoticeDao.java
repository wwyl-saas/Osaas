package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Notice;

import java.util.List;

/**
 * <p>
 * 消息通知表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
public interface NoticeDao extends IService<Notice> {

    Integer getUnreadNoticeCount(Long userId);

    List<Notice> getListByUserId(Long userId);

    List<Notice> getNoticeListByMerchant(Long merchantId);
}
