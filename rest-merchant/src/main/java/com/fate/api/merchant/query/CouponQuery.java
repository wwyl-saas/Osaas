package com.fate.api.merchant.query;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fate.common.enums.CouponType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @program: parent
 * @description:
 * @author:
 * @create:
 **/
@ApiModel
@Data
public class CouponQuery {

    @ApiModelProperty("优惠券id")
    private Long id;

    @ApiModelProperty("优惠券类型")
    @NotNull
    private CouponType type;


    @ApiModelProperty("优惠券名称")
    @NotBlank
    private String name;

    @ApiModelProperty("优惠券描述")
    @NotBlank
    private String description;


    @ApiModelProperty("优惠券标注")
    @NotBlank
    private String tag;


    @ApiModelProperty("折扣金额")
    @NotNull
    private BigDecimal discountAmount;


    @ApiModelProperty("折扣比例")
    @NotNull
    private Integer discountRatio;


    @ApiModelProperty("发放总数量")
    @NotNull
    private Integer issueNum;


    @ApiModelProperty("剩余次数")
    @NotNull
    private Integer residueNum;


    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate endTime;


    @ApiModelProperty("生效规则json字符串")
    private String effectRule;

    @ApiModelProperty("状态")
    @NotNull
    private Boolean enable;

    @ApiModelProperty("发行规则")
    @NotNull
    private String issueRule;



}
