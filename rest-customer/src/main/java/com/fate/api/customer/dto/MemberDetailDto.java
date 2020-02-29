package com.fate.api.customer.dto;

import com.fate.common.entity.Coupon;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: parent
 * @description: 会员详情
 * @author: chenyixin
 * @create: 2019-06-07 17:13
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailDto implements Serializable {
    @ApiModelProperty("会员等级")
    private Integer memberLevel;
    @ApiModelProperty("会员名称")
    private String memberName;
    @ApiModelProperty("会员卡图片URL")
    private String memberCardUrl;
    @ApiModelProperty("会员卡标记，已升级，待升级")
    private String cardTag;
    @ApiModelProperty("未解锁福利")
    private List<AttributeDto> unlockedFares;
    @ApiModelProperty("已解锁福利")
    private List<AttributeDto> lockedFares;
    @ApiModelProperty("是否已领礼包")
    private Boolean couponFlag;
    @ApiModelProperty("礼包信息")
    private List<MemberCouponDto> coupons;
    @ApiModelProperty("等级要求标签")
    private String levelConditionTag;
    @ApiModelProperty("等级要求属性")
    private List<AttributeDto> levelCondition;
    @ApiModelProperty("按钮名称，为空时不显示")
    private String chargeButton;
    @ApiModelProperty("充值金额")
    private String chargeAmount;

}
