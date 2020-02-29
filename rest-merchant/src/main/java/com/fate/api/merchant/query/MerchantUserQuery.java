package com.fate.api.merchant.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: parent
 * @description:授权登录
 * @author: chenyixin
 * @create: 2019-06-21 11:57
 **/
@Data
@ApiModel
public class MerchantUserQuery {
    @ApiModelProperty("用户id")
    private Long id;
    @NotBlank
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("艺名")
    private String nickname;
    @NotNull
    @ApiModelProperty("职称id")
    private Long postTitleId;
    @NotBlank
    @ApiModelProperty("手机号")
    private String mobile;
    @NotNull
    @ApiModelProperty("角色")
    private Integer roleTypeCode;
    @NotNull
    @ApiModelProperty("门店id")
    private Long shopId;
    @NotNull
    @ApiModelProperty("是否接受预约")
    private Boolean ifAppointment;
    @ApiModelProperty("服务商品列表")
    private List<Long> goodsIds;
}
