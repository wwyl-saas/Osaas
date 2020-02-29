package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CustomerAppointmentDao;
import com.fate.common.entity.CustomerAccount;
import com.fate.common.entity.CustomerAppointment;
import com.fate.common.enums.AppointmentStatus;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.mapper.CustomerAppointmentMapper;
import com.fate.common.model.StatisticModel;
import com.fate.common.util.DateUtil;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * C端用户预约表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class CustomerAppointmentDaoImpl extends ServiceImpl<CustomerAppointmentMapper, CustomerAppointment> implements CustomerAppointmentDao {

    @Override
    public List<CustomerAppointment> getAppointmentByCustomerId(Long customerId) {
        Assert.notNull(customerId, ResponseInfo.PARAM_NULL.getMsg());
        QueryWrapper<CustomerAppointment> queryWrapper = new QueryWrapper<CustomerAppointment>().eq(CustomerAppointment.CUSTOMER_ID,customerId).ne(CustomerAppointment.STATUS, AppointmentStatus.CANCELED).orderByDesc(CustomerAppointment.CREATE_TIME);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<CustomerAppointment> getByShopAndUserIdAndDate(Long shopId, Long userId, LocalDate date) {
        QueryWrapper<CustomerAppointment> queryWrapper = new QueryWrapper<CustomerAppointment>();
        if (shopId!=null){
            queryWrapper.eq(CustomerAppointment.SHOP_ID,shopId);
        }
        if (userId!=null){
            queryWrapper.eq(CustomerAppointment.MERCHANT_USER_ID,userId);
        }
        if (date!=null){
            queryWrapper.eq(CustomerAppointment.ARRIVE_DATE,date);
        }
        queryWrapper.ne(CustomerAppointment.STATUS, AppointmentStatus.CANCELED);
        queryWrapper.orderByDesc(CustomerAppointment.CREATE_TIME);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<CustomerAppointment> getByShopAndUserIdAndDatePage(Long shopId, Long userId,LocalDate date, Integer pageIndex, Integer pageSize) {
        QueryWrapper<CustomerAppointment> queryWrapper = new QueryWrapper<CustomerAppointment>();
        if (shopId!=null){
            queryWrapper.eq(CustomerAppointment.SHOP_ID,shopId);
        }
        if (userId!=null){
            queryWrapper.eq(CustomerAppointment.MERCHANT_USER_ID,userId);
        }
        if (date!=null){
            queryWrapper.eq(CustomerAppointment.ARRIVE_DATE,date);
        }
        queryWrapper.ne(CustomerAppointment.STATUS, AppointmentStatus.CANCELED);
        queryWrapper.orderByDesc(CustomerAppointment.CREATE_TIME);
        return baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);
    }

    @Override
    public Integer getCountByShopAndUserIdAndDate(Long shopId, Long userId, LocalDate date) {
        QueryWrapper<CustomerAppointment> queryWrapper = new QueryWrapper<CustomerAppointment>();
        Assert.notNull(shopId,ResponseInfo.PARAM_NULL.getMsg());
        queryWrapper.eq(CustomerAppointment.SHOP_ID,shopId);
        if (userId!=null){
            queryWrapper.eq(CustomerAppointment.MERCHANT_USER_ID,userId);
        }
        if (date!=null){
            queryWrapper.eq(CustomerAppointment.CREATE_TIME,date);
        }
        queryWrapper.ne(CustomerAppointment.STATUS, AppointmentStatus.CANCELED);
        queryWrapper.orderByDesc(CustomerAppointment.CREATE_TIME);
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public IPage<CustomerAppointment> getByShopAndUserIdAndStatusAndPhoneAndCreateDatePage(Long shopId, Long userId, LocalDate arriveDate, AppointmentStatus appointmentStatus, LocalDate createDateStart, LocalDate createDateEnd, String appointmentPhone, Integer pageIndex, Integer pageSize) {
        Assert.notNull(arriveDate,"预约到店时间未指定");
        QueryWrapper<CustomerAppointment> queryWrapper = new QueryWrapper<CustomerAppointment>();
        if (shopId!=null){
            queryWrapper.eq(CustomerAppointment.SHOP_ID,shopId);
        }if (userId!=null){
            queryWrapper.eq(CustomerAppointment.MERCHANT_USER_ID,userId);
        }if (arriveDate!=null){
            queryWrapper.eq(CustomerAppointment.ARRIVE_DATE,arriveDate);
        }if (appointmentStatus!=null){
            queryWrapper.eq(CustomerAppointment.STATUS,appointmentStatus);
        }if (createDateStart!=null && createDateEnd!=null){
            Assert.isTrue(DateUtil.getBetweenDay(createDateStart,createDateEnd)<32,"查询创建时间间隔须小于一个月");
            queryWrapper.between(CustomerAppointment.CREATE_TIME,createDateStart,createDateEnd);
        }if (appointmentPhone!=null){
            queryWrapper.eq(CustomerAppointment.APPOINT_MOBILE,appointmentPhone);
        }
        queryWrapper.orderByDesc(CustomerAppointment.CREATE_TIME);
        return baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);
    }

    @Override
    public Integer getCountByShopAndUserIdAndStatusAndPhoneAndCreateDate(Long shopId, Long userId, LocalDate arriveDate, AppointmentStatus status, LocalDate createDateStart, LocalDate createDateEnd, String appointmentPhone) {
        Assert.notNull(arriveDate,"预约到店时间未指定");
        QueryWrapper<CustomerAppointment> queryWrapper = new QueryWrapper<CustomerAppointment>();
        if (shopId!=null){
            queryWrapper.eq(CustomerAppointment.SHOP_ID,shopId);
        }if (userId!=null){
            queryWrapper.eq(CustomerAppointment.MERCHANT_USER_ID,userId);
        }if (arriveDate!=null) {
            queryWrapper.eq(CustomerAppointment.ARRIVE_DATE, arriveDate);
        }if (status!=null){
            queryWrapper.eq(CustomerAppointment.STATUS,status);
        }
        if (createDateStart!=null && createDateEnd!=null){
            Assert.isTrue(DateUtil.getBetweenDay(createDateStart,createDateEnd)<32,"查询创建时间间隔须小于一个月");
            queryWrapper.between(CustomerAppointment.CREATE_TIME,createDateStart,createDateEnd);
        }if (appointmentPhone!=null){
            queryWrapper.eq(CustomerAppointment.APPOINT_MOBILE,appointmentPhone);
        }
        queryWrapper.orderByDesc(CustomerAppointment.CREATE_TIME);
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public List<String> getAppointmentDates(Long shopId, Long userId, LocalDate arriveDate, LocalDate createDateStart, LocalDate createDateEnd, String appointmentPhone, int days) {
        Assert.notNull(arriveDate,"预约到店时间未指定");
        LocalDateTime createTimeStart=null;
        LocalDateTime createTimeEnd=null;
        if (createDateStart!=null && createDateEnd!=null){
            createTimeStart=createDateStart.atTime(LocalTime.MIN);
            createTimeEnd=createDateEnd.atTime(23,59,59);
        }
        return baseMapper.getAppointmentDates(shopId,userId,arriveDate.minusDays(days),arriveDate.minusDays(-days),createTimeStart,createTimeEnd,appointmentPhone);
    }

    @Override
    public Integer getAppointmentCount(Long shopId, Long userId, AppointmentStatus status) {
        QueryWrapper<CustomerAppointment> queryWrapper = new QueryWrapper<CustomerAppointment>();
        if (shopId != null) {
            queryWrapper.eq(CustomerAppointment.SHOP_ID, shopId);
        }
        if (userId != null) {
            queryWrapper.eq(CustomerAppointment.MERCHANT_USER_ID, userId);
        }
        if (status != null) {
            queryWrapper.eq(CustomerAppointment.STATUS, status);
        }
        return baseMapper.selectCount(queryWrapper);
    }

    public IPage<CustomerAppointment> getAppointmentByDateRangeAndMerchantUserId(Long shopId, Long merchantUserId, Integer appointStatus,LocalDate startDate, LocalDate endDate, Integer pageIndex, Integer pageSize) {

        QueryWrapper<CustomerAppointment> queryWrapper = new QueryWrapper<CustomerAppointment>();
        if (shopId!=null){
            queryWrapper.eq(CustomerAppointment.SHOP_ID,shopId);
        }
        if (merchantUserId!=null){
            queryWrapper.eq(CustomerAppointment.MERCHANT_USER_ID,merchantUserId);
        }
        if (startDate!=null){
            queryWrapper.ge(CustomerAppointment.ARRIVE_DATE,startDate);
        }
        if (endDate!=null){
            queryWrapper.le(CustomerAppointment.ARRIVE_DATE,endDate);
        }
        if (appointStatus!=null){
            queryWrapper.eq(CustomerAppointment.STATUS,appointStatus);
        }
        queryWrapper.orderByDesc(CustomerAppointment.CREATE_TIME);

        return baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);
    }

    @Override
    public Integer getAppointmentCountByShopAndDate(Long shopId, LocalDate date,Long userId) {
        QueryWrapper queryWrapper= new QueryWrapper<CustomerAppointment>().eq(CustomerAppointment.ARRIVE_DATE,date);
        if (shopId!=null){
            queryWrapper.eq(CustomerAppointment.SHOP_ID,shopId);
        }
        if (userId!=null){
            queryWrapper.eq(CustomerAppointment.MERCHANT_USER_ID,userId);
        }
        queryWrapper.ne(CustomerAppointment.STATUS,AppointmentStatus.CANCELED);
        return baseMapper.selectCount(queryWrapper);
    }


    @Override
    public List<CustomerAppointment> getByArriveDateBefore(LocalDate date) {
        return baseMapper.selectList(new QueryWrapper<CustomerAppointment>().lt(CustomerAppointment.ARRIVE_DATE,date).orderByDesc(CustomerAppointment.CREATE_TIME));
    }

    @Override
    public List<StatisticModel> getAppointmentCount(Long merchantId, LocalDate date) {
        return baseMapper.getAppointmentCount(merchantId,date);
    }
}
