package com.fate.api.customer.controller;

import com.fate.api.customer.service.ChargeService;
import com.fate.api.customer.service.PayService;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @program: parent
 * @description: 微信小程序支付相关回调接口
 * @author: chenyixin
 * @create: 2019-05-30 00:08
 **/
@ApiIgnore//使用该注解忽略这个API
@RestController
@RequestMapping("/callback/{applicationId}/v1")
@Slf4j
public class AppletPayCallbackController {
    @Autowired
    ChargeService chargeService;
    @Autowired
    PayService payService;
    /**
     * <pre>
     * 微信通知支付结果
     * </pre>
     */
    @PostMapping(value = "/pay/notify" ,produces = "text/plain;charset=utf-8")
    public String payNotify(@RequestBody String xmlData) throws WxPayException {
        log.info("微信反馈支付结果,data="+xmlData);
        WxPayOrderNotifyResult result= WxPayOrderNotifyResult.fromXML(xmlData);
        if (result.getResultCode().equals(WxPayConstants.ResultCode.SUCCESS) && StringUtils.isNotBlank(result.getAttach())){
            String[] array=result.getAttach().split(":");
            Assert.noNullElements(array,"微信通知返回附加数据为空");
            Long orderId=Long.parseLong(array[1]);
            payService.consumeWxData(xmlData,orderId);
        }else{
            log.error("微信通知返回数据异常:{}",xmlData);
        }
        //返还成功，微信服务器停止重复调用
        return WxPayNotifyResponse.success("成功");
    }


    /**
     * <pre>
     * 微信通知充值结果
     * </pre>
     */
    @PostMapping(value = "/charge/notify" ,produces = "text/plain;charset=utf-8")
    public String notify(@PathVariable String applicationId, @RequestBody String xmlData) {
        log.info("微信反馈支付结果,data="+xmlData);
        WxPayOrderNotifyResult result= WxPayOrderNotifyResult.fromXML(xmlData);
        if (result.getResultCode().equals(WxPayConstants.ResultCode.SUCCESS) && StringUtils.isNotBlank(result.getAttach())){
            String[] array=result.getAttach().split(":");
            Assert.noNullElements(array,"微信通知返回附加数据为空");
            Long chargeId=Long.parseLong(array[1]);
           chargeService.consumeWxData(xmlData,chargeId);
        }else{
            log.error("微信通知返回数据异常:{}",xmlData);
        }
        //返还成功，微信服务器停止重复调用
        return WxPayNotifyResponse.success("成功");
    }

}
