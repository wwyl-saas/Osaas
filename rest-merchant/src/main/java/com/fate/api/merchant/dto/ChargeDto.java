package com.fate.api.merchant.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fate.common.enums.ChargeStatus;
import com.fate.common.enums.ChargeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: parent
 * @description: 充值记录
 * @author: chenyixin
 * @create: 2019-08-24 10:51
 **/

@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeDto implements Serializable {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("商铺ID")
    private Long shopId;
    @ApiModelProperty("商铺名称")
    private String shopName;
    @ApiModelProperty("商户用户ID")
    private Long merchantUserId;
    @ApiModelProperty("商户用户")
    private String merchantUserName;
    @ApiModelProperty("C端用户ID")
    private Long customerId;
    @ApiModelProperty("C端用户头像")
    private String customerAvatar;
    @ApiModelProperty("C端用户名称")
    private String customerName;
    @ApiModelProperty("充值类型")
    private ChargeType chargeType;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("返赠金额")
    private BigDecimal giftAmount;

    @ApiModelProperty("充值状态")
    private ChargeStatus chargeStatus;

    @ApiModelProperty("操作用户ID")
    private Long operatorId;
    @ApiModelProperty("代操作用户")
    private String operatorName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
