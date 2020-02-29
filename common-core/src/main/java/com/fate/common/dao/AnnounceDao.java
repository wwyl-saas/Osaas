package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Announce;

import java.util.List;

/**
 * <p>
 * 公告表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
public interface AnnounceDao extends IService<Announce> {
    Integer getCount();

    List<Announce> getList();
}
