package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.api.merchant.config.feign.FeignConfiguration;
import com.fate.api.merchant.dto.*;
import com.fate.api.merchant.endpoint.SettleWebSocket;
import com.fate.api.merchant.exception.OrderStatusException;
import com.fate.api.merchant.query.GoodsQuery;
import com.fate.api.merchant.query.OrderCreateQuery;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.api.merchant.util.OrderNoUtil;
import com.fate.api.merchant.util.UserSessionUtil;
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
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 订单相关
 * @author: chenyixin
 * @create: 2019-07-22 13:38
 **/
@Service
@Slf4j
public class OrderService {
    private static final String key = "order:pay:";
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Value("${wx.applet.appId}")
    private String appletId;
    @Resource
    OrderDescDao orderDescDao;
    @Resource
    OrderDao orderDao;
    @Autowired
    CacheService cacheService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    PayForAnotherService payForAnotherService;
    @Autowired
    CouponService couponService;
    @Autowired
    CustomerConsumeService customerConsumeService;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    MerchantService merchantService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    MerchantUserService merchantUserService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerRestService customerRestService;
    @Resource
    MerchantShopDao merchantShopDao;
    @Autowired
    CouponFilterService couponFilterService;
    @Resource
    MemberWelfareDao memberWelfareDao;
    @Autowired
    SettleWebSocket settleWebSocket;
    @Autowired
    MqSendService mqSendService;


    /**
     * 扫码下单
     *
     * @param query
     * @return
     */
    public OrderDto barcodePlace(OrderCreateQuery query) {
        String value = (String) cacheService.get(key + query.getCode());
        Assert.notNull(value, "条形码已过期，请刷新后再试");
        String[] array = value.split("-");
        Assert.isTrue(array.length > 1, "条形码有误");
        Long customerId = Long.parseLong(array[0]);
        Long merchantApplicationId = Long.parseLong(array[1]);

        List<Long> goodsIds = query.getGoods().stream().map(goodsQuery -> goodsQuery.getId()).collect(Collectors.toList());
        Map<Long, Goods> goodsMap = goodsService.getGoodsByIds(goodsIds).stream().collect(Collectors.toMap(Goods::getId, goods -> goods, (goods1, goods2) -> goods1));
        Order order=createOrder(query,customerId,merchantApplicationId,goodsMap);

        String existIp = (String) cacheService.get(RedisKey.CUSTOMER_SOCKET_KEY + customerId);
        Assert.isTrue(StringUtils.isNotBlank(existIp),"客户端连接不存在");
        CustomerSettleService settleService= FeignConfiguration.getCustomerSettleService(existIp);
        StandardResponse standardResponse=settleService.sendOrderCreateToCustomer(order.getId(),merchantApplicationId);
        Assert.isTrue(standardResponse.getCode()==0,standardResponse.getMsg());
        OrderDto result=BeanUtil.mapper(order, OrderDto.class);
        return result;
    }

    /**
     * 会员代付下单
     *
     * @param query
     * @return
     */
    public OrderDto qrCodePlace(OrderCreateQuery query) {
        PayForAnother payForAnother = payForAnotherService.getByPayCode(query.getCode());
        Assert.notNull(payForAnother, "无可用支付码");

        List<Long> goodsIds = query.getGoods().stream().map(goodsQuery -> goodsQuery.getId()).collect(Collectors.toList());
        Map<Long, Goods> goodsMap = goodsService.getGoodsByIds(goodsIds).stream().collect(Collectors.toMap(Goods::getId, goods -> goods, (goods1, goods2) -> goods1));
        Order order = createOrder(query,payForAnother.getPayCustomerId(),payForAnother.getApplicationId(),goodsMap);

        CustomerAccount customerAccount = customerAccountService.findByCustomerId(order.getCustomerId());
        Assert.notNull(customerAccount, "订单用户账户不存在");
        Assert.isTrue(order.getPayAmount().compareTo(customerAccount.getBalance()) <= 0, "余额不足");
        remainderPayForAnother(order,customerAccount,payForAnother);


        MyMessage message=new MyMessage(order.getId());
        MqProperties mqProperties=MqProperties.builder().merchantId(CurrentMerchantUtil.getMerchant().getId()).delay(10).messageTag(MessageTag.ORDER_SETTLE.getTag()).build();
        mqSendService.sendToCustomer(message,mqProperties);
        return BeanUtil.mapper(order, OrderDto.class);
    }


    /**
     * 求金额合计
     *
     * @param goodsMap
     * @param goods
     * @return
     */
    private BigDecimal accumulatorAmount(Map<Long, Goods> goodsMap, List<GoodsQuery> goods) {
        return goods.stream().map(goodsQuery -> {
            return goodsMap.get(goodsQuery.getId()).getCounterPrice().multiply(BigDecimal.valueOf(goodsQuery.getGoodsNum()));
        }).reduce((a, b) -> a.add(b)).get();
    }


    /**
     * 计算order的商品总价、折扣金额、
     * @param order
     */
    private void caculateOrder(Order order, CustomerCoupon customerCoupon) {
        //会员折扣金额
        CustomerAccount customerAccount=customerAccountService.findByCustomerId(order.getCustomerId());
        Assert.notNull(customerAccount,"用户会员卡未激活");
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
                BigDecimal afterCouponAmount=afterMemberDiscount.multiply(BigDecimal.valueOf(customerCoupon.getDiscount()/100D)).setScale(2,BigDecimal.ROUND_HALF_UP);//百分值转换
                couponAmout=afterMemberDiscount.subtract(afterCouponAmount);
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
     * 连接商品名称
     *
     * @param goodsMap
     * @param goodsList
     * @return
     */
    private String joinGoodsNames(Map<Long, Goods> goodsMap, List<GoodsQuery> goodsList) {
        List<String> goosNames = goodsList.stream().map(goodsQuery -> {
            Goods goods = goodsMap.get(goodsQuery.getId());
            Assert.notNull(goods, "商品不存在");
            return goods.getName();
        }).collect(Collectors.toList());
        return Joiner.on(",").join(goosNames);
    }


    /**
     * 创建订单
     * @param query
     * @param customerId
     * @param merchantApplicationId
     * @param goodsMap
     * @return
     */
    @Transactional
    public Order createOrder(OrderCreateQuery query,Long customerId,Long merchantApplicationId, Map<Long, Goods> goodsMap){
        String existIp = (String) cacheService.get(RedisKey.CUSTOMER_SOCKET_KEY + customerId);
        Assert.isTrue(StringUtils.isNotBlank(existIp),"客户端连接不存在");
        //创建订单
        Order order = new Order();
        order.setStatus(OrderStatus.WAITING_PAY);
        order.setTotalNum(query.getGoods().stream().mapToInt(GoodsQuery::getGoodsNum).sum());
        order.setShopId(query.getShopId());
        order.setOrderNo(OrderNoUtil.getOrderNo(customerId));
        order.setMerchantUserId(query.getServiceUserId());
        order.setChecker(CurrentUserUtil.getMerchantUser().getId());
        order.setMerchantAppId(merchantApplicationId);
        order.setCustomerId(customerId);
        order.setGoodsNames(joinGoodsNames(goodsMap, query.getGoods()));
        order.setOrderSumAmount(accumulatorAmount(goodsMap, query.getGoods()));
        Assert.isTrue(order.insert(), "数据插入失败");

        //插入订单详情
        query.getGoods().stream().forEach(goodsQuery -> {
            Goods goods = goodsMap.get(goodsQuery.getId());
            OrderDesc orderDesc = new OrderDesc();
            orderDesc.setOrderId(order.getId());
            orderDesc.setName(goods.getName());
            orderDesc.setGoodsId(goodsQuery.getId());
            orderDesc.setMarketPrice(goods.getMarketPrice());
            orderDesc.setGoodsNum(goodsQuery.getGoodsNum());
            orderDesc.setGoodsBrief(goods.getGoodsBrief());
            orderDesc.setCounterPrice(goods.getCounterPrice());
            orderDesc.setBriefPicUrl(goods.getBriefPicUrl());
            Assert.isTrue(orderDesc.insert(), "数据插入失败");
        });

        CustomerCoupon customerCoupon=couponFilterService.matchOrderCoupon(order);
        caculateOrder(order,customerCoupon);
        order.setUpdateTime(null);
        Assert.isTrue(orderDao.updateById(order),"数据更新失败");
        return order;
    }



    /**
     * 余额支付-完成订单支付状态
     *
     * @param order
     * @param customerAccount
     * @return
     */
    @Transactional
    protected void remainderPayForAnother(Order order, CustomerAccount customerAccount, PayForAnother payForAnother) {
        //更改账户信息
        customerAccountService.consumeByBalance(order.getPayAmount(),customerAccount);
        //修改订单状态
        order.setStatus(OrderStatus.PAYED);
        Assert.isTrue(order.updateById(), "数据更新失败");

        //修改优惠券状态
        couponService.consumeCoupon(order.getCouponId());
        //增加消费记录
        Long consumeId = customerConsumeService.createConsumeLog(order, ConsumeType.REMAINDER);

        //修改代付记录
        payForAnother.setConsumeId(consumeId);
        payForAnother.setPayAmount(order.getPayAmount());
        Assert.isTrue(payForAnother.updateById(), "数据更新失败");

    }

    /**
     * 获取订单列表
     *
     * @param shopId
     * @param date
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public OrderListDto getOrderList(Long shopId, LocalDate date, OrderStatus orderStatus, String customerPhone, Long pageIndex, Long pageSize) {
        List<OrderDto> orderDtos = Lists.emptyList();
        PageDto pageDto=new PageDto(pageIndex,pageSize,0,0,orderDtos);
        OrderListDto result = OrderListDto.builder().total(0).waitingPay(0).payed(0).canceled(0).build();

        Long customerId = null;
        if (StringUtils.isNotBlank(customerPhone)) {
            Customer customer = customerService.getCustomerByPhone(customerPhone);
            Assert.notNull(customer, "手机号对应用户不存在");
            customerId = customer.getId();
        }

        Long userId = null;
        UserRoleType roleType = CurrentUserUtil.getMerchantUserRoleType(shopId);
        //查门店所有人员记录
        if (!roleType.equals(UserRoleType.SHOP_OWNER) && !roleType.equals(UserRoleType.MANAGER_ASSISTANT) && !roleType.equals(UserRoleType.MANAGER)) {
            userId = CurrentUserUtil.getMerchantUser().getId();
        }

        Integer total = orderDao.getCountByShopStatusAndCustomer(shopId, customerId, userId, null, date);
        result.setTotal(total);
        if (total > 0) {
            Integer waitingPay=orderDao.getCountByShopStatusAndCustomer(shopId,customerId,userId,OrderStatus.WAITING_PAY,date);
            result.setWaitingPay(waitingPay);
            Integer payed=orderDao.getCountByShopStatusAndCustomer(shopId,customerId,userId,OrderStatus.PAYED,date);
            result.setPayed(payed);
            Integer canceled=orderDao.getCountByShopStatusAndCustomer(shopId,customerId,userId,OrderStatus.CANCELED,date);
            result.setCanceled(canceled);
            //根据shopID userId查询订单列表
            IPage<Order> page = orderDao.getByShopAndUserAndCustomerAndDatePage(shopId, userId, orderStatus, customerId, date, pageIndex, pageSize);
            if (CollectionUtils.isNotEmpty(page.getRecords())) {
                orderDtos = page.getRecords().stream().map(order -> {
                    OrderDto orderDto = BeanUtil.mapper(order, OrderDto.class);
                    StringBuilder stringBuilder = new StringBuilder().append(order.getGoodsNames()).append("共").append(order.getTotalNum()).append("件商品");
                    orderDto.setOrderDesc(stringBuilder.toString());
                    orderDto.setStatusCode(order.getStatus().getCode());
                    return orderDto;
                }).collect(Collectors.toList());
            }
            pageDto=new PageDto<>(pageIndex, pageSize, page.getTotal(), page.getPages(), orderDtos);
        }
        result.setPageList(pageDto);
        return result;
    }


    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    public OrderDto getOrderDetail(Long orderId) {
        Order order = orderDao.getById(orderId);
        Assert.notNull(order, "订单不存在");
        OrderDto orderDto = BeanUtil.mapper(order, OrderDto.class);
        Customer customer = customerService.getById(order.getCustomerId());
        if (customer != null) {
            orderDto.setCustomerName(customer.getName());
        }
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
     * 取消订单
     *
     * @param orderId
     * @return
     */
    public void cancle(Long orderId) {
        Order order = orderDao.getById(orderId);
        Assert.notNull(order, "订单不存在");
        if (order.getStatus().equals(OrderStatus.WAITING_PAY)) {
            //调用通知API
            StandardResponse standardResponse=customerRestService.sendOrderCancelToCustomer(orderId,order.getMerchantAppId());
            Assert.isTrue(standardResponse.getCode()==0,standardResponse.getMsg());
        } else {
            throw new OrderStatusException(order.getStatus().getDesc() + "状态订单不能取消");
        }
    }

    /**
     * 余额支付
     *
     * @param orderId
     */
    public OrderDto remainderSettle(Long orderId) {
        Order order = orderDao.getById(orderId);
        Assert.notNull(order, "订单不存在");
        CustomerAccount customerAccount = customerAccountService.findByCustomerId(order.getCustomerId());
        Assert.notNull(customerAccount, "订单用户账户不存在");
        Assert.isTrue(order.getPayAmount().compareTo(customerAccount.getBalance()) <= 0, "余额不足");
        remainderPay(order,customerAccount);

        OrderDto result = BeanUtil.mapper(order, OrderDto.class);
        StringBuilder stringBuilder = new StringBuilder().append(order.getGoodsNames()).append("共").append(order.getTotalNum()).append("件商品");
        result.setOrderDesc(stringBuilder.toString());
        result.setStatusCode(order.getStatus().getCode());
        return result;
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
        customerAccountService.consumeByBalance(order.getPayAmount(),customerAccount);
        //修改订单状态
        order.setStatus(OrderStatus.PAYED);
        Assert.isTrue(order.updateById(), "数据更新失败");

        //修改优惠券状态
        couponService.consumeCoupon(order.getCouponId());
        //增加消费记录
        customerConsumeService.createConsumeLog(order, ConsumeType.REMAINDER);
    }

    /**
     * 查询订单
     *
     * @param id
     * @return
     */
    public Order getById(Long id) {
        return orderDao.getById(id);
    }


    /**
     * 会员代付
     * @param
     * @return
     */
    public boolean memberWithhold(Long orderId,Long memerId){
        Order order= getById(orderId);
        Assert.notNull(order,"订单不存在");
        CustomerAccount customerAccount = customerAccountService.findByCustomerId(memerId);
        Assert.notNull(customerAccount,"会员不存在");
        if (customerAccount.getBalance().compareTo(order.getPayAmount()) >= 0) {
            customerAccountService.consumeByBalance(order.getPayAmount(),customerAccount);
            //修改订单状态
            order.setStatus(OrderStatus.PAYED);
            Assert.isTrue(order.updateById(), "数据更新失败");

            //修改优惠券状态
            couponService.consumeCoupon(order.getCouponId());
            //增加消费记录
            customerConsumeService.createConsumeLog(order, ConsumeType.REMAINDER);
            return true;
        }
        return false;
    }

    /**
     * 根据订单id或者订单编号，订单状态查询订单主信息
     * @param orderId
     * @param orderNo
     * @param orderStatus
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageDto getOrderByStatusAndOrderNoAndId(Long orderId, String orderNo, Integer orderStatus, Long pageIndex, Long pageSize) {

        IPage<Order> page;
        page=orderDao.getOrderByStatusAndOrderNoAndIdPage(orderId,orderNo,orderStatus,pageIndex,pageSize);

        List<Order> orders=new ArrayList<Order>();
        if (page!=null && CollectionUtils.isNotEmpty(page.getRecords())) {
            orders = page.getRecords();
        }
        List<Long> shopIdsList=new ArrayList<Long>();
        List<Long> customerIdsList=new ArrayList<Long>();
        for(Order order:orders){
            shopIdsList.add(order.getShopId());
            customerIdsList.add(order.getCustomerId());
        }
        List<Map<String,Object>> shopId2NameMap= merchantShopDao.getshopNamesByIds(shopIdsList);
        List<Map<String,Object>> customerId2NameMap= customerService.getCustmerNamesByIds(customerIdsList);


        List<OrderDto> orderDtos= Lists.emptyList();

        if (CollectionUtils.isNotEmpty(page.getRecords())){
            orderDtos=page.getRecords().stream().map(order -> {
                OrderDto orderDto=BeanUtil.mapper(order,OrderDto.class);
                StringBuilder stringBuilder=new StringBuilder().append(order.getGoodsNames()).append("共").append(order.getTotalNum()).append("件商品");
                orderDto.setOrderDesc(stringBuilder.toString());
                orderDto.setStatusCode(order.getStatus().getCode());
                for(Map<String,Object> map:shopId2NameMap){
                    String shopId= order.getShopId().toString();
                    if(map.get("id").toString().equals(shopId)){
                        orderDto.setShopName(map.get("name").toString());

                    }
                }
                if(null==orderDto.getShopName() || orderDto.getShopName().equals("")){
                    orderDto.setShopName("");
                }

                for(Map<String,Object> map:customerId2NameMap){
                    String customerId= order.getCustomerId().toString();
                    if(map.get("id").toString().equals(customerId)){
                        orderDto.setCustomerName(map.get("name").toString());

                    }
                }
                if(null==orderDto.getCustomerName() || orderDto.getCustomerName().equals("")){
                    orderDto.setCustomerName("");
                }

                return orderDto;
            }).collect(Collectors.toList());
        }
        return new PageDto<>(pageIndex,pageSize,page.getTotal(),page.getPages(),orderDtos);


    }

    public Integer getTodayOrderCount(Long shopId) {
        return orderDao.getOrderCountBy(shopId,LocalDate.now(),null);
    }

    public Integer getTodayOrderCount(Long shopId, Long userId) {
        return orderDao.getOrderCountBy(shopId,LocalDate.now(),userId);
    }


    public List<Long> getOrderIdsByShopAndUserAndDate(Long shopId, Long userId, LocalDate date) {
        return orderDao.getOrderIdsByShopAndUserAndDate(shopId,userId,date);
    }

    /**
     * 根据订单ID查询订单集合分页
     * @param orderIds
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public IPage<Order> getPageByOrderIds(List<Long> orderIds, Long userId, Integer pageIndex, Integer pageSize) {
        return orderDao.getPageByOrderIds(orderIds,userId,pageIndex,pageSize);
    }

    public IPage<Order> getPageByShopAndDate(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize) {
        return orderDao.getPageBy(shopId,userId,date,pageIndex,pageSize);
    }


    /**
     * 订单结算推送商户端订单结算结果
     * @param orderId
     */
    public void sendOrderSettleToUser(Long orderId) {
        Order order=getById(orderId);
        Assert.notNull(order,"订单不能为空");
        Session session=UserSessionUtil.getSession(order.getMerchantUserId()).orElseThrow(()->new BaseException(ResponseInfo.SESSION_NOT_EXIST_ERROR));
        Assert.isTrue(order.getStatus().equals(OrderStatus.PAYED_ERROR) || order.getStatus().equals(OrderStatus.PAYED),ResponseInfo.ORDER_STATUS_ERROR.getMsg());
        SocketMsgDto socketMsgDto=SocketMsgDto.orderStage(order,order.getStatus().equals(OrderStatus.PAYED),2);
        settleWebSocket.sendObjectMessage(session,socketMsgDto);
        log.info("长连接通知前端处理{}",socketMsgDto);
    }

    /**
     * 订单结算推送商户端订单放弃支付
     * @param orderId
     */
    public void sendOrderAbandonToUser(Long orderId) {
        Order order=getById(orderId);
        Assert.notNull(order,"订单不能为空");
        Session session=UserSessionUtil.getSession(order.getMerchantUserId()).orElseThrow(()->new BaseException(ResponseInfo.SESSION_NOT_EXIST_ERROR));
        Assert.isTrue(order.getStatus().equals(OrderStatus.WAITING_PAY),ResponseInfo.ORDER_STATUS_ERROR.getMsg());
        SocketMsgDto socketMsgDto=SocketMsgDto.orderStage(order,false,1);
        settleWebSocket.sendObjectMessage(session,socketMsgDto);
        log.info("长连接通知前端处理{}",socketMsgDto);
    }

    /**
     * 推送商户端订单变化
     * @param orderId
     */
    public void sendOrderChangeToUser(Long orderId) {
        Order order=getById(orderId);
        Assert.notNull(order,"订单不能为空");
        Session session=UserSessionUtil.getSession(order.getMerchantUserId()).orElseThrow(()->new BaseException(ResponseInfo.SESSION_NOT_EXIST_ERROR));
        Assert.isTrue(order.getStatus().equals(OrderStatus.WAITING_PAY),ResponseInfo.ORDER_STATUS_ERROR.getMsg());
        SocketMsgDto socketMsgDto=SocketMsgDto.orderStage(order,false,0);
        settleWebSocket.sendObjectMessage(session,socketMsgDto);
        log.info("长连接通知前端处理{}",socketMsgDto);
    }


}
