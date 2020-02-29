package com.fate.api.customer.service;

import com.fate.api.customer.dto.*;
import com.fate.api.customer.handler.EventHandler;
import com.fate.api.customer.handler.HandlerFactory;
import com.fate.api.customer.query.AppointmentCreateQuery;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.dao.CustomerAppointmentDao;
import com.fate.common.entity.*;
import com.fate.common.enums.AppointmentStatus;
import com.fate.common.enums.MessageTag;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.model.MqProperties;
import com.fate.common.model.MyMessage;
import com.fate.common.model.StandardResponse;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: rest-customer
 * @description: 预约相关
 * @author: xudongdong
 * @create: 2019-05-26 12:54
 **/
@Service
@Slf4j
public class AppointmentService {
    private static final String DEFAULT="designer.jpg";
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Resource
    CustomerAppointmentDao customerAppointmentDao;
    @Autowired
    MerchantUserService merchantUserService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    MerchantService merchantService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    PushMessageService pushMessageService;
    @Autowired
    HandlerFactory handlerFactory;
    @Autowired
    CategoryService categoryService;
    @Autowired
    MqSendService mqSendService;

    /**
     * 获取预约列表
     * @return
     */
    public List<AppointmentDto> getMyAppointments(){
        List<AppointmentDto> result=Lists.emptyList();
        Customer customer= CurrentCustomerUtil.getCustomer();
        List<CustomerAppointment> appointments=customerAppointmentDao.getAppointmentByCustomerId(customer.getId());
        if (CollectionUtils.isNotEmpty(appointments)){
            List<Long> merchantUserIds = appointments.stream().filter(customerAppointment -> customerAppointment.getMerchantUserId()!=null).map(customerAppointment -> customerAppointment.getMerchantUserId()).collect(Collectors.toList());
            Map<Long, MerchantUser> merchantUserMap = merchantUserService.getMerchantUserByIds(merchantUserIds).stream().collect(Collectors.toMap(MerchantUser::getId,merchantUser -> merchantUser,(user1,user2)->user1));
            List<Long> shopIds=appointments.stream().map(CustomerAppointment::getShopId).collect(Collectors.toList());
            Map<Long,MerchantShop> shopMap=merchantService.getShopsByShopIds(shopIds).stream().collect(Collectors.toMap(MerchantShop::getId,merchantShop -> merchantShop,(s1,s2)->s1));
            result=appointments.stream().map(customerAppointment -> {
                AppointmentDto dto= BeanUtil.mapper(customerAppointment,AppointmentDto.class);
                MerchantShop shop=shopMap.get(customerAppointment.getShopId());
                if (shop!=null){
                    dto.setShopPhone(shop.getTelephone());
                }
                dto.setArriveTimeStart(customerAppointment.getArriveTime().plusMinutes(-15));
                dto.setArriveTimeEnd(customerAppointment.getArriveTime().plusMinutes(15));
                dto.setMerchantUserName("到店安排");
                MerchantUser merchantUser = merchantUserMap.get(customerAppointment.getMerchantUserId());
                if (merchantUser!=null){
                    dto.setMerchantUserName(merchantUser.getName());
                }
                if (StringUtils.isBlank(dto.getGoodsName())){
                    dto.setGoodsName("到店指定");
                }
                return dto;
            }).collect(Collectors.toList());
        }
        return result;
    }



    /**
     * 取消预约
     * @param appointmentId
     */
    public void cancelMyAppointments(Long appointmentId) {
        Assert.notNull(appointmentId, ResponseInfo.PARAM_NULL.getMsg());
        CustomerAppointment customerAppointment = customerAppointmentDao.getById(appointmentId);
        Assert.notNull(customerAppointment,"取消操作对应预约不存在");
        if (!customerAppointment.getStatus().equals(AppointmentStatus.CANCELED)){
            customerAppointment.setStatus(AppointmentStatus.CANCELED);
            customerAppointment.setMerchantId(null);
            Assert.isTrue(customerAppointmentDao.updateById(customerAppointment),"数据更新失败");
            //通知商户预约取消
            MyMessage myMessage=new MyMessage(appointmentId);
            MqProperties mqProperties=MqProperties.builder().merchantId(CurrentMerchantUtil.getMerchant().getId()).messageTag(MessageTag.CUSTOMER_APPOINTMENT_CANCEL.getTag()).build();
            mqSendService.sendToMerchant(myMessage,mqProperties);
        }
    }

    /**
     * 创建预约信息
     * @param appointmentCreateQuery
     */
    public void createAppointment(AppointmentCreateQuery appointmentCreateQuery) {
        MerchantApplication application= CurrentApplicationUtil.getMerchantApplication();
        Customer customer=CurrentCustomerUtil.getCustomer();
        CustomerAppointment customerAppointment=new CustomerAppointment();
        customerAppointment.setCustomerId(customer.getId())
                .setAppointMobile(appointmentCreateQuery.getPhone())
                .setAppointName(appointmentCreateQuery.getCustomerName())
                .setAppointAvatar(customer.getAvatar())
                .setArriveDate(appointmentCreateQuery.getArriveDate())
                .setArriveTime(appointmentCreateQuery.getArriveTime())
                .setGoodsId(appointmentCreateQuery.getGoodsId())
                .setGoodsName(appointmentCreateQuery.getGoodsName())
                .setMerchantAppId(application.getId())
                .setMerchantUserId(appointmentCreateQuery.getMerchantUserId())
                .setShopId(appointmentCreateQuery.getShopId())
                .setShopName(appointmentCreateQuery.getShopName())
                .setStatus(AppointmentStatus.WAITING_CONFIRM);
        Assert.isTrue(customerAppointment.insert(),"数据插入失败");
        //通知商户新的预约
        MyMessage myMessage=new MyMessage(customerAppointment.getId());
        MqProperties mqProperties=MqProperties.builder().merchantId(CurrentMerchantUtil.getMerchant().getId()).messageTag(MessageTag.APPOINTMENT_CREATE.getTag()).build();
        mqSendService.sendToMerchant(myMessage,mqProperties);
        //记录小程序的formid，以便需要的时候推送消息
        pushMessageService.createCustomerAppointmentPushMessage(customerAppointment,appointmentCreateQuery.getFormId());
    }

    /**
     * 获取技师列表
     *
     * @param shopId
     * @param goodsId
     * @return
     */
    public List<AppointmentHairdresserDto> getHairdresserList(Long shopId, Long goodsId) {
        List<AppointmentHairdresserDto> result= Lists.emptyList();
        List<MerchantUser> merchantUsers = merchantUserService.getByShopId(shopId);
        Map<Long, String> titleMap = merchantUserService.getPostTitleNameMap();
        if (CollectionUtils.isNotEmpty(merchantUsers)) {
            final List<Long> goodsUserIds = merchantUserService.getMerchantUserIdsByGoodsId(goodsId);
            result=merchantUsers.stream().filter(merchantUser -> merchantUser.getIfAppointment().booleanValue()).map(merchantUser -> {
                if(StringUtils.isNotBlank(merchantUser.getProfileUrl())){
                    merchantUser.setProfileUrl(cdnDomain+merchantUser.getProfileUrl());
                }else{
                    merchantUser.setProfileUrl(cdnDomain+DEFAULT);
                }
                AppointmentHairdresserDto dto = AppointmentHairdresserDto.builder()
                        .merchantUserId(merchantUser.getId())
                        .name(merchantUser.getName())
                        .title(titleMap.get(merchantUser.getPostTitleId()))
                        .introduce(merchantUser.getUserBrief())
                        .favorableRate(merchantUser.getGrade())
                        .portrait(merchantUser.getProfileUrl())
                        .match(true)
                        .order(merchantUser.getGrade())
                        .build();
                if (goodsId != null) {
                    dto.setMatch(goodsUserIds.contains(merchantUser.getId()));
                    dto.setOrder(dto.getMatch()?dto.getOrder()+100:dto.getOrder());
                }
                return dto;
            }).sorted(Comparator.comparing(AppointmentHairdresserDto::getOrder).reversed()).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取服务项目列表
     * @param shopId
     * @param merchantUserId
     * @return
     */
    public List<AppointmentServiceDto> getServiceList(Long shopId, Long merchantUserId) {
        List<AppointmentServiceDto> result=new ArrayList<>();
        final List<Long> goodsUserIds = goodsService.getGoodsIdByMerchantUserId(merchantUserId);
        //全部加分类
       
        List<Goods> goodes=goodsService.getGoodsByShopId(shopId);
        List<GoodsDto> goodsDtoList = null;
        if (CollectionUtils.isNotEmpty(goodes)){
           goodsDtoList=goodes.stream().map(goods ->{
                GoodsDto dto=GoodsDto.builder()
                        .id(goods.getId())
                        .categoryId(goods.getCategoryId())
                        .name(goods.getName())
                        .briefPicUrl(cdnDomain+goods.getBriefPicUrl())
                        .goodsBrief(goods.getGoodsBrief())
                        .counterPrice(goods.getCounterPrice())
                        .marketPrice(goods.getMarketPrice())
                        .sellVolume(goods.getSellVolume())
                        .isHot(goods.getIsHot())
                        .isNew(goods.getIsNew())
                        .isRecommend(goods.getIsRecommend())
                        .match(true)
                        .build();
                if (merchantUserId!=null){
                    dto.setMatch(goodsUserIds.contains(goods.getId()));
                }
                dto.setSortOrder(getOrder(dto));
                return dto;
            }).sorted(Comparator.comparing(GoodsDto::getSortOrder).reversed()).collect(Collectors.toList());
        }
        //全部
        AppointmentServiceDto allServiceDto=AppointmentServiceDto.builder().categoryId(0L).categoryName("全部").build();
        allServiceDto.setGoodsList(goodsDtoList);
        result.add(allServiceDto);
        //分类
        List<Category> categories=categoryService.getCategoryList();
        Map<Long,AppointmentServiceDto> serviceDtoMap = null;
        if (CollectionUtils.isNotEmpty(categories)){
            serviceDtoMap=categories.stream().map(category -> AppointmentServiceDto.builder().categoryId(category.getId()).categoryName(category.getName()).goodsList(new ArrayList<>()).build())
                    .collect(Collectors.toMap(AppointmentServiceDto::getCategoryId, appointmentServiceDto -> appointmentServiceDto));
        }
        if (CollectionUtils.isNotEmpty(goodsDtoList) && MapUtils.isNotEmpty(serviceDtoMap)){
            for (GoodsDto goodsDto:goodsDtoList){
                AppointmentServiceDto appointmentServiceDto=serviceDtoMap.get(goodsDto.getCategoryId());
                appointmentServiceDto.getGoodsList().add(goodsDto);
            }
            result.addAll(serviceDtoMap.values());
        }

        return result;
    }


    /**
     * 计算商品排序
     * @param dto
     * @return
     */
    private Integer getOrder(GoodsDto dto){
        int order=0;
        if (dto.getIsHot()){
            order++;
        }
        if (dto.getIsNew()){
            order++;
        }
        if (dto.getIsRecommend()){
            order++;
        }
        if (dto.getMatch()){
            order++;
        }
        return order;
    }
}
