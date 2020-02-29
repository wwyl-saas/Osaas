package com.fate.api.customer.handler.wx.applet;

import com.fate.api.customer.config.wx.WxPayConfiguration;
import com.fate.api.customer.dto.PayDto;
import com.fate.api.customer.handler.PayHandler;
import com.fate.api.customer.service.CustomerService;
import com.fate.api.customer.service.PushMessageService;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.entity.CustomerApplication;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.entity.Order;
import com.fate.common.enums.ConsumeType;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentRequestUtil;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayOrderCloseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * @program: parent
 * @description: 微信支付
 * @author: chenyixin
 * @create: 2019-06-21 15:03
 **/
@Slf4j
@Component
public class WaPayHandler implements PayHandler {
    private static final String VERSION = "/v1";
    private static final String MAPPING = "/pay/notify";
    @Value("${wx.pay.notifyUrl}")
    private String wxNotifyUrl;
    @Autowired
    PushMessageService pushMessageService;
    @Autowired
    CustomerService customerService;

    @Override
    public ConsumeType getPayType() {
        return ConsumeType.WX_PAY;
    }

    /**
     * 微信小程序预支付
     * @param order
     * @return
     */
    @Override
    public PayDto preparePay(Order order) {

        MerchantApplication application = CurrentApplicationUtil.getMerchantApplication();
        CustomerApplication customerApplication=CurrentCustomerUtil.getCustomerApplication();
        WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder().openid(customerApplication.getWxOpenid())
                .tradeType(WxPayConstants.TradeType.JSAPI)
                .body(application.getName() + "-" + "线下服务")
                .outTradeNo(order.getOrderNo())
                .totalFee((order.getPayAmount().multiply(BigDecimal.valueOf(100))).intValue())
                .spbillCreateIp(CurrentRequestUtil.getCurrentClientIp())
                .notifyUrl(wxNotifyUrl+ application.getId()+ VERSION  + MAPPING)
                .attach(order.getMerchantId() + ":" + order.getId())
                .build();
        final WxPayService payService = WxPayConfiguration.getPayService(application.getAppId());
        WxPayMpOrderResult wxPayMpOrderResult= null;
        try {
            wxPayMpOrderResult = payService.createOrder(request);//内部会做校验
        } catch (WxPayException e) {
            log.error("微信统一下单失败order={}",order,e);
        }
        Assert.notNull(wxPayMpOrderResult,"微信支付失败");
        //记录业务关联信息
        pushMessageService.createOrderPayPushMessage(order,getPrepayId(wxPayMpOrderResult));

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
     *  查询微信支付结果，对支付额做校验
     * @return
     */
    @Override
    public Boolean queryPayResult(Order order) {
        Boolean result=false;
        final WxPayService payService = WxPayConfiguration.getPayService(CurrentApplicationUtil.getMerchantApplication().getAppId());
        WxPayOrderQueryResult wxPayOrderQueryResult = null;
        try {
            wxPayOrderQueryResult = payService.queryOrder(null, order.getOrderNo());
        } catch (WxPayException e) {
            log.error("查询微信支付结果失败，result={},order={}",wxPayOrderQueryResult,order,e);
            return false;
        }
        Assert.notNull(wxPayOrderQueryResult, "查询微信订单结果为空");

        Integer payment = order.getPayAmount().multiply(BigDecimal.valueOf(100)).intValue();
        if (wxPayOrderQueryResult.getTradeState().equals("SUCCESS") && payment.equals(wxPayOrderQueryResult.getTotalFee())) {
            result=true;
        } else {
            log.error("商户订单和微信支付存在问题，order={},result={}", order, wxPayOrderQueryResult);
            result=false;
        }
        return result;
    }

    /**
     * 微信取消支付
     * @param order
     * @return
     */
    @Override
    public void cancelPay(Order order) throws WxPayException {
        String appid= CurrentApplicationUtil.getMerchantApplication().getAppId();
        WxPayOrderCloseResult result= WxPayConfiguration.getPayService(appid).closeOrder(new WxPayOrderCloseRequest(order.getOrderNo()));
        if (!result.getResultCode().equals(WxPayConstants.ResultCode.SUCCESS)){//取消成功
            log.error("微信取消订单{}失败，返回",order.getId(),result.toString());
            throw new BaseException(ResponseInfo.WX_CANCEL_ERROR);
        }
    }
}
