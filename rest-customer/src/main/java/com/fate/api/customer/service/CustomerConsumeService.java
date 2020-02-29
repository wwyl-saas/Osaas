package com.fate.api.customer.service;

import com.fate.api.customer.dto.CustomerBillDto;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.dao.CustomerChargeDao;
import com.fate.common.dao.CustomerConsumeDao;
import com.fate.common.entity.*;
import com.fate.common.enums.*;
import com.fate.common.model.MqProperties;
import com.fate.common.model.MyMessage;
import com.fate.common.util.CurrentMerchantUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
    MqSendService mqSendService;
    @Resource
    CustomerConsumeDao customerConsumeDao;
    @Resource
    CustomerChargeDao customerChargeDao;
    @Autowired
    OrderService orderService;


    /**
     * 创建消费记录
     * @param order
     * @param consumeType
     */
    public void createConsumeLog(Order order, ConsumeType consumeType) {
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
    }


    /**
     * 查询订单列表
     * @param type
     * @return
     */
    public List<CustomerBillDto> getBillList(BillType type) {
        Customer customer=CurrentCustomerUtil.getCustomer();
        List<CustomerBillDto> result= Lists.newArrayList();
        List<CustomerConsume> customerConsumes=null;
        List<CustomerCharge> customerCharges=null;
        switch (type){
            case ALL:
                customerConsumes=customerConsumeDao.selectByCustomerId(customer.getId());
                customerCharges=customerChargeDao.selectByCustomerId(customer.getId());
                break;
            case CONSUME:
                customerConsumes=customerConsumeDao.selectByCustomerId(customer.getId());
                break;
            case RECHARGE:
                customerCharges=customerChargeDao.selectByCustomerId(customer.getId());
                break;
        }
        if (CollectionUtils.isNotEmpty(customerConsumes)){
            customerConsumes.parallelStream().forEach(customerConsume -> {
                CustomerBillDto billDto=CustomerBillDto.builder()
                        .id(customerConsume.getId())
                        .amount(customerConsume.getAmount())
                        .dateTime(customerConsume.getCreateTime())
                        .goodsDesc(customerConsume.getDescription())
                        .settleDesc(customerConsume.getConsumeType().getDesc())
                        .flag(BillType.CONSUME.getCode())
                        .build();
                result.add(billDto);
            });
        }
        if (CollectionUtils.isNotEmpty(customerCharges)){
            customerCharges.parallelStream().forEach(customerCharge -> {
                CustomerBillDto billDto=CustomerBillDto.builder()
                        .id(customerCharge.getId())
                        .amount(customerCharge.getAmount().add(customerCharge.getGiftAmount()))
                        .dateTime(customerCharge.getCreateTime())
                        .goodsDesc("会员充值")
                        .settleDesc(customerCharge.getChargeType().getDesc())
                        .flag(BillType.RECHARGE.getCode())
                        .build();
                result.add(billDto);
            });
        }
        if (CollectionUtils.isNotEmpty(result)){
            result.sort(Comparator.comparing(CustomerBillDto::getDateTime).reversed());
        }
        return result;
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

}
