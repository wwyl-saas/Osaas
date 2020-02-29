package com.fate.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.Banner;

import java.util.List;

/**
 * <p>
 * 首页banner表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface BannerMapper extends BaseMapper<Banner> {
    List<Banner> findlistAll();
}
