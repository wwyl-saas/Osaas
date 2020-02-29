package com.fate.api.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fate.common.enums.MemberLevel;
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
 * @description: 会员账户
 * @author: chenyixin
 * @create: 2019-08-06 11:29
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountDto implements Serializable {
    private Long id;
    @ApiModelProperty("用户ID")
    private Long customerId;
    @ApiModelProperty("会员等级")
    private MemberLevel memberLevel;
    @ApiModelProperty("会员积分")
    private Integer consumeScore;
    @ApiModelProperty("会员消费次数")
    private Integer consumeNum;
    @ApiModelProperty("充值次数")
    private Integer chargeNum;
    @ApiModelProperty("会员余额")
    private BigDecimal balance;
    @ApiModelProperty("会员累计充值金额")
    private BigDecimal cumulativeRecharge;
    @ApiModelProperty("会员累计消费金额")
    private BigDecimal cumulativeConsume;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
