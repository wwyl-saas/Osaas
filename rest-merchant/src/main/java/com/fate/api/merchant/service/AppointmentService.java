package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.api.merchant.cons.PublicTemplate;
import com.fate.api.merchant.dto.*;
import com.fate.api.merchant.event.PublicMsgEvent;
import com.fate.api.merchant.query.AppointmentChangeQuery;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.dao.CustomerAppointmentDao;
import com.fate.common.entity.*;
import com.fate.common.enums.AppointmentStatus;
import com.fate.common.enums.MessageTag;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.enums.UserRoleType;
import com.fate.common.model.MqProperties;
import com.fate.common.model.MyMessage;
import com.fate.common.model.StandardResponse;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentMerchantUtil;
import com.fate.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 预约相关
 * @author: chenyixin
 * @create: 2019-07-21 11:21
 **/
@Service
@Slf4j
public class AppointmentService {
    @Resource
    CustomerAppointmentDao customerAppointmentDao;
    @Autowired
    MerchantUserService merchantUserService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    CategoryService categoryService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    MqSendService mqSendService;


    /**
     * 根据日期、门店和用户获取预约列表
     * @param shopId
     * @param arriveDate
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public AppointmentListDto getAppointmentList(Long shopId, LocalDate arriveDate, AppointmentStatus appointmentStatus,LocalDate createDateStart,LocalDate createDateEnd,String appointmentPhone,Integer pageIndex, Integer pageSize) {
        AppointmentListDto result= AppointmentListDto.builder().waitingConfirm(0).confirmed(0).canceled(0).build();
        Long userId=null;
        UserRoleType roleType=CurrentUserUtil.getMerchantUserRoleType(shopId);
        //查门店所有人员记录
        if (!roleType.equals(UserRoleType.SHOP_OWNER) && !roleType.equals(UserRoleType.MANAGER_ASSISTANT) && !roleType.equals(UserRoleType.MANAGER)){
            userId=CurrentUserUtil.getMerchantUser().getId();
        }
        List<AppointmentDto> appointmentDtos=Lists.emptyList();
        Integer total=customerAppointmentDao.getCountByShopAndUserIdAndStatusAndPhoneAndCreateDate(shopId,userId,arriveDate,null,createDateStart,createDateEnd,appointmentPhone);
        result.setTotal(total);
        if (total!=0){
            Integer waitingConfirm=customerAppointmentDao.getCountByShopAndUserIdAndStatusAndPhoneAndCreateDate(shopId,userId,arriveDate,AppointmentStatus.WAITING_CONFIRM,createDateStart,createDateEnd,appointmentPhone);
            result.setWaitingConfirm(waitingConfirm);
            Integer confirmed=customerAppointmentDao.getCountByShopAndUserIdAndStatusAndPhoneAndCreateDate(shopId,userId,arriveDate,AppointmentStatus.CONFIRMED,createDateStart,createDateEnd,appointmentPhone);
            result.setConfirmed(confirmed);
            Integer canceled=customerAppointmentDao.getCountByShopAndUserIdAndStatusAndPhoneAndCreateDate(shopId,userId,arriveDate,AppointmentStatus.CANCELED,createDateStart,createDateEnd,appointmentPhone);
            result.setCanceled(canceled);

            IPage<CustomerAppointment> page=customerAppointmentDao.getByShopAndUserIdAndStatusAndPhoneAndCreateDatePage(shopId,userId,arriveDate,appointmentStatus,createDateStart,createDateEnd,appointmentPhone,pageIndex,pageSize);
            if (page!=null && CollectionUtils.isNotEmpty(page.getRecords())){
                List<CustomerAppointment> appointments=page.getRecords();
                Map<Long, MerchantUser> merchantUserMap;
                if (userId==null){
                    List<Long> merchantUserIds = appointments.stream().filter(customerAppointment -> customerAppointment.getMerchantUserId()!=null).map(customerAppointment -> customerAppointment.getMerchantUserId()).collect(Collectors.toList());
                    merchantUserMap = merchantUserService.getMerchantUserByIds(merchantUserIds).stream().collect(Collectors.toMap(MerchantUser::getId, user -> user,(user1, user2)->user1));
                }else{
                    merchantUserMap=new HashMap<>();
                    merchantUserMap.put(userId,CurrentUserUtil.getMerchantUser());
                }

                appointmentDtos=appointments.stream().map(appointment-> {
                    AppointmentDto appointmentDto=BeanUtil.mapper(appointment,AppointmentDto.class);
                    appointmentDto.setStatus(appointment.getStatus().getCode());
                    appointmentDto.setStatusName(appointment.getStatus());
                    appointmentDto.setArriveTimeStart(appointment.getArriveTime().plusMinutes(-15));
                    appointmentDto.setArriveTimeEnd(appointment.getArriveTime().plusMinutes(15));
                    if (StringUtils.isBlank(appointmentDto.getGoodsName())){
                        appointmentDto.setGoodsName("到店指定");
                    }
                    appointmentDto.setMerchantUserName("到店安排");
                    MerchantUser user = merchantUserMap.get(appointment.getMerchantUserId());
                    if (user!=null){
                        appointmentDto.setMerchantUserName(user.getName());
                    }
                    return appointmentDto;
                }).collect(Collectors.toList());
            }
            PageDto<AppointmentDto>  pageDto=new PageDto<>(pageIndex,pageSize,page.getTotal(),page.getPages(),appointmentDtos);
            result.setPageList(pageDto);
        }else {
            PageDto<AppointmentDto>  pageDto=new PageDto<>(pageIndex,pageSize,0,0,appointmentDtos);
            result.setPageList(pageDto);
        }
        return result;
    }



    /**
     * 预约取消
     * @param appointmentId
     */
    public void cancel(Long appointmentId){
        Assert.notNull(appointmentId, ResponseInfo.PARAM_NULL.getMsg());
        CustomerAppointment customerAppointment = customerAppointmentDao.getById(appointmentId);
        Assert.notNull(customerAppointment,"取消操作对应预约不存在");
        if (!customerAppointment.getStatus().equals(AppointmentStatus.CANCELED)){
            customerAppointment.setStatus(AppointmentStatus.CANCELED);
            customerAppointment.setMerchantId(null);
            Assert.isTrue(customerAppointmentDao.updateById(customerAppointment),"数据更新失败");
            // 通知客户取消信息
            MyMessage myMessage=new MyMessage(appointmentId);
            MqProperties mqProperties=MqProperties.builder().merchantId(CurrentMerchantUtil.getMerchant().getId()).messageTag(MessageTag.MERCHANT_APPOINTMENT_CANCEL.getTag()).build();
            mqSendService.sendToCustomer(myMessage,mqProperties);
        }
    }

    /**
     * 确认预约
     * @param appointmentId
     */
    public void confirm(Long appointmentId) {
        Assert.notNull(appointmentId, ResponseInfo.PARAM_NULL.getMsg());
        CustomerAppointment customerAppointment = customerAppointmentDao.getById(appointmentId);
        Assert.notNull(customerAppointment,"取消操作对应预约不存在");
        if (customerAppointment.getStatus().equals(AppointmentStatus.WAITING_CONFIRM)){
            customerAppointment.setStatus(AppointmentStatus.CONFIRMED);
            customerAppointment.setMerchantId(null);
            Assert.isTrue(customerAppointmentDao.updateById(customerAppointment),"数据更新失败");
            // 通知客户确认
            MyMessage myMessage=new MyMessage(appointmentId);
            MqProperties mqProperties=MqProperties.builder().merchantId(CurrentMerchantUtil.getMerchant().getId()).messageTag(MessageTag.APPOINTMENT_CONFIRM.getTag()).build();
            mqSendService.sendToCustomer(myMessage,mqProperties);
        }
    }

    /**
     * 改约
     * @param query
     * @return
     */
    public AppointmentDto changeAppointment(AppointmentChangeQuery query) {
        CustomerAppointment customerAppointment = customerAppointmentDao.getById(query.getAppointmentId());
        Assert.notNull(customerAppointment,"取消操作对应预约不存在");
        customerAppointment.setAppointName(query.getCustomerName());
        customerAppointment.setAppointMobile(query.getPhone());
        customerAppointment.setGoodsId(query.getGoodsId());
        customerAppointment.setGoodsName(query.getGoodsName());
        customerAppointment.setArriveDate(query.getArriveDate());
        customerAppointment.setArriveTime(query.getArriveTime());
        customerAppointment.setMerchantUserId(query.getMerchantUserId());
        customerAppointment.setStatus(AppointmentStatus.CONFIRMED);
        customerAppointment.setUpdateTime(null);
        Assert.isTrue(customerAppointmentDao.updateById(customerAppointment),"数据更新失败");

        AppointmentDto result=BeanUtil.mapper(customerAppointment,AppointmentDto.class);
        result.setStatus(customerAppointment.getStatus().getCode());
        result.setArriveTimeStart(customerAppointment.getArriveTime().plusMinutes(-15));
        result.setArriveTimeEnd(customerAppointment.getArriveTime().plusMinutes(15));
        if (StringUtils.isBlank(result.getGoodsName())){
            result.setGoodsName("到店指定");
        }
        result.setMerchantUserName("到店安排");

        //通知客户改约
        MyMessage myMessage=new MyMessage(customerAppointment.getId());
        MqProperties mqProperties=MqProperties.builder().merchantId(CurrentMerchantUtil.getMerchant().getId()).messageTag(MessageTag.APPOINTMENT_CHANGE.getTag()).build();
        mqSendService.sendToCustomer(myMessage,mqProperties);
        return result;
    }

    /**
     * 获取单个预约信息
     * @param appointmentId
     * @return
     */
    public AppointmentDto getAppointment(Long appointmentId) {
        CustomerAppointment appointment = customerAppointmentDao.getById(appointmentId);
        Assert.notNull(appointment,"预约不存在");
        AppointmentDto appointmentDto=BeanUtil.mapper(appointment,AppointmentDto.class);
        return appointmentDto;
    }

    /**
     * 获取门店技师列表
     *
     * @param shopId
     * @param goodsId
     * @return
     */
    public List<AppointmentSelectDto> getHairdresserList(Long shopId, Long goodsId) {
        List<AppointmentSelectDto> result= Lists.emptyList();
        List<MerchantUser> merchantUsers = merchantUserService.getByShopId(shopId);
        if (CollectionUtils.isNotEmpty(merchantUsers)) {
            if (goodsId!=null){
                List<Long> goodsUserIds = merchantUserService.getMerchantUserIdsByGoodsId(goodsId);
                result=merchantUsers.stream().filter(merchantUser -> merchantUser.getIfAppointment().booleanValue()).map(merchantUser -> {
                    AppointmentSelectDto dto = BeanUtil.mapper(merchantUser,AppointmentSelectDto.class);
                    if (CollectionUtils.isNotEmpty(goodsUserIds)) {
                        dto.setMatch(goodsUserIds.contains(merchantUser.getId()));
                    }else {
                        dto.setMatch(false);
                    }
                    return dto;
                }).filter(appointmentSelectDto -> appointmentSelectDto.getMatch()).collect(Collectors.toList());
            }else{
                result=merchantUsers.stream().filter(merchantUser -> merchantUser.getIfAppointment().booleanValue())
                        .map(merchantUser -> BeanUtil.mapper(merchantUser,AppointmentSelectDto.class)).collect(Collectors.toList());
            }
        }
        return result;
    }

    /**
     * 获取服务项目列表
     * @param shopId
     * @param merchantUserId
     * @return
     */
    public List<AppointmentSelectDto> getServiceList(Long shopId, Long merchantUserId) {
        List<AppointmentSelectDto> result=Lists.emptyList();
        List<Goods> goodes=goodsService.getGoodsByShopId(shopId);
        if (CollectionUtils.isNotEmpty(goodes)){
            if (merchantUserId!=null){
                List<Long> goodsIds = goodsService.getGoodsIdByMerchantUserId(merchantUserId);
                result=goodes.stream().map(goods ->{
                    AppointmentSelectDto dto=BeanUtil.mapper(goods,AppointmentSelectDto.class);
                    if (CollectionUtils.isNotEmpty(goodsIds)){
                        dto.setMatch(goodsIds.contains(goods.getId()));
                    }else{
                        dto.setMatch(false);
                    }
                    return dto;
                }).filter(appointmentSelectDto -> appointmentSelectDto.getMatch()).collect(Collectors.toList());
            }else{
                result=goodes.stream().map(goods ->BeanUtil.mapper(goods,AppointmentSelectDto.class)).collect(Collectors.toList());
            }
        }
        return result;
    }

    /**
     * 获取预先信息（1.arriveDate 状态分类数据 2.arriveDate前后若干天内的某种状态的数据）
     * @param shopId
     * @param arriveDate
     * @param createDateStart
     * @param createDateEnd
     * @param appointmentPhone
     * @return
     */
    public List<String> getAppointmentListPrefilter(Long shopId, LocalDate arriveDate, LocalDate createDateStart, LocalDate createDateEnd, String appointmentPhone) {
        AppointmentListDto result= AppointmentListDto.builder().build();
        Long userId=null;
        UserRoleType roleType=CurrentUserUtil.getMerchantUserRoleType(shopId);
        //查门店所有人员记录
        if (!roleType.equals(UserRoleType.SHOP_OWNER) && !roleType.equals(UserRoleType.MANAGER_ASSISTANT) && !roleType.equals(UserRoleType.MANAGER)){
            userId=CurrentUserUtil.getMerchantUser().getId();
        }
        //前后20天吧
        List<String> dates=customerAppointmentDao.getAppointmentDates(shopId,userId,arriveDate,createDateStart,createDateEnd,appointmentPhone,20);
        return dates;
    }


    /**
     * 获取未确认的预约
     * @param shopId
     * @return
     */
    public Integer getWaitingAppointmentCount(Long shopId) {
        Long userId=null;
        UserRoleType roleType=CurrentUserUtil.getMerchantUserRoleType(shopId);
        //查门店所有人员记录
        if (!roleType.equals(UserRoleType.SHOP_OWNER) && !roleType.equals(UserRoleType.MANAGER_ASSISTANT) && !roleType.equals(UserRoleType.MANAGER)){
            userId=CurrentUserUtil.getMerchantUser().getId();
        }
        Integer number=customerAppointmentDao.getAppointmentCount(shopId,userId,AppointmentStatus.WAITING_CONFIRM);
        return number;
    }

    public  PageDto<AppointmentDto> getAppointmentByDateRangeAndMerchantUserId(Long shopId, Long merchantUserId,Integer appointStatus, LocalDate startDate, LocalDate endDate,Integer pageIndex,Integer  pageSize){
        IPage<CustomerAppointment> page=customerAppointmentDao.getAppointmentByDateRangeAndMerchantUserId(shopId,merchantUserId,appointStatus,startDate,endDate,pageIndex,pageSize);
        List<AppointmentDto> result=Lists.emptyList();
        if (page!=null && CollectionUtils.isNotEmpty(page.getRecords())) {
            List<CustomerAppointment> appointments = page.getRecords();
            Map<Long, MerchantUser> merchantUserMap;
            if (merchantUserId == null) {
                List<Long> merchantUserIds = appointments.stream().filter(customerAppointment -> customerAppointment.getMerchantUserId() != null).map(customerAppointment -> customerAppointment.getMerchantUserId()).collect(Collectors.toList());

                merchantUserMap = merchantUserService.getMerchantUserByIds(merchantUserIds).stream().collect(Collectors.toMap(MerchantUser::getId, user -> user, (user1, user2) -> user1));
            } else {
                merchantUserMap = new HashMap<>();
                MerchantUser merchantUser=merchantUserService.getById(merchantUserId);
                merchantUserMap.put(merchantUserId,merchantUser);
            }

            result = appointments.stream().map(appointment -> {
                AppointmentDto appointmentDto = BeanUtil.mapper(appointment, AppointmentDto.class);
                appointmentDto.setStatus(appointment.getStatus().getCode());
                appointmentDto.setStatusName(appointment.getStatus());
                appointmentDto.setArriveTimeStart(appointment.getArriveTime().plusMinutes(-15));
                appointmentDto.setArriveTimeEnd(appointment.getArriveTime().plusMinutes(15));
                if (StringUtils.isBlank(appointmentDto.getGoodsName())) {
                    appointmentDto.setGoodsName("到店指定");
                }
                appointmentDto.setMerchantUserName("到店安排");
                MerchantUser user = merchantUserMap.get(appointment.getMerchantUserId());
                if (user != null) {
                    appointmentDto.setMerchantUserName(user.getName());
                }
                return appointmentDto;
            }).collect(Collectors.toList());
            return new PageDto<>(pageIndex, pageSize, page.getTotal(), page.getPages(), result);

        }
        return new PageDto<>(pageIndex,pageSize,0,0,result);



    }

    /**
     * 获取今日预约数量（预约到达时间）
     * @param shopId
     * @return
     */
    public Integer getTodayAppointmentCount(Long shopId) {
       return customerAppointmentDao.getAppointmentCountByShopAndDate(shopId,LocalDate.now(),null);
    }

    public Integer getTodayAppointmentCount(Long shopId, Long userId) {
        return customerAppointmentDao.getAppointmentCountByShopAndDate(shopId,LocalDate.now(),userId);
    }

    /**
     * 根据参数条件获取分页
     * @param shopId
     * @param userId
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public IPage<CustomerAppointment> getPageBy(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize) {
        return customerAppointmentDao.getByShopAndUserIdAndDatePage(shopId,userId,date,pageIndex,pageSize);
    }

}
