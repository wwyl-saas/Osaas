package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Banner;
import com.fate.common.enums.BannerPoisition;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface BannerDao extends IService<Banner> {

    List<Banner> getByPositionId(BannerPoisition positionId);
}
