package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.BannerDao;
import com.fate.common.entity.Banner;
import com.fate.common.enums.BannerPoisition;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.BannerMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class BannerDaoImpl extends ServiceImpl<BannerMapper, Banner> implements BannerDao {

    @Override
    public List<Banner> getByPositionId(BannerPoisition positionId) {
        Assert.notNull(positionId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.selectList(new QueryWrapper<Banner>().eq(Banner.BANNER_POSITION_ID,positionId));
    }
}
