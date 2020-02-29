package com.fate.api.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: parent
 * @description: 会员福利
 * @author: chenyixin
 * @create: 2019-07-29 15:31
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberWelfareDto {
    @ApiModelProperty("积分")
    private Integer consumeScore;
    @ApiModelProperty("累计充值")
    private BigDecimal cumulativeRecharge;
    @ApiModelProperty("消费次数")
    private Integer consumeNum;
    @ApiModelProperty("当前会员等级")
    private Integer currentLevel;
    @ApiModelProperty("会员详情")
    private List<MemberDetailDto> detailDtoList;
}
