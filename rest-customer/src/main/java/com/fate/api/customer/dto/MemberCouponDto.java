package com.fate.api.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: parent
 * @description: 会员福利优惠券
 * @author: chenyixin
 * @create: 2019-07-29 00:37
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCouponDto {
    @ApiModelProperty("优惠券ID")
    private Long couponId;
    @ApiModelProperty("优惠券名称")
    private String couponName;
    @ApiModelProperty("优惠券数量")
    private Integer number;
    @ApiModelProperty("优惠券tag")
    private String tag;
}
