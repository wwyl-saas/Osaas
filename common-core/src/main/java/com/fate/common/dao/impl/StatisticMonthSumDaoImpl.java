package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.StatisticMonthSumDao;
import com.fate.common.entity.StatisticMonthSum;
import com.fate.common.entity.StatisticWeekSum;
import com.fate.common.enums.StatisticType;
import com.fate.common.mapper.StatisticMonthSumMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
public class StatisticMonthSumDaoImpl extends ServiceImpl<StatisticMonthSumMapper, StatisticMonthSum> implements StatisticMonthSumDao {

    @Override
    public List<StatisticMonthSum> getList(Long shopId, Long userId, StatisticType statisticType, Integer year, Integer startMonth, Integer endMonth) {
        QueryWrapper queryWrapper=new QueryWrapper<StatisticMonthSum>().eq(StatisticWeekSum.TYPE,statisticType).eq(StatisticWeekSum.YEAR, year)
                .between(StatisticMonthSum.MONTH,startMonth,endMonth);
        if (shopId!=null){
            queryWrapper.eq(StatisticWeekSum.SHOP_ID,shopId);
        }
        if (userId!=null){
            queryWrapper.eq(StatisticWeekSum.MERCHANT_USER_ID,userId);
        }
        queryWrapper.orderByAsc(StatisticMonthSum.MONTH);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteMonthSumBy(Long merchantId, Integer month, StatisticType type) {
        baseMapper.delete(new QueryWrapper<StatisticMonthSum>().eq(StatisticMonthSum.MERCHANT_ID,merchantId).eq(StatisticMonthSum.MONTH,month).eq(StatisticMonthSum.TYPE,type));
    }
}
