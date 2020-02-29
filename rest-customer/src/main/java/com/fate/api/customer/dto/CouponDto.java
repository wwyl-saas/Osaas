package com.fate.api.customer.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
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

/**
 * @program: parent
 * @description: 优惠券
 * @author: chenyixin
 * @create: 2019-07-20 11:44
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {
    @ApiModelProperty("卡券ID")
    private Long id;

    @ApiModelProperty("C端用户ID")
    private Long customerId;

    @ApiModelProperty("商户ID")
    private Long merchantId;

    @ApiModelProperty("来源应用ID")
    private Long merchantAppId;

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

    @ApiModelProperty("折扣比例")
    private BigDecimal discountRatio;

    @ApiModelProperty("剩余次数")
    private Integer residueNum;

    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty("结束时间")
    private LocalDate endTime;

    @ApiModelProperty("生效规则json字符串")
    private String effectRule;

    @ApiModelProperty("状态")
    private CouponStatus status;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
