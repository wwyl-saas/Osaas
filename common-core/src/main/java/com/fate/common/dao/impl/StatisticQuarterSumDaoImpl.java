package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.StatisticQuarterSumDao;
import com.fate.common.entity.StatisticQuarterSum;
import com.fate.common.enums.StatisticType;
import com.fate.common.mapper.StatisticQuarterSumMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商户首页指标表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-08-26
 */
@Repository
public class StatisticQuarterSumDaoImpl extends ServiceImpl<StatisticQuarterSumMapper, StatisticQuarterSum> implements StatisticQuarterSumDao {

    @Override
    public List<StatisticQuarterSum> getList(Long shopId, Long  userId, StatisticType statisticType, Integer year, Integer startQuarter, Integer endQuarter) {
        QueryWrapper queryWrapper=new QueryWrapper<StatisticQuarterSum>().eq(StatisticQuarterSum.TYPE,statisticType).eq(StatisticQuarterSum.YEAR,year )
                .between(StatisticQuarterSum.QUARTER,startQuarter,endQuarter);
        if (shopId!=null){
            queryWrapper.eq(StatisticQuarterSum.SHOP_ID,shopId);
        }
        if (userId!=null){
            queryWrapper.eq(StatisticQuarterSum.MERCHANT_USER_ID,userId);
        }
        queryWrapper.orderByAsc(StatisticQuarterSum.QUARTER);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteQuarterSumBy(Long merchantId, Integer quarter, StatisticType type) {
        baseMapper.delete(new QueryWrapper<StatisticQuarterSum>().eq(StatisticQuarterSum.MERCHANT_ID,merchantId).eq(StatisticQuarterSum.QUARTER,quarter).eq(StatisticQuarterSum.TYPE,type));
    }
}
