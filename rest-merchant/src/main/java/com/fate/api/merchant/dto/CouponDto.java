package com.fate.api.merchant.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fate.common.enums.CouponType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * @program: parent
 * @description:
 * @author:
 * @create:
 **/
@ApiModel
@Data
public class CouponDto {
    @ApiModelProperty("优惠券id")
    private Long id;

    @ApiModelProperty("优惠券类型")
    private CouponType type;


    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("优惠券描述")
    private String description;


    @ApiModelProperty("优惠券标注")
    private String tag;


    @ApiModelProperty("折扣金额")
    private BigDecimal discountAmount;


    @ApiModelProperty("折扣比例")
    private BigDecimal discountRatio;


    @ApiModelProperty("剩余次数")
    private Integer residueNum;


    @ApiModelProperty("开始时间")
    private LocalDate startTime;


    @ApiModelProperty("结束时间")
    private LocalDate endTime;


    @ApiModelProperty("生效规则json字符串")
    private String effectRule;


    @ApiModelProperty("发放总数量")
    private Integer issueNum;

    @ApiModelProperty("状态")
    private Boolean enable;


    @ApiModelProperty("发行规则")
    @NotNull
    private String issueRule;






}
