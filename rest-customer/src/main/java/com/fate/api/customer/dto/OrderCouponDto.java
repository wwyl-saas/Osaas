package com.fate.api.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fate.common.enums.CouponStatus;
import com.fate.common.enums.CouponType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: parent
 * @description: 订单优惠券
 * @author: chenyixin
 * @create: 2019-09-18 18:49
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCouponDto {
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("C端用户ID")
    private String customerId;

    @ApiModelProperty("商户ID")
    private String merchantId;

    @ApiModelProperty("来源应用ID")
    private String merchantAppId;

    @ApiModelProperty("卡券类型")
    private CouponType type;

    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("优惠券详情")
    private String description;

    @ApiModelProperty("优惠券标注")
    private String tag;

    @ApiModelProperty("折扣金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("折扣比例0-99表示0-9.9折")
    private Integer discountRatio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("开始生效时间")
    private LocalDate startTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("结束生效时间")
    private LocalDate endTime;

    @ApiModelProperty("优惠券状态")
    private CouponStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("create_time")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty("是否匹配")
    private boolean match;
    @ApiModelProperty("不可用原因列表")
    private List<String> reasons;
    
}
