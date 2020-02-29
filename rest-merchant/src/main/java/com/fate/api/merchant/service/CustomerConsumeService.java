package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.api.merchant.dto.*;
import com.fate.common.dao.CustomerConsumeDao;
import com.fate.common.dao.MemberDao;
import com.fate.common.dao.MemberThresholdDao;
import com.fate.common.dao.MemberWelfareDao;
import com.fate.common.entity.*;
import com.fate.common.enums.MemberLevel;
import com.fate.common.enums.MemberThresholdType;
import com.fate.common.enums.MessageTag;
import com.fate.common.enums.ConsumeType;
import com.fate.common.model.MqProperties;
import com.fate.common.model.MyMessage;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 账单操作相关
 * @author: songjignhuan
 * @create: 2019-05-29 15:13
 **/
@Service
@Slf4j
public class CustomerConsumeService {
    @Autowired
    ApplicationContext applicationContext;
    @Resource
    CustomerConsumeDao customerConsumeDao;
    @Autowired
    OrderService orderService;
    @Autowired
    MqSendService mqSendService;

    @Value("${cdn.domain}")
    private String cdnDomain;


    @Resource
    MemberDao memberDao;

    @Resource
    MemberWelfareDao memberWelfareDao;

    @Resource
    MemberThresholdDao memberThresholdDao;


    /**
     * 创建消费记录
     * @param order
     * @param consumeType
     */
    public Long createConsumeLog(Order order, ConsumeType consumeType) {
        CustomerConsume customerConsume =new CustomerConsume();
        customerConsume.setCustomerId(order.getCustomerId());
        customerConsume.setAmount(order.getPayAmount());
        customerConsume.setConsumeType(consumeType);
        customerConsume.setOrderId(order.getId());
        customerConsume.setShopId(order.getShopId());
        customerConsume.setMerchantAppId(order.getMerchantAppId());
        customerConsume.setMerchantUserId(order.getMerchantUserId());

        StringBuilder sb=new StringBuilder().append(order.getGoodsNames()).append("的服务消费");
        customerConsume.setDescription(sb.toString());
        boolean flag= customerConsumeDao.save(customerConsume);
        Assert.isTrue(flag,"数据插入失败");
        sendConsumeSuccess(customerConsume);
        return customerConsume.getId();
    }


    public CustomerConsume selectByOrderId(Long orderId) {
        return customerConsumeDao.selectByOrderId(orderId);
    }

    /**
     * 获得今日实际支付订单金额
     * @param shopId
     * @return
     */
    public BigDecimal getTodayFactConsumeAmount(Long shopId) {
       return customerConsumeDao.getFactConsumeAmount(shopId, LocalDate.now());
    }
    /**
     * 获得今日余额支付订单金额
     * @param shopId
     * @return
     */
    public BigDecimal getTodayStoreConsumeAmount(Long shopId) {
        return customerConsumeDao.getStoreConsumeAmount(shopId, LocalDate.now());
    }

    /**
     * 获得个人今日实际支付订单金额
     * @param shopId
     * @return
     */
    public BigDecimal getTodayFactConsumeAmount(Long shopId, Long userId) {
        return customerConsumeDao.getTodayFactConsumeAmount(shopId,userId,LocalDate.now());
    }


    /**
     * 获得个人今日余额支付订单金额
     * @param shopId
     * @return
     */
    public BigDecimal getTodayStoreConsumeAmount(Long shopId, Long userId) {
        return customerConsumeDao.getTodayStoreConsumeAmount(shopId,userId,LocalDate.now());
    }

    /**
     *
     * @param shopId
     * @param date
     * @param consumeType
     * @return
     */
    public List<Long> getOrderIdsBy(Long shopId, LocalDate date, ConsumeType consumeType) {
        return customerConsumeDao.getOrderIdsBy(shopId,date,consumeType);
    }

    /**
     * 发送消费成功事件
     * @param customerConsume
     */
    private void sendConsumeSuccess(CustomerConsume customerConsume){
        try {
            MyMessage message=new MyMessage(customerConsume.getId());
            MqProperties mqProperties=MqProperties.builder().merchantId(CurrentMerchantUtil.getMerchant().getId()).delay(10).messageTag(MessageTag.CONSUME_SUCCESS.getTag()).build();
            mqSendService.sendToAdmin(message,mqProperties);
        } catch (Exception e) {
            log.error("商户端支付结果消息发送失败，order={}", customerConsume, e);
        }
    }


    /**
     * 获取店铺会员的消费记录
     * @param pageIndex
     * @param pageSize
     * @param shopIds
     * @return
     */
    public PageDto<CustomerConsumeDto> getCustomerConsumeByShopId(Long pageIndex, Long pageSize, String shopIds) {

        List<CustomerConsumeDto> result=new ArrayList<>();
        IPage<CustomerConsume> page;
        if(StringUtils.isEmpty(shopIds) || StringUtils.isBlank(shopIds)){
            page=customerConsumeDao.getPageByMerchant(pageIndex,pageSize);
        }else{
            List<String> shopIdsStr= Arrays.asList(shopIds.split(","));
            List<Long> list=new ArrayList<Long>();
            for(String str:shopIdsStr){
                list.add(Long.valueOf(str));
            }
            page=customerConsumeDao.getPageByShopIds(pageIndex,pageSize,list);
        }

        if (page != null && CollectionUtils.isNotEmpty(page.getRecords())) {
            List<CustomerConsume> customerConsumes = page.getRecords();
            result = customerConsumes.stream().map(customerConsume -> {
                CustomerConsumeDto customerConsumeDto = BeanUtil.mapper(customerConsume, CustomerConsumeDto.class);
                return customerConsumeDto;
            }).collect(Collectors.toList());
            return new PageDto<>(pageIndex, pageSize, page.getTotal(), page.getPages(), result);
        }

        return new PageDto<>(pageIndex, pageSize, 0, 0, result);




    }


    /**
     * 根据页面面选择的商户ID查询会员卡信息
     * @param merchantId
     * @return
     */
    public List<MemberCardDto> getMemberCard(String merchantId) {
        List<Member>  list;
        List<MemberCardDto> result=new ArrayList<>();
        if(StringUtils.isNotEmpty(merchantId)){
            list=memberDao.getMemberCardByMercchantId(merchantId);
        }else{
            list=memberDao.getMemberCard();
        }
        if (list != null && CollectionUtils.isNotEmpty(list)) {
            result = list.stream().map(member -> {
                List<MemberWelfareDto> welfarelList=new ArrayList<MemberWelfareDto>();
                String[] warefareCodes=member.getWelfareAllTypes().split(",");

                MemberCardDto memberCardDto = BeanUtil.mapper(member, MemberCardDto.class);
                if(member.getPictureUrl()!=null){
                    memberCardDto.setPictureUrl(cdnDomain+member.getPictureUrl());
                    memberCardDto.setLevelCode(member.getLevel().getCode());
                    memberCardDto.setLevelDesc(member.getLevel().getDesc());
                }
                int warefareCode=0;
                for(String warefare :warefareCodes){
                    warefareCode=Integer.parseInt(warefare);
                    MemberWelfareDto memberWelfareDto = MemberWelfareDto.builder()
                            .code(warefareCode)
                            .desc(MemberThresholdType.getEnum(warefareCode).get().getDesc())
                            .build();
                    welfarelList.add(memberWelfareDto);
                }
               memberCardDto.setWelfareAllTypesList(welfarelList);
                return memberCardDto;
            }).collect(Collectors.toList());

        }
        return result;
    }


    /**
     * 根据页面面选择的商户ID和会员卡ID查询会员门槛值信息
     * @param merchantId
     * @param memberId
     * @return
     */
    public List<MemberThresholdDto> getMemberThreshold(Long merchantId, Long memberId) {

        List<MemberThresholdDto> result=new ArrayList<>();
        List<MemberThreshold> list=memberThresholdDao.getByMemberIdAndMerchantId(merchantId,memberId);
        if (list != null && CollectionUtils.isNotEmpty(list)) {
            result = list.stream().map(memberThreshold -> {
                MemberThresholdDto memberThresholdDto = BeanUtil.mapper(memberThreshold, MemberThresholdDto.class);
                if(memberThreshold.getThresholdKey()!=null){
                    memberThresholdDto.setCode(memberThreshold.getThresholdKey().getCode());
                    memberThresholdDto.setDesc(memberThreshold.getThresholdKey().getDesc());
                }

                return memberThresholdDto;
            }).collect(Collectors.toList());

        }
        return result;
    }
    /**
     * 根据页面面选择的商户ID和会员卡ID查询会员福利信息
     * @param merchantId
     * @param memberId
     * @return
     */
    public List<MemberWelfareDto> getWelFare(Long merchantId, Long memberId) {
        List<MemberWelfareDto> result=new ArrayList<>();
        List<MemberWelfare> list=memberWelfareDao.getByMemberIdAndMerchantId(merchantId,memberId);
        if (list != null && CollectionUtils.isNotEmpty(list)){
            result = list.stream().map(memberWelfare -> {
                MemberWelfareDto memberWelfareDto = BeanUtil.mapper(memberWelfare, MemberWelfareDto.class);
                if(memberWelfare.getWelfareKey()!=null){
                    memberWelfareDto.setCode(memberWelfare.getWelfareKey().getCode());
                    memberWelfareDto.setDesc(memberWelfare.getWelfareKey().getDesc());
                    if(memberWelfare.getWelfareKey().getIcon()!=null){
                        memberWelfareDto.setIcon(cdnDomain+memberWelfare.getWelfareKey().getIcon());
                    }

                }
                return memberWelfareDto;
            }).collect(Collectors.toList());

        }
        return result;

    }

    /**
     * 根据商户id查询会员种类
     * @param merchantId
     * @return
     */
    public List<MemberCardDto> getMemberIdAndLevel(Long merchantId) {

        List<MemberCardDto> memberCardDtoList=new ArrayList<>();
        List<Map<String,Object>> list=memberDao.getMemberIdAndLevel(merchantId);
        for(Map<String,Object> map:list){
            MemberCardDto memberCardDto=new MemberCardDto();
            memberCardDto.setId(Long.valueOf(map.get("id").toString()));
            memberCardDto.setLevelCode(MemberLevel.getEnum(Integer.valueOf(map.get("level").toString())).get().getCode());
            memberCardDto.setLevelDesc(MemberLevel.getEnum(Integer.valueOf(map.get("level").toString())).get().getDesc());

            memberCardDtoList.add(memberCardDto);

        }
        return memberCardDtoList;

    }
}
