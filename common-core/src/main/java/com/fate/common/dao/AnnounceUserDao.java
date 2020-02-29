package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.AnnounceUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 公告查看表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
public interface AnnounceUserDao extends IService<AnnounceUser> {

    Integer getCountBy(Long userId);

    List<AnnounceUser> getListByUserIdAndAnnounceIds(Long userId, List<Long> announceIds);

    AnnounceUser getByUserIdAndAnnounceId(Long id, Long announceId);
}
