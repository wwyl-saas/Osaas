package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.StatisticDao;
import com.fate.common.entity.Statistic;
import com.fate.common.enums.StatisticType;
import com.fate.common.mapper.StatisticMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 商户首页指标表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-08-23
 */
@Repository
public class StatisticDaoImpl extends ServiceImpl<StatisticMapper, Statistic> implements StatisticDao {

    @Override
    public List<Statistic> getStatisticDateList(Long shopId, Long userId, StatisticType dataType, LocalDate startDate, LocalDate endDate) {
        QueryWrapper queryWrapper=new QueryWrapper<Statistic>().eq(Statistic.TYPE,dataType).between(Statistic.DATA_DATE,startDate,endDate);
        if (shopId!=null){
            queryWrapper.eq(Statistic.SHOP_ID,shopId);
        }
        if (userId!=null){
            queryWrapper.eq(Statistic.MERCHANT_USER_ID,userId);
        }
        queryWrapper.orderByAsc(Statistic.DATA_DATE);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public BigDecimal getStatisticSum(Long userId, StatisticType type) {
        return baseMapper.getStatisticSum(userId,type.getCode());
    }

    @Override
    public List<Statistic> getAverageListBy(Long shopId, Integer type, Integer year, Integer quarter, Integer month, Integer week) {
        return baseMapper.getAverageListBy(shopId,type,year,quarter,month,week);
    }

    @Override
    public void deleteBy(Long merchantId, LocalDate date, StatisticType type) {
        baseMapper.delete(new QueryWrapper<Statistic>().eq(Statistic.MERCHANT_ID,merchantId).eq(Statistic.TYPE,type).eq(Statistic.DATA_DATE,date));
    }

    @Override
    public List<Statistic> getStatisticBy(Long merchantId, StatisticType type, Integer week) {
        return baseMapper.selectList(new QueryWrapper<Statistic>().eq(Statistic.MERCHANT_ID,merchantId).eq(Statistic.TYPE,type).eq(Statistic.WEEK,week));
    }

    @Override
    public List<Statistic> getStatisticBy(Long merchantId, StatisticType type, Integer quarter, Integer month, Integer week) {
        QueryWrapper queryWrapper=new QueryWrapper<Statistic>().eq(Statistic.MERCHANT_ID,merchantId).eq(Statistic.TYPE,type);
        if (quarter!=null){
            queryWrapper.eq(Statistic.QUARTER,quarter);
        }else if (month!=null){
            queryWrapper.eq(Statistic.MONTH,month);
        }else{
            queryWrapper.eq(Statistic.WEEK,week);
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Statistic> getSumListBy(Long shopId, Integer type, Integer year, Integer quarter, Integer month, Integer week) {
        return baseMapper.getSumListBy(shopId,type,year,quarter,month,week);
    }
}
