package com.fate.api.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: parent
 * @description: 订单列表封装对象
 * @author: chenyixin
 * @create: 2019-08-21 17:00
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListDto implements Serializable {
    @ApiModelProperty("总数")
    private Integer total;
    @ApiModelProperty("待支付")
    private Integer waitingPay;
    @ApiModelProperty("已支付")
    private Integer payed;
    @ApiModelProperty("已取消")
    private Integer canceled;
    @ApiModelProperty("分页列表")
    private PageDto<OrderDto> pageList;
}
