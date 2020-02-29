package com.fate.api.customer.service;

import com.fate.api.customer.config.feign.FeignConfiguration;
import com.fate.api.customer.config.wx.WxPayConfiguration;
import com.fate.api.customer.dto.PayDto;
import com.fate.api.customer.exception.OrderStatusException;
import com.fate.api.customer.handler.EventHandler;
import com.fate.api.customer.handler.HandlerFactory;
import com.fate.api.customer.handler.PayHandler;
import com.fate.api.customer.query.ConfirmPayQuery;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.cons.RedisKey;
import com.fate.common.entity.*;
import com.fate.common.enums.ConsumeType;
import com.fate.common.enums.MessageTag;
import com.fate.common.enums.OrderStatus;
import com.fate.common.model.MqProperties;
import com.fate.common.model.MyMessage;
import com.fate.common.model.StandardResponse;
import com.fate.common.util.CurrentMerchantUtil;
import com.fate.common.util.IdUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.io.IOException;

/**
 * @program: parent
 * @description: 支付相关
 * @author: chenyixin
 * @create: 2019-05-26 18:45
 **/
@Service
@Slf4j
public class PayService {
    private static final String key = "order:pay:";
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Autowired
    MerchantService merchantService;
    @Autowired
    CouponService couponService;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderService orderService;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    CustomerConsumeService customerConsumeService;
    @Autowired
    HandlerFactory handlerFactory;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    CacheService cacheService;
    @Autowired
    MqSendService mqSendService;
    @Autowired
    PushMessageService pushMessageService;


    /**
     * 确认支付接口
     *
     * @return
     */
    public PayDto confirmOrder(ConfirmPayQuery confirmPayQuery) {
        Order order = orderService.findOrderById(confirmPayQuery.getOrderId());
        Assert.notNull(order, "订单不存在");
        if (!order.getStatus().equals(OrderStatus.WAITING_PAY)) {
            throw new OrderStatusException();
        }
        CustomerAccount customerAccount = customerAccountService.findByCustomerId(order.getCustomerId());
        Assert.notNull(customerAccount, "订单用户账户不存在");
        if (order.getPayAmount().compareTo(customerAccount.getBalance()) <= 0) {//余额支付
            remainderPay(order,customerAccount);
            pushMessageService.createOrderPayPushMessage(order,confirmPayQuery.getFormId());
            EventHandler eventHandler = handlerFactory.getEventHandler();
            eventHandler.pushNotifyPaySuccess(order);
            PayDto result = new PayDto();
            result.setType(ConsumeType.REMAINDER);
            result.setPay(true);
            //推送商户端结果
            sendOrderSettleToMerchant(order);
            return result;
        } else {
            PayHandler payHandler = handlerFactory.getPayHandler();
            PayDto result = payHandler.preparePay(order);
            result.setPay(false);
            result.setPayId(order.getId());
            return result;
        }
    }


    /**
     * 发送订单结算信息
     */
    private void sendOrderSettleToMerchant(Order order) {
        String existIp = (String) cacheService.get(RedisKey.MERCHANT_SOCKET_KEY + order.getMerchantUserId());
        if (StringUtils.isNotBlank(existIp)) {
            MerchantSettleService merchantSettleService = FeignConfiguration.getMerchantSettleService(existIp);
            StandardResponse standardResponse = merchantSettleService.sendOrderSettleToUser(order.getId());
            if (standardResponse.getCode() != 0) {
                log.error("通知商户端接口返回异常:{}", standardResponse);
            }
        } else {
            log.error("商户端连接不存在，application :{},user:{}", order.getMerchantAppId(), order.getMerchantUserId());
        }
        //通知商户结果
        MyMessage myMessage=new MyMessage(order.getId());
        MqProperties mqProperties=MqProperties.builder().merchantId(CurrentMerchantUtil.getMerchant().getId()).messageTag(MessageTag.ORDER_SETTLE.getTag()).build();
        mqSendService.sendToMerchant(myMessage,mqProperties);
    }


    /**
     * 查询订单是否支付成功，当查询结果为支付成功时更新订单数据
     *
     * @param orderId
     * @return
     */
    public Boolean queryPayResult(Long orderId) {
        Boolean result = false;
        Order order = orderService.findOrderById(orderId);
        Assert.notNull(order, "订单不存在");
        EventHandler eventHandler = handlerFactory.getEventHandler();
        if (order.getStatus().equals(OrderStatus.WAITING_PAY)) {
            PayHandler payHandler = handlerFactory.getPayHandler();
            result = payHandler.queryPayResult(order);
            if (result) {
                ConsumeType consumeType = payHandler.getPayType();
                completePay(order, consumeType);
                sendOrderSettleToMerchant(order);
                //通知客户端
                eventHandler.pushQueryPaySuccess(order);
            }
        } else {
            if (order.getStatus().equals(OrderStatus.PAYED) || order.getStatus().equals(OrderStatus.COMPLETED)) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }


    /**
     * 验证和消费微信回调数据
     *
     * @param xmlData
     * @param orderId
     */
    public void consumeWxData(String xmlData, Long orderId) throws WxPayException {
        EventHandler eventHandler = handlerFactory.getEventHandler();
        WxPayService wxPayService = WxPayConfiguration.getPayService(CurrentApplicationUtil.getMerchantApplication().getAppId());
        WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
        Order order = orderService.findOrderById(orderId);
        Assert.notNull(order, "微信回调信息所属订单不存在");
        if (order.getStatus().equals(OrderStatus.WAITING_PAY)) {
            completePay(order, ConsumeType.WX_PAY);
            //通知商户端
            sendOrderSettleToMerchant(order);
            //通知客户结果
            eventHandler.pushNotifyPaySuccess(order);
        } else {
            if (order.getStatus().equals(OrderStatus.COMPLETED) || order.getStatus().equals(OrderStatus.PAYED)) {
                log.info("当前订单已经支付,notify={}", notifyResult.toString());
            } else {
                log.error("当前订单order={}状态不正确,微信返回信息result={}", order, notifyResult);
            }
        }

    }


    /**
     * 余额支付-完成订单支付状态
     *
     * @param order
     * @param customerAccount
     * @return
     */
    @Transactional
    protected void remainderPay(Order order, CustomerAccount customerAccount) {
        //更新账户信息
        customerAccountService.consumeByBalance(order.getPayAmount(), customerAccount);

        //修改订单状态
        order.setStatus(OrderStatus.PAYED);
        Assert.isTrue(order.updateById(), "数据更新失败");

        //修改优惠券状态
        couponService.consumeCoupon(order.getCouponId());
        //增加消费记录
        customerConsumeService.createConsumeLog(order, ConsumeType.REMAINDER);
    }


    /**
     * 微信支付-完成订单支付状态
     *
     * @param order
     */
    @Transactional
    protected void completePay(Order order, ConsumeType consumeType) {
        //增加积分
        CustomerAccount customerAccount = customerAccountService.findByCustomerId(order.getCustomerId());
        Assert.notNull(customerAccount, "下单用户不存在");
        customerAccountService.consumeNotBalance(order.getPayAmount(), customerAccount);


        //修改订单状态
        order.setStatus(OrderStatus.PAYED);
        order.setUpdateTime(null);
        Assert.isTrue(orderService.updateOrder(order), "数据更新失败");
        //消费优惠券
        couponService.consumeCoupon(order.getCouponId());
        //增加消费记录
        customerConsumeService.createConsumeLog(order, consumeType);
    }

    /**
     * 放弃支付
     *
     * @param orderId
     * @return
     */
    public void abandonOrder(Long orderId) {
        Order order = orderService.findOrderById(orderId);
        Assert.notNull(order, "订单不存在");
        if (order.getStatus().equals(OrderStatus.WAITING_PAY)) {
            sendOrderAbandonToMerchant(order);
        } else {
            throw new OrderStatusException();
        }

    }


    /**
     * 发送订单结算信息
     */
    private void sendOrderAbandonToMerchant(Order order) {
        String existIp = (String) cacheService.get(RedisKey.MERCHANT_SOCKET_KEY + order.getMerchantUserId());
        if (StringUtils.isNotBlank(existIp)) {
            MerchantSettleService merchantSettleService = FeignConfiguration.getMerchantSettleService(existIp);
            StandardResponse standardResponse = merchantSettleService.sendOrderAbandonToUser(order.getId());
            if (standardResponse.getCode() != 0) {
                log.error("通知商户端接口返回异常:{}", standardResponse);
            }
        } else {
            log.error("商户端连接不存在，application :{},user:{}", order.getMerchantAppId(), order.getMerchantUserId());
        }
    }

    /**
     * 生成支付条形码
     *
     * @return
     */
    public String takePayCode() {
        Customer customer = CurrentCustomerUtil.getCustomer();
        MerchantApplication application = CurrentApplicationUtil.getMerchantApplication();
        Long barCode = IdUtil.nextId();
        cacheService.set(key + barCode, customer.getId() + "-" + application.getId(), 1800L);//条形码有效时间30分钟
        return barCode.toString();
    }
}
