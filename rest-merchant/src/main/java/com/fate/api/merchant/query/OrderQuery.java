package com.fate.api.merchant.query;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class OrderQuery {
    @ApiModelProperty("商品Id")
    @NotNull
    private Long orderId;

    @ApiModelProperty("商品Id")
    @NotNull
    private Integer  orderStatus;

    @ApiModelProperty("商品Id")
    @NotNull
    private String orderNo;
}
