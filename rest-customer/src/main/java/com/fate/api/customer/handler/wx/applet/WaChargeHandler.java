package com.fate.api.customer.handler.wx.applet;

import com.fate.api.customer.config.wx.WxPayConfiguration;
import com.fate.api.customer.dto.PayDto;
import com.fate.api.customer.handler.ChargeHandler;
import com.fate.api.customer.service.CustomerService;
import com.fate.api.customer.service.PushMessageService;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.entity.Customer;
import com.fate.common.entity.CustomerApplication;
import com.fate.common.entity.CustomerCharge;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.enums.ConsumeType;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentRequestUtil;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: parent
 * @description: 微信小程序支付
 * @author: chenyixin
 * @create: 2019-06-24 22:57
 **/
@Component
@Slf4j
public class WaChargeHandler implements ChargeHandler {
    private static final String VERSION="/v1";
    private static final String MAPPING="/charge/notify";
    @Value("${wx.pay.notifyUrl}")
    private String wxNotifyUrl;
    @Autowired
    PushMessageService pushMessageService;
    @Autowired
    CustomerService customerService;

    /**
     * 会员充值预支付
     * @param charge
     * @return
     */
    @Override
    public PayDto preparePay(CustomerCharge charge) {
        CustomerApplication customerApplication=CurrentCustomerUtil.getCustomerApplication();
        MerchantApplication application= CurrentApplicationUtil.getMerchantApplication();
        final WxPayService payService= WxPayConfiguration.getPayService(application.getAppId());
        WxPayUnifiedOrderRequest request= WxPayUnifiedOrderRequest.newBuilder().openid(customerApplication.getWxOpenid())
                .tradeType(WxPayConstants.TradeType.JSAPI)
                .body(application.getName()+"-"+"微信充值")
                .outTradeNo(charge.getId().toString())
                .totalFee(charge.getAmount().multiply(BigDecimal.valueOf(100)).intValue())
                .spbillCreateIp(CurrentRequestUtil.getCurrentClientIp())
                .notifyUrl(wxNotifyUrl+application.getId()+VERSION+MAPPING)
                .attach(application.getMerchantId()+":"+charge.getId())
                .build();
        WxPayMpOrderResult wxPayMpOrderResult=null;
        try {
            wxPayMpOrderResult=payService.createOrder(request);//内部已做校验
        } catch (WxPayException e) {
            log.error("会员充值charge={}失败",charge,e);
        }
        Assert.notNull(wxPayMpOrderResult,"微信充值接口返回为空");
        //记录业务关联信息
        pushMessageService.createChargePushMessage(charge,getPrepayId(wxPayMpOrderResult));
        PayDto result= BeanUtil.mapper(wxPayMpOrderResult,PayDto.class);
        result.setType(ConsumeType.WX_PAY);
        result.setPay(false);
        return result;
    }

    /**
     * 获取预支付ID
     * @param wxPayMpOrderResult
     * @return
     */
    private String getPrepayId(WxPayMpOrderResult wxPayMpOrderResult){
        if (StringUtils.isNotBlank(wxPayMpOrderResult.getPackageValue())){
            String[] array=wxPayMpOrderResult.getPackageValue().split("=");
            if (array.length>1){
                return array[1];
            }
        }
        return "";
    }


    /**
     * 查询会员支付结果
     * @param charge
     */
    @Override
    public Boolean queryChargeResult(CustomerCharge charge) {
        Boolean result=false;
        final WxPayService payService = WxPayConfiguration.getPayService(CurrentApplicationUtil.getMerchantApplication().getAppId());
        WxPayOrderQueryResult wxPayOrderQueryResult =null;
        try {
            wxPayOrderQueryResult = payService.queryOrder(null, charge.getId().toString());
        } catch (WxPayException e) {
            log.error("查询微信支付结果失败，result={},charge={}",wxPayOrderQueryResult,charge,e);
            return false;
        }
        Assert.notNull(wxPayOrderQueryResult, "查询微信充值结果为空");

        Integer payment = charge.getAmount().multiply(BigDecimal.valueOf(100)).intValue();
        if (payment.equals(wxPayOrderQueryResult.getTotalFee())) {
            result=true;
        } else {
            log.error("商户订单和微信结算金额不一致，order={},result={}", charge, wxPayOrderQueryResult);
            result=false;
        }
        return result;
    }

}
