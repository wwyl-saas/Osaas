package com.fate.api.customer.handler.wx.applet;

import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import com.fate.api.customer.cons.WxAppletTemplate;
import com.fate.api.customer.event.WxAppletMsgEvent;
import com.fate.api.customer.handler.EventHandler;
import com.fate.api.customer.service.*;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.entity.*;
import com.fate.common.enums.*;
import com.fate.common.util.DateUtil;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: parent
 * @description:推送客户端消息
 * @author: chenyixin
 * @create: 2019-06-24 22:25
 **/
@Component
@Slf4j
public class WaEventHandler implements EventHandler {
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    PushMessageService pushMessageService;
    @Autowired
    CustomerService customerService;
    @Autowired
    MerchantService merchantService;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    MerchantUserService merchantUserService;

    /**
     * 查询支付成功消息
     *
     * @param order
     */
    @Override
    public void pushQueryPaySuccess(Order order) {
        try {
            PushMessage pushMessage = pushMessageService.getByBusinessIdAndType(order.getId(), BusinessType.WX_PAY_CREATE);
            if (pushMessage != null) {
                MerchantApplication application = CurrentApplicationUtil.getMerchantApplication();
                CustomerApplication customerApplication = CurrentCustomerUtil.getCustomerApplication();
                //小程序通知用户
                WxAppletMsgEvent appletMsg = new WxAppletMsgEvent(this);
                appletMsg.setAppid(application.getAppId());
                appletMsg.setFormId(pushMessage.getFormId());
                appletMsg.setOpenId(customerApplication.getWxOpenid());
                appletMsg.setTemplateId(WxAppletTemplate.PAY_SUCCESS_TEMPLATE_ID);
                List<WxMaTemplateData> templateDatas = new ArrayList<>();
                templateDatas.add(new WxMaTemplateData("keyword1", order.getOrderNo()));
                MerchantShop shop=merchantService.getMerchantShopById(order.getShopId());
                templateDatas.add(new WxMaTemplateData("keyword2", shop!=null?shop.getName():""));
                templateDatas.add(new WxMaTemplateData("keyword3", order.getGoodsNames()));
                templateDatas.add(new WxMaTemplateData("keyword4", order.getPayAmount().toPlainString()));
                templateDatas.add(new WxMaTemplateData("keyword5", order.getDiscountAmount().add(order.getCouponAmount()).toPlainString()));
                templateDatas.add(new WxMaTemplateData("keyword6", order.getPayAmount().setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString()));
                appletMsg.setTemplateData(templateDatas);
                appletMsg.setPage("pages/serviceRecords/index?status=2");
                applicationContext.publishEvent(appletMsg);
            } else {
                log.error("未查询到消息推送记录，order={}", order);
            }
        } catch (Exception e) {
            log.error("查询支付成功消息发送失败，order={}", order, e);
        }

    }

    /**
     * 微信通知支付成功消息
     *
     * @param order
     */
    @Override
    public void pushNotifyPaySuccess(Order order) {
        try {
            PushMessage pushMessage = pushMessageService.getByBusinessIdAndType(order.getId(), BusinessType.WX_PAY_CREATE);
            if (pushMessage != null) {
                MerchantApplication application = CurrentApplicationUtil.getMerchantApplication();
                CustomerApplication customerApplication = customerService.getCustomerApplicationByCustomerIdAndApplicationId(order.getCustomerId(), application.getId());
                //小程序通知用户
                WxAppletMsgEvent appletMsg = new WxAppletMsgEvent(this);
                appletMsg.setAppid(application.getAppId());
                appletMsg.setFormId(pushMessage.getFormId());
                appletMsg.setOpenId(customerApplication.getWxOpenid());
                appletMsg.setTemplateId(WxAppletTemplate.PAY_SUCCESS_TEMPLATE_ID);
                List<WxMaTemplateData> templateDatas = new ArrayList<>();
                templateDatas.add(new WxMaTemplateData("keyword1", order.getOrderNo()));
                MerchantShop shop=merchantService.getMerchantShopById(order.getShopId());
                templateDatas.add(new WxMaTemplateData("keyword2", shop!=null?shop.getName():""));
                templateDatas.add(new WxMaTemplateData("keyword3", order.getGoodsNames()));
                templateDatas.add(new WxMaTemplateData("keyword4", order.getPayAmount().toPlainString()+"元"));
                templateDatas.add(new WxMaTemplateData("keyword5", order.getDiscountAmount().add(order.getCouponAmount()).toPlainString()+"元"));
                templateDatas.add(new WxMaTemplateData("keyword6", order.getPayAmount().setScale(0,BigDecimal.ROUND_HALF_UP).toPlainString()));
                appletMsg.setTemplateData(templateDatas);
                appletMsg.setPage("pages/serviceRecords/index?status=2");
                applicationContext.publishEvent(appletMsg);
            } else {
                log.error("未查询到消息推送记录，order={}", order);
            }
        } catch (Exception e) {
            log.error("微信通知支付成功消息发送失败，order={}", order, e);
        }
    }

    /**
     * 查询会员充值成功
     *
     * @param charge
     */
    @Override
    public void pushQueryChargeSuccess(CustomerCharge charge) {
        try {

            PushMessage pushMessage = pushMessageService.getByBusinessIdAndType(charge.getId(), BusinessType.WX_CHARGE_CREATE);
            if (pushMessage != null) {
                MerchantApplication application = CurrentApplicationUtil.getMerchantApplication();
                CustomerApplication customerApplication = CurrentCustomerUtil.getCustomerApplication();

                //小程序通知用户
                WxAppletMsgEvent appletMsg = new WxAppletMsgEvent(this);
                appletMsg.setAppid(application.getAppId());
                appletMsg.setFormId(pushMessage.getFormId());
                appletMsg.setOpenId(customerApplication.getWxOpenid());
                appletMsg.setTemplateId(WxAppletTemplate.CHARGE_SUCCESS_TEMPLATE_ID);
                List<WxMaTemplateData> templateDatas = new ArrayList<>();
                templateDatas.add(new WxMaTemplateData("keyword1", DateUtil.getLocalDateTimeString(charge.getCreateTime())));
                templateDatas.add(new WxMaTemplateData("keyword2", charge.getAmount().toPlainString()+"元"));
                templateDatas.add(new WxMaTemplateData("keyword3", charge.getGiftAmount().toPlainString()+"元"));
                CustomerAccount customerAccount=customerAccountService.findByCustomerId(charge.getCustomerId());
                templateDatas.add(new WxMaTemplateData("keyword4", customerAccount!=null?customerAccount.getBalance().toPlainString()+"元":""));
                appletMsg.setTemplateData(templateDatas);
                appletMsg.setPage("pages/vipBill/index");
                applicationContext.publishEvent(appletMsg);
            } else {
                log.error("未查询到消息推送记录，charge={}", charge);
            }
        } catch (Exception e) {
            log.error("查询支付成功消息发送失败，charge={}", charge, e);
        }
    }

    /**
     * 微信通知会员充值成功
     *
     * @param charge
     */
    @Override
    public void pushNotifyChargeSuccess(CustomerCharge charge) {
        try {
            PushMessage pushMessage = pushMessageService.getByBusinessIdAndType(charge.getId(), BusinessType.WX_CHARGE_CREATE);
            if (pushMessage != null) {
                MerchantApplication application = CurrentApplicationUtil.getMerchantApplication();
                CustomerApplication customerApplication = customerService.getCustomerApplicationByCustomerIdAndApplicationId(charge.getCustomerId(), application.getId());
                //小程序通知用户
                WxAppletMsgEvent appletMsg = new WxAppletMsgEvent(this);
                appletMsg.setAppid(application.getAppId());
                appletMsg.setFormId(pushMessage.getFormId());
                appletMsg.setOpenId(customerApplication.getWxOpenid());
                appletMsg.setTemplateId(WxAppletTemplate.CHARGE_SUCCESS_TEMPLATE_ID);
                List<WxMaTemplateData> templateDatas = new ArrayList<>();
                templateDatas.add(new WxMaTemplateData("keyword1", DateUtil.getLocalDateTimeString(charge.getCreateTime())));
                templateDatas.add(new WxMaTemplateData("keyword2", charge.getAmount().toPlainString()+"元"));
                templateDatas.add(new WxMaTemplateData("keyword3", charge.getGiftAmount().toPlainString()+"元"));
                CustomerAccount customerAccount=customerAccountService.findByCustomerId(charge.getCustomerId());
                templateDatas.add(new WxMaTemplateData("keyword4", customerAccount!=null?customerAccount.getBalance().toPlainString()+"元":""));
                appletMsg.setTemplateData(templateDatas);
                appletMsg.setPage("pages/vipBill/index");
                applicationContext.publishEvent(appletMsg);
            } else {
                log.error("未查询到消息推送记录，charge={}", charge);
            }
        } catch (Exception e) {
            log.error("支付成功消息发送失败，charge={}", charge, e);
        }
    }


    /**
     * 推送取消消息
     *
     * @param order
     */
    @Override
    public void pushOrderCancel(Order order) throws WxPayException {
        PushMessage pushMessage = pushMessageService.getByBusinessIdAndType(order.getId(), BusinessType.WX_PAY_CREATE);
        if (pushMessage!=null){
            CustomerApplication customerApplication = customerService.getCustomerApplicationByCustomerIdAndApplicationId(order.getCustomerId(), order.getMerchantAppId());
            MerchantApplication application = CurrentApplicationUtil.getMerchantApplication();
            WxAppletMsgEvent appletMsg = new WxAppletMsgEvent(this);
            appletMsg.setAppid(application.getAppId());
            appletMsg.setFormId(pushMessage.getFormId());
            appletMsg.setOpenId(customerApplication.getWxOpenid());
            appletMsg.setTemplateId(WxAppletTemplate.ORDER_CANCEL_TEMPLATE_ID);
            List<WxMaTemplateData> templateDatas = new ArrayList<>();
            templateDatas.add(new WxMaTemplateData("keyword1", order.getOrderNo()));
            MerchantShop shop=merchantService.getMerchantShopById(order.getShopId());
            templateDatas.add(new WxMaTemplateData("keyword2", shop!=null?shop.getName():""));
            templateDatas.add(new WxMaTemplateData("keyword3", DateUtil.getLocalDateTimeString(order.getCreateTime())));
            templateDatas.add(new WxMaTemplateData("keyword4", order.getPayAmount().toPlainString()+"元"));
            templateDatas.add(new WxMaTemplateData("keyword5", order.getGoodsNames()));
            appletMsg.setTemplateData(templateDatas);
            appletMsg.setPage("pages/serviceRecords/index?status=1");
            applicationContext.publishEvent(appletMsg);
        }
    }


    /**
     * 推送结算消息(代人支付的)
     *
     * @param order
     */
    @Override
    public void pushOrderSettle(Order order) {
        PushMessage pushMessage = pushMessageService.getByBusinessIdAndType(order.getId(), BusinessType.WX_APPOINTMENT_CREATE);//todo 余额扣除没有服务通知
        CustomerApplication customerApplication = customerService.getCustomerApplicationByCustomerIdAndApplicationId(order.getCustomerId(), order.getMerchantAppId());
        Assert.notNull(pushMessage, "推送消息对象不存在");
//        MerchantApplication application = CurrentApplicationUtil.getMerchantApplication();
//        WxAppletMsgEvent appletMsg = new WxAppletMsgEvent(this);
//        appletMsg.setAppid(application.getAppId());
//        appletMsg.setFormId(pushMessage.getFormId());
//        appletMsg.setOpenId(customerApplication.getWxOpenid());
//        appletMsg.setTemplateId(WxAppletTemplate.ORDER_SETTLE_TEMPLATE_ID);
////            appletMsg.setTemplateData();
//        appletMsg.setPage("/pages/index/index");
//        applicationContext.publishEvent(appletMsg);
    }

    /**
     * 预约确认
     * @param customerAppointment
     */
    @Override
    public void pushCustomerAppointmentConfirmMsg(CustomerAppointment customerAppointment) {
        CustomerApplication customerApplication = customerService.getCustomerApplicationByCustomerIdAndApplicationId(customerAppointment.getCustomerId(), customerAppointment.getMerchantAppId());
        PushMessage pushMessage = pushMessageService.getByBusinessIdAndType(customerAppointment.getId(), BusinessType.WX_APPOINTMENT_CREATE);
        Assert.notNull(pushMessage, "预约对象不存在");
        WxAppletMsgEvent appletMsg = new WxAppletMsgEvent(this);
        appletMsg.setAppid(CurrentApplicationUtil.getMerchantApplication().getAppId());
        appletMsg.setFormId(pushMessage.getFormId());
        appletMsg.setOpenId(customerApplication.getWxOpenid());
        appletMsg.setTemplateId(WxAppletTemplate.APPOINTMENT_CONFIRM_TEMPLATE_ID);
        List<WxMaTemplateData> templateDatas = new ArrayList<>();
        templateDatas.add(new WxMaTemplateData("keyword1", customerAppointment.getAppointName()));
        templateDatas.add(new WxMaTemplateData("keyword2", customerAppointment.getAppointMobile()));
        templateDatas.add(new WxMaTemplateData("keyword3", customerAppointment.getShopName()));
        templateDatas.add(new WxMaTemplateData("keyword4", DateUtil.getLocalDateString(customerAppointment.getArriveDate())+" "+DateUtil.getLocalTimeString(customerAppointment.getArriveTime())));
        if (customerAppointment.getMerchantUserId()!=null){
            MerchantUser merchantUser=merchantUserService.getMerchantUserById(customerAppointment.getMerchantUserId());
            templateDatas.add(new WxMaTemplateData("keyword5", merchantUser!=null?merchantUser.getNickname():""));
        }else{
            templateDatas.add(new WxMaTemplateData("keyword5", "到店指定"));
        }
        templateDatas.add(new WxMaTemplateData("keyword6", StringUtils.isNotBlank(customerAppointment.getGoodsName())?customerAppointment.getGoodsName():"到店确认"));
        appletMsg.setTemplateData(templateDatas);
        appletMsg.setPage("pages/myOrder/index");
        applicationContext.publishEvent(appletMsg);
    }


    /**
     * 改约
     * @param customerAppointment
     */
    @Override
    public void pushCustomerAppointmentChangeMsg(CustomerAppointment customerAppointment) {
        CustomerApplication customerApplication = customerService.getCustomerApplicationByCustomerIdAndApplicationId(customerAppointment.getCustomerId(), customerAppointment.getMerchantAppId());
        PushMessage pushMessage = pushMessageService.getByBusinessIdAndType(customerAppointment.getId(), BusinessType.WX_APPOINTMENT_CREATE);
        Assert.notNull(pushMessage, "预约对象不存在");
        WxAppletMsgEvent appletMsg = new WxAppletMsgEvent(this);
        appletMsg.setAppid(CurrentApplicationUtil.getMerchantApplication().getAppId());
        appletMsg.setFormId(pushMessage.getFormId());
        appletMsg.setOpenId(customerApplication.getWxOpenid());
        appletMsg.setTemplateId(WxAppletTemplate.APPOINTMENT_CHANGE_TEMPLATE_ID);
        List<WxMaTemplateData> templateDatas = new ArrayList<>();
        templateDatas.add(new WxMaTemplateData("keyword1", DateUtil.getLocalDateString(customerAppointment.getArriveDate())+" "+DateUtil.getLocalTimeString(customerAppointment.getArriveTime())));
        templateDatas.add(new WxMaTemplateData("keyword2", customerAppointment.getShopName()));
        if (customerAppointment.getMerchantUserId()!=null){
            MerchantUser merchantUser=merchantUserService.getMerchantUserById(customerAppointment.getMerchantUserId());
            templateDatas.add(new WxMaTemplateData("keyword3", merchantUser!=null?merchantUser.getNickname():""));
        }else{
            templateDatas.add(new WxMaTemplateData("keyword3", "到店指定"));
        }
        templateDatas.add(new WxMaTemplateData("keyword4", StringUtils.isNotBlank(customerAppointment.getGoodsName())?customerAppointment.getGoodsName():"到店确认"));
        templateDatas.add(new WxMaTemplateData("keyword5","您的预约已经修改，点击下方进入小程序咨询门店客服或联系设计师" ));
        appletMsg.setTemplateData(templateDatas);
        appletMsg.setPage("pages/myOrder/index");
        applicationContext.publishEvent(appletMsg);
    }

    /**
     * 预约取消
     * @param customerAppointment
     */
    @Override
    public void pushCustomerAppointmentCancelMsg(CustomerAppointment customerAppointment) {
        CustomerApplication customerApplication = customerService.getCustomerApplicationByCustomerIdAndApplicationId(customerAppointment.getCustomerId(), customerAppointment.getMerchantAppId());
        PushMessage pushMessage = pushMessageService.getByBusinessIdAndType(customerAppointment.getId(), BusinessType.WX_APPOINTMENT_CREATE);
        Assert.notNull(pushMessage, "预约对象不存在");
        WxAppletMsgEvent appletMsg = new WxAppletMsgEvent(this);
        appletMsg.setAppid(CurrentApplicationUtil.getMerchantApplication().getAppId());
        appletMsg.setFormId(pushMessage.getFormId());
        appletMsg.setOpenId(customerApplication.getWxOpenid());
        appletMsg.setTemplateId(WxAppletTemplate.APPOINTMENT_CANCEL_TEMPLATE_ID);
        List<WxMaTemplateData> templateDatas = new ArrayList<>();
        templateDatas.add(new WxMaTemplateData("keyword1", customerAppointment.getAppointName()));
        templateDatas.add(new WxMaTemplateData("keyword2", DateUtil.getLocalDateString(customerAppointment.getArriveDate())+" "+DateUtil.getLocalTimeString(customerAppointment.getArriveTime())));
        templateDatas.add(new WxMaTemplateData("keyword3", customerAppointment.getShopName()));
        templateDatas.add(new WxMaTemplateData("keyword4", StringUtils.isNotBlank(customerAppointment.getGoodsName())?customerAppointment.getGoodsName():"到店确认"));
        if (customerAppointment.getMerchantUserId()!=null){
            MerchantUser merchantUser=merchantUserService.getMerchantUserById(customerAppointment.getMerchantUserId());
            templateDatas.add(new WxMaTemplateData("keyword5", merchantUser!=null?merchantUser.getNickname():""));
        }else{
            templateDatas.add(new WxMaTemplateData("keyword5", "到店指定"));
        }
        appletMsg.setTemplateData(templateDatas);
        appletMsg.setPage("pages/myOrder/index");
        applicationContext.publishEvent(appletMsg);
    }
}
