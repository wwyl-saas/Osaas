package com.fate.api.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: parent
 * @description: 卡项
 * @author: chenyixin
 * @create: 2019-09-13 15:53
 **/
@ApiModel
@Data
public class CardDto {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("C端用户ID")
    private Long customerId;

    @ApiModelProperty("商户ID")
    private Long merchantId;

    @ApiModelProperty("来源应用ID")
    private Long merchantAppId;

    @ApiModelProperty("次卡类型")
    private Integer type;

    @ApiModelProperty("次卡类型")
    private String name;

    @ApiModelProperty("次卡详情")
    private String description;

    @ApiModelProperty("此卡标注")
    private String tag;

    @ApiModelProperty("折扣金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("折扣比例")
    private Integer discountRatio;

    @ApiModelProperty("剩余次数")
    private Integer residueNum;

    @ApiModelProperty("次卡有效次数")
    private Integer effectNum;

    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    private LocalDate endTime;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("状态Code")
    private Integer statusCode;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
