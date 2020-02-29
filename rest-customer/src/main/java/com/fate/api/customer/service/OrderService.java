package com.fate.api.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.api.customer.config.feign.FeignConfiguration;
import com.fate.api.customer.dto.*;
import com.fate.api.customer.endpoint.PayWebSocket;
import com.fate.api.customer.exception.OrderStatusException;
import com.fate.api.customer.handler.EventHandler;
import com.fate.api.customer.handler.HandlerFactory;
import com.fate.api.customer.handler.PayHandler;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.api.customer.util.CustomerSessionUtil;
import com.fate.common.cons.RedisKey;
import com.fate.common.dao.*;
import com.fate.common.entity.*;
import com.fate.common.enums.*;
import com.fate.common.exception.BaseException;
import com.fate.common.model.MqProperties;
import com.fate.common.model.MyMessage;
import com.fate.common.model.StandardResponse;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentMerchantUtil;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 订单相关
 * @author: chenyixin
 * @create: 2019-05-27 15:45
 **/
@Service
@Slf4j
public class OrderService {
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Autowired
    CouponService couponService;
    @Autowired
    PayWebSocket payWebSocket;
    @Autowired
    CustomerService customerService;
    @Resource
    CustomerAccountService customerAccountService;
    @Resource
    OrderDao orderDao;
    @Resource
    OrderDescDao orderDescDao;
    @Resource
    CustomerConsumeDao customerConsumeDao;
    @Resource
    MerchantShopDao merchantShopDao;
    @Autowired
    CouponFilterService couponFilterService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    PushMessageService pushMessageService;
    @Autowired
    HandlerFactory handlerFactory;
    @Autowired
    MqSendService mqSendService;
    @Resource
    MemberWelfareDao memberWelfareDao;
    @Autowired
    CacheService cacheService;

    /**
     * 计算order的商品总价、折扣金额、
     * @param order
     */
    private void caculateOrder(Order order, CustomerCoupon customerCoupon) {
        //会员折扣金额
        CustomerAccount customerAccount=customerAccountService.findByCustomerId(order.getCustomerId());
        Assert.notNull(customerAccount,"用户未领取会员卡");
        List<MemberWelfare> memberWelfares=memberWelfareDao.getByMemberIdAndType(customerAccount.getMemberId(),MemberWelfareType.SERVICE_DISCOUNT);
        BigDecimal discountRate=BigDecimal.ONE;
        if (CollectionUtils.isNotEmpty(memberWelfares) && StringUtils.isNotBlank(memberWelfares.get(0).getWelfareValue())){
            discountRate=BigDecimal.valueOf(Double.parseDouble(memberWelfares.get(0).getWelfareValue())).setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        BigDecimal afterMemberDiscount=order.getOrderSumAmount().multiply(discountRate).setScale(2,BigDecimal.ROUND_HALF_UP);
        order.setDiscountAmount(order.getOrderSumAmount().subtract(afterMemberDiscount));

        //优惠券金额
        if (customerCoupon!=null){
            couponService.lockCoupon(customerCoupon);
            order.setCouponId(customerCoupon.getId());
            BigDecimal couponAmout=BigDecimal.ZERO;
            if (customerCoupon.getType().equals(CouponType.DISCOUNT_COUPON)){
                BigDecimal afterCoupon=afterMemberDiscount.multiply(BigDecimal.valueOf(customerCoupon.getDiscount()/100D)).setScale(2,BigDecimal.ROUND_HALF_UP);//百分值转换
                couponAmout=afterMemberDiscount.subtract(afterCoupon);
            }else if (customerCoupon.getType().equals(CouponType.CASH_COUPON)){
                couponAmout=BigDecimal.valueOf(customerCoupon.getDiscount()/100D).setScale(2,BigDecimal.ROUND_HALF_UP);//分转元
                if (couponAmout.compareTo(afterMemberDiscount)>=0){
                    couponAmout=afterMemberDiscount;
                }
            }
            order.setCouponAmount(couponAmout);
        }else{
            order.setCouponAmount(BigDecimal.ZERO);
        }

        //实际支付金额 总金额-会员折扣金额-优惠券金额
        BigDecimal payAmount=order.getOrderSumAmount().subtract(order.getDiscountAmount()).subtract(order.getCouponAmount());
        order.setPayAmount(payAmount);
    }

    /**
     * 通过订单ID查询详情列表
     * @param orderId
     * @return
     */
    public  List<OrderDesc> getByOrderId(Long orderId){
        return orderDescDao.getByOrderId(orderId);
    }


    public Order findOrderById(Long orderId) {
        return orderDao.getById(orderId);
    }


    public boolean updateOrder(Order order) {
        return orderDao.updateById(order);
    }

    /**
     * 查询历史订单列表
     * @param status
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageDto<OrderDto> findOrderHisByCustomer(OrderStatus status, Long pageIndex, Long pageSize){
        Long customerId= CurrentCustomerUtil.getCustomer().getId();
        final IPage<Order> orderIPage = orderDao.findByCustomer(customerId, status,pageIndex,pageSize);
        final List<Order> orders = orderIPage.getRecords();
        List<OrderDto> result=new ArrayList<OrderDto>();
        if(CollectionUtils.isNotEmpty(orders)){
            final List<Long> orderIds = orders.stream().filter(order -> order.getId() != null).map(order -> order.getId()).collect(Collectors.toList());
            final Map<Long, List<OrderDescDto>> orderDescMap = orderDescDao.getByOrderIds(orderIds).stream().filter(orderDesc -> orderDesc != null).map(orderDesc -> {
                OrderDescDto descDto=BeanUtil.mapper(orderDesc, OrderDescDto.class);
                if (descDto.getBriefPicUrl()!=null){
                    descDto.setBriefPicUrl(cdnDomain+descDto.getBriefPicUrl());
                }
                return descDto;
            }).collect(Collectors.groupingBy(OrderDescDto::getOrderId));

            result=orders.stream().map(order -> {
                OrderDto dto = BeanUtil.mapper(order, OrderDto.class);
                final List<OrderDescDto> orderDescDtos = orderDescMap.get(order.getId());
                dto.setOrderDescList(orderDescDtos);
                return dto;
            }).collect(Collectors.toList());
        }
        return  new PageDto<>(pageIndex,pageSize,orderIPage.getTotal(),orderIPage.getPages(),result);
    }

    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    public OrderDto findOrderDescById(Long orderId) {
        Order order=orderDao.getById(orderId);
        Assert.notNull(order,"订单不存在");
        OrderDto orderDto = BeanUtil.mapper(order,OrderDto.class);
        ConsumeType consumeType = Optional.ofNullable(customerConsumeDao.selectByOrderId(orderId)).map(customerConsume -> customerConsume.getConsumeType()).orElse(null);
        String shopName = Optional.ofNullable(merchantShopDao.getById(order.getShopId())).map(merchantShop -> merchantShop.getName()).orElse(null);
        orderDto.setConsumeType(consumeType);
        orderDto.setShopName(shopName);
        List<OrderDesc> orderDescs = orderDescDao.getByOrderId(orderId);
        if (CollectionUtils.isNotEmpty(orderDescs)) {
            orderDto.setOrderDescList(orderDescs.stream().map(orderDesc -> {
                OrderDescDto dto = BeanUtil.mapper(orderDesc, OrderDescDto.class);
                dto.setBriefPicUrl(cdnDomain + dto.getBriefPicUrl());
                return dto;
            }).collect(Collectors.toList()));
        }
        return orderDto;
    }

    /**
     * 取消订单,未点击确认时直接取消，点击确认以后未完成微信支付，需要调用接口。
     * @param orderId
     * @throws WxPayException
     */
    public void cancelOrder(Long orderId) throws WxPayException {
        Order order=findOrderById(orderId);
        Assert.notNull(order,"订单不存在");
        if (order.getStatus().equals(OrderStatus.WAITING_PAY)){
            order.setStatus(OrderStatus.CANCELED);
            Assert.isTrue(order.updateById(),"数据更新失败");
            //释放优惠券
            if (order.getCouponId()!=null){
                couponService.unlockCoupon(order.getCouponId());
            }

            PushMessage pushMessage = pushMessageService.getByBusinessIdAndType(orderId, BusinessType.WX_PAY_CREATE);
            if (pushMessage!=null){
                PayHandler payHandler=handlerFactory.getPayHandler();
                try {
                    payHandler.cancelPay(order);
                } catch (WxPayException e) {
                    log.error("取消订单{}失败",order);
                }
            }

            //通知商户端
            MyMessage myMessage=new MyMessage(orderId);
            MqProperties mqProperties=MqProperties.builder().merchantId(CurrentMerchantUtil.getMerchant().getId()).messageTag(MessageTag.CUSTOMER_ORDER_CANCEL.getTag()).build();
            mqSendService.sendToMerchant(myMessage,mqProperties);
        }else {
            throw new OrderStatusException(order.getStatus().getDesc()+"状态订单不能取消");
        }
    }

    /**
     * 修改优惠券重新计算
     * @param couponId
     * @param orderId
     * @return
     */
    public OrderDto changeCoupon(Long couponId, Long orderId) {
        Order order=orderDao.getById(orderId);
        Assert.notNull(order,"ID查询的订单不存在");
        couponService.unlockCoupon(order.getCouponId());
        CustomerCoupon customerCoupon=null;
        if (couponId!=null){
            customerCoupon=couponService.findCouponById(couponId);
            Assert.notNull(customerCoupon,"优惠券不存在");
        }
        caculateOrder(order,customerCoupon);
        order.setUpdateTime(null);
        Assert.isTrue(orderDao.updateById(order),"数据更新失败");
        //通知商户端订单变化
        sendOrderChangeToMerchant(order);
        return BeanUtil.mapper(order,OrderDto.class);
    }

    /**
     * 发送订单结算信息
     */
    private void sendOrderChangeToMerchant(Order order) {
        String existIp = (String) cacheService.get(RedisKey.MERCHANT_SOCKET_KEY + order.getMerchantUserId());
        if (StringUtils.isNotBlank(existIp)) {
            MerchantSettleService merchantSettleService = FeignConfiguration.getMerchantSettleService(existIp);
            StandardResponse standardResponse = merchantSettleService.sendOrderChangeToUser(order.getId());
            if (standardResponse.getCode() != 0) {
                log.error("通知商户端接口返回异常:{}", standardResponse);
            }
        } else {
            log.error("商户端连接不存在，application :{},user:{}", order.getMerchantAppId(), order.getMerchantUserId());
        }
    }


    /**
     * 给用户发送订单取消提醒
     * @param orderId
     */
    public void sendOrderCancelToCustomer(Long orderId) throws WxPayException {
        Order order=orderDao.getById(orderId);
        Assert.notNull(order,"订单不存在");
        if (order.getStatus().equals(OrderStatus.WAITING_PAY)){
            order.setStatus(OrderStatus.CANCELED);
            Assert.isTrue(order.updateById(),"数据更新失败");

            //释放优惠券
            if (order.getCouponId()!=null){
                couponService.unlockCoupon(order.getCouponId());
            }

            PushMessage pushMessage = pushMessageService.getByBusinessIdAndType(orderId, BusinessType.WX_PAY_CREATE);
            if (pushMessage!=null){
                PayHandler payHandler=handlerFactory.getPayHandler();
                try {
                    payHandler.cancelPay(order);
                } catch (WxPayException e) {
                    log.error("取消订单{}失败",order);
                }
                //通知客户端
                EventHandler eventHandler=handlerFactory.getEventHandler();
                eventHandler.pushOrderCancel(order);
            }

        }else {
            log.error(order.getStatus().getDesc()+"状态订单不能取消");
        }
    }

    /**
     * 给用户发送订单结算提醒（余额扣款）
     * @param orderId
     */
    public void sendOrderSettleToCustomer(Long orderId) {
        Order order=orderDao.getById(orderId);
        Assert.notNull(order,"订单不存在");
        if (order.getStatus().equals(OrderStatus.PAYED)||order.getStatus().equals(OrderStatus.COMPLETED)){
            //通知客户端
            EventHandler eventHandler=handlerFactory.getEventHandler();
            eventHandler.pushOrderSettle(order);
        }else{
            log.error("订单状态与操作不符");
        }
    }

    /**
     * 向客户端推送订单信息
     * @param orderId
     */
    public void sendOrderCreateToCustomer(Long orderId) {
        Order order=orderDao.getById(orderId);
        Assert.notNull(order,"订单不存在");
        Assert.isTrue(order.getStatus().equals(OrderStatus.WAITING_PAY),ResponseInfo.ORDER_STATUS_ERROR.getMsg());
        Session session=CustomerSessionUtil.getSession(order.getCustomerId()).orElseThrow(()->new BaseException(ResponseInfo.SESSION_NOT_EXIST_ERROR));
        List<OrderCouponDto> customerCoupons=couponFilterService.matchOrderCoupons(order);
        List<OrderDesc> orderDescs=getByOrderId(order.getId());
        if (CollectionUtils.isNotEmpty(orderDescs)){
            orderDescs.stream().forEach(orderDesc -> orderDesc.setBriefPicUrl(cdnDomain+orderDesc.getBriefPicUrl()));
        }
        SocketMsgDto socketMsgDto=SocketMsgDto.orderStage(order,customerCoupons,orderDescs);
        payWebSocket.sendObjectMessage(session, StandardResponse.success(socketMsgDto));
        log.info("通知客户端处理订单创建{}",socketMsgDto);
    }
}
