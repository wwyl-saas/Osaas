package com.fate.api.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fate.common.enums.ConsumeType;
import com.fate.common.enums.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: xudongdong
 * @create: 2019-06-09 12:45
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements Serializable {
    @ApiModelProperty(notes = "订单id")
    private Long id;
    @ApiModelProperty(notes = "订单编号")
    private String orderNo;
    @ApiModelProperty(notes = "订单状态")
    private OrderStatus status;
    @ApiModelProperty(notes = "订单状态Code")
    private Integer statusCode;
    @ApiModelProperty(notes = "商品总数")
    private Integer totalNum;
    @ApiModelProperty(notes = "优惠券折扣")
    private BigDecimal couponAmount;
    @ApiModelProperty(notes = "订单总价")
    private BigDecimal orderSumAmount;
    @ApiModelProperty(notes = "优惠金额")
    private BigDecimal discountAmount;
    @ApiModelProperty(notes = "实付金额")
    private BigDecimal payAmount;
    @ApiModelProperty(notes = "店铺id")
    private Long shopId;
    @ApiModelProperty(notes = "店铺名称")
    private String shopName;
    @ApiModelProperty(notes = "客户名称")
    private String customerName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "服务时间")
    private LocalDateTime createTime;
    @ApiModelProperty(notes = "支付方式")
    private ConsumeType consumeType;
    @ApiModelProperty(notes = "订单详情列表")
    private List<OrderDescDto> orderDescList;

    @ApiModelProperty(notes = "订单内容")
    private String orderDesc;
}
