package com.fate.api.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fate.common.enums.ApplicationType;
import com.fate.common.enums.Gender;
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
 * @description: 用户信息相关
 * @author: chenyixin
 * @create: 2019-06-11 13:37
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto implements Serializable {
    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("商户ID")
    private Long merchantId;

    @ApiModelProperty("用户手机号")
    private String mobile;

    @ApiModelProperty("用户名称")
    private String name;

    @ApiModelProperty("别名")
    private String nickname;

    @ApiModelProperty("所属城市")
    private Integer cityId;

    @ApiModelProperty("性别")
    private Gender gender;

    @ApiModelProperty("头像")
    private String avatar;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("最后登录时间")
    private LocalDateTime lastLoginTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(notes = "用户会员号")
    private String memberId;
    @ApiModelProperty(notes = "用户余额")
    private BigDecimal balance;
    @ApiModelProperty(notes = "用户积分")
    private Integer consumeScore;
    @ApiModelProperty("会员消费次数")
    private Integer consumeNum;
    @ApiModelProperty("充值次数")
    private Integer chargeNum;
    @ApiModelProperty("会员累计充值金额")
    private BigDecimal cumulativeRecharge;
    @ApiModelProperty("会员累计消费金额")
    private BigDecimal cumulativeConsume;
    @ApiModelProperty(notes = "会员级别ID")
    private Integer memberLevelCode;
    @ApiModelProperty(notes = "会员级别")
    private MemberLevel memberLevel;


}
