package com.fate.api.customer.dto;

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
 * @description: 预约-服务列表
 * @author: chenyixin
 * @create: 2019-06-06 12:11
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentServiceDto implements Serializable {
    @ApiModelProperty(notes = "分类Id,全部ID为0")
    private Long categoryId;
    @ApiModelProperty(notes = "分类名称")
    private String categoryName;
    @ApiModelProperty(notes = "商品列表")
    private List<GoodsDto> goodsList;
}
