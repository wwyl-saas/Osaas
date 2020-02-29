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
 * @description: 下单参数
 * @author: chenyixin
 * @create: 2019-07-22 14:19
 **/
@ApiModel
@Data
public class OrderCreateQuery {
    @ApiModelProperty("条形码")
    @NotNull
    private Long code;
    @ApiModelProperty("选择商品")
    @NotEmpty
    private List<GoodsQuery> goods;
    @ApiModelProperty("服务员工")
    @NotNull
    private Long serviceUserId;
    @ApiModelProperty("门店ID")
    @NotNull
    private Long shopId;
}
