package com.fate.api.customer.service;

import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.dao.PushMessageDao;
import com.fate.common.entity.CustomerAppointment;
import com.fate.common.entity.CustomerCharge;
import com.fate.common.entity.Order;
import com.fate.common.entity.PushMessage;
import com.fate.common.enums.BusinessType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @program: parent
 * @description: 延迟消息推送服务
 * @author: chenyixin
 * @create: 2019-06-18 18:10
 **/
@Slf4j
@Service
public class PushMessageService {
    @Resource
    PushMessageDao pushMessageDao;

    /**
     * 创建预约的延迟消息推送对象
     * @param customerAppointment
     * @param formId
     */
    public void createCustomerAppointmentPushMessage(CustomerAppointment customerAppointment,String formId) {
        PushMessage pushMessage=pushMessageDao.getByBusinessIdAndType(customerAppointment.getId(),BusinessType.WX_APPOINTMENT_CREATE);
        if (pushMessage==null){
            pushMessage=new PushMessage()
                    .setMerchantAppId(customerAppointment.getMerchantAppId())
                    .setPushNumber(0)
                    .setBusinessId(customerAppointment.getId())
                    .setBusinessType(BusinessType.WX_APPOINTMENT_CREATE)
                    .setCustomerId(CurrentCustomerUtil.getCustomer().getId())
                    .setFormId(formId);
            Assert.isTrue(pushMessage.insert(),"插入数据失败");
        }else{
            pushMessage.setFormId(formId);
            Assert.isTrue(pushMessage.updateById(),"数据更新失败");
        }

    }

    /**
     * 创建订单支付延迟消息推送对象
     * @param order
     * @param formId
     */
    public void createOrderPayPushMessage(Order order, String formId) {
        PushMessage pushMessage=pushMessageDao.getByBusinessIdAndType(order.getId(),BusinessType.WX_PAY_CREATE);
        if (pushMessage==null){
            pushMessage=new PushMessage()
                    .setMerchantAppId(order.getMerchantAppId())
                    .setPushNumber(0)
                    .setBusinessId(order.getId())
                    .setBusinessType(BusinessType.WX_PAY_CREATE)
                    .setCustomerId(CurrentCustomerUtil.getCustomer().getId())
                    .setFormId(formId);
            Assert.isTrue(pushMessage.insert(),"插入数据失败");
        }else{
            pushMessage.setFormId(formId);
            Assert.isTrue(pushMessage.updateById(),"数据更新失败");
        }
    }

    /**
     * 创建会员支付延迟消息推送对象
     * @param charge
     * @param prepayId
     */
    public void createChargePushMessage(CustomerCharge charge, String prepayId) {
        PushMessage pushMessage=pushMessageDao.getByBusinessIdAndType(charge.getId(),BusinessType.WX_CHARGE_CREATE);
        if (pushMessage==null){
            pushMessage=new PushMessage()
                    .setMerchantAppId(charge.getMerchantAppId())
                    .setPushNumber(0)
                    .setBusinessId(charge.getId())
                    .setBusinessType(BusinessType.WX_CHARGE_CREATE)
                    .setCustomerId(CurrentCustomerUtil.getCustomer().getId())
                    .setFormId(prepayId);
            Assert.isTrue(pushMessage.insert(),"插入数据失败");
        }else{
            pushMessage.setFormId(prepayId);
            Assert.isTrue(pushMessage.updateById(),"数据更新失败");
        }

    }

    public PushMessage getByBusinessIdAndType(Long id, BusinessType type) {
       return pushMessageDao.getByBusinessIdAndType(id,type);
    }
}
