package com.fate.api.customer.dto;

import com.fate.common.entity.Banner;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: parent
 * @description: 首页
 * @author: chenyixin
 * @create: 2019-06-12 11:40
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexDto {
    @ApiModelProperty("新品")
    private IndexGoodsDto newGoods;
    @ApiModelProperty("热销")
    private IndexGoodsDto hotGoods;
    @ApiModelProperty("店家推荐")
    private IndexGoodsDto recommendGoods;
}
