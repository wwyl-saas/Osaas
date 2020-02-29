package com.fate.api.merchant.dto;

import com.fate.common.entity.Order;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;

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
    private Integer stage;
    private String orderId;
    private Boolean result;
    private BigDecimal sumAmount;
    private BigDecimal couponAmount;
    private BigDecimal discountAmount;
    private BigDecimal payAmount;

    /**
     * 发送订单信息
     * @param order
     * @return
     */
    public static SocketMsgDto orderStage(Order order,Boolean result,Integer stage){
        SocketMsgDto socketMsgDto=new SocketMsgDto();
        socketMsgDto.setStage(stage);
        socketMsgDto.setOrderId(order.getId().toString());
        socketMsgDto.setSumAmount(order.getOrderSumAmount());
        socketMsgDto.setCouponAmount(order.getCouponAmount());
        socketMsgDto.setDiscountAmount(order.getDiscountAmount());
        socketMsgDto.setPayAmount(order.getPayAmount());
        socketMsgDto.setResult(result);
        return socketMsgDto;
    }
}
