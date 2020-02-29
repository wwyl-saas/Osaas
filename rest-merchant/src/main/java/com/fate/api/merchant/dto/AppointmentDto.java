package com.fate.api.merchant.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fate.common.enums.AppointmentStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @program: parent
 * @description: 预约
 * @author: chenyixin
 * @create: 2019-06-12 23:43
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto implements Serializable {
    @ApiModelProperty(value = "预约信息ID")
    private Long id;

    @ApiModelProperty("商铺名称")
    private String shopName;

    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty("到达日期")
    private LocalDate arriveDate;

    @JsonFormat(pattern="HH:mm")
    @ApiModelProperty("到达时间")
    private LocalTime arriveTime;

    @JsonFormat(pattern="HH:mm")
    @ApiModelProperty("到达时间起始")
    private LocalTime arriveTimeStart;

    @JsonFormat(pattern="HH:mm")
    @ApiModelProperty("到达时间终止")
    private LocalTime arriveTimeEnd;

    @ApiModelProperty("预约人名称")
    private String appointName;

    @ApiModelProperty("预约人手机号")
    private String appointMobile;

    @ApiModelProperty("预约人头像")
    private String appointAvatar;

    @ApiModelProperty("商户用户名称")
    private String merchantUserName;

    @ApiModelProperty("商户用户ID")
    private Long merchantUserId;

    @ApiModelProperty("服务项目ID")
    private Long goodsId;

    @ApiModelProperty("服务项目名称")
    private String goodsName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("状态")
    private AppointmentStatus statusName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;




}
