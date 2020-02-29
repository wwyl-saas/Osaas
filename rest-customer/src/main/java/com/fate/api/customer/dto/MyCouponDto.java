package com.fate.api.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: parent
 * @description: 优惠券列表
 * @author: chenyixin
 * @create: 2019-07-20 11:49
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyCouponDto {
    @ApiModelProperty(notes = "优惠券列表")
    private List<CouponDto> coupons;
    @ApiModelProperty(notes = "次卡列表")
    private List<CardDto> cards;
}
