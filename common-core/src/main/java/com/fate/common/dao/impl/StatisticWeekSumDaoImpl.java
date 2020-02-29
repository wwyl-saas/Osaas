package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.StatisticWeekSumDao;
import com.fate.common.entity.StatisticWeekSum;
import com.fate.common.enums.StatisticType;
import com.fate.common.mapper.StatisticWeekSumMapper;
import org.apache.shardingsphere.shardingjdbc.api.yaml.YamlEncryptDataSourceFactory;
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
public class StatisticWeekSumDaoImpl extends ServiceImpl<StatisticWeekSumMapper, StatisticWeekSum> implements StatisticWeekSumDao {

    @Override
    public List<StatisticWeekSum> getList(Long shopId, Long userId, StatisticType statisticType, Integer year,Integer startWeek, Integer endWeek) {
        QueryWrapper queryWrapper=new QueryWrapper<StatisticWeekSum>().eq(StatisticWeekSum.TYPE,statisticType).eq(StatisticWeekSum.YEAR, year)
                .between(StatisticWeekSum.WEEK,startWeek,endWeek);
        if (shopId!=null){
            queryWrapper.eq(StatisticWeekSum.SHOP_ID,shopId);
        }
        if (userId!=null){
            queryWrapper.eq(StatisticWeekSum.MERCHANT_USER_ID,userId);
        }
        queryWrapper.orderByAsc(StatisticWeekSum.WEEK);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteWeekSumBy(Long merchantId, Integer week, StatisticType type) {
        baseMapper.delete(new QueryWrapper<StatisticWeekSum>().eq(StatisticWeekSum.MERCHANT_ID,merchantId).eq(StatisticWeekSum.WEEK,week).eq(StatisticWeekSum.TYPE,type));
    }
}
