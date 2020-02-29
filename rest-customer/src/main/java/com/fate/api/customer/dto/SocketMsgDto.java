package com.fate.api.customer.dto;

import com.fate.common.entity.Order;
import com.fate.common.entity.OrderDesc;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: parent
 * @description: websocket的消息对象
 * @author: chenyixin
 * @create: 2019-05-26 12:20
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocketMsgDto implements Serializable {
    //阶段  1 返回二维码条形码阶段  2.返回订单信息阶段
    private Integer stage;
    private String barcode;

    private String orderId;
    private BigDecimal sumAmount;
    private BigDecimal couponAmount;
    private BigDecimal discountAmount;

    private String couponId;
    private List<OrderCouponDto> coupons;
    private Long usableCoupons;
    private List<OrderDesc> orderDescs;

    private Integer totalNum;
    private BigDecimal payAmount;

    /**
     * 第一阶段的信息，一维码发送
     * @param barcode
     * @return
     */
    public static SocketMsgDto barcodeStage(Long barcode){
        SocketMsgDto socketMsgDto=new SocketMsgDto();
        socketMsgDto.setStage(1);
        socketMsgDto.setBarcode(String.valueOf(barcode));
        return socketMsgDto;
    }


    /**
     * 第二阶段的信息，发送订单全部信息
     * @param order
     * @param coupons
     * @param orderDescs
     * @return
     */
    public static SocketMsgDto orderStage(Order order, List<OrderCouponDto> coupons, List<OrderDesc> orderDescs){
        SocketMsgDto socketMsgDto=new SocketMsgDto();
        socketMsgDto.setStage(2);
        socketMsgDto.setOrderId(String.valueOf(order.getId()));
        socketMsgDto.setSumAmount(order.getOrderSumAmount());
        socketMsgDto.setCouponAmount(order.getCouponAmount());
        socketMsgDto.setDiscountAmount(order.getDiscountAmount());
        socketMsgDto.setCouponId(String.valueOf(order.getCouponId()));
        socketMsgDto.setCoupons(coupons);
        socketMsgDto.setUsableCoupons(0L);
        if (CollectionUtils.isNotEmpty(coupons)){
            socketMsgDto.setUsableCoupons(coupons.stream().filter(orderCouponDto -> orderCouponDto.isMatch()).count());
        }
        socketMsgDto.setOrderDescs(orderDescs);
        socketMsgDto.setPayAmount(order.getPayAmount());
        socketMsgDto.setTotalNum(order.getTotalNum());
        return socketMsgDto;
    }
}
