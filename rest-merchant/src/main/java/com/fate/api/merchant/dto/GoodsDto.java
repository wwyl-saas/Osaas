package com.fate.api.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: parent
 * @description: 商品dto
 * @author: chenyixin
 * @create: 2019-06-12 10:06
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDto implements Serializable {
    @ApiModelProperty("商品ID")
    private Long id;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("简图url")
    private String briefPicUrl;

    @ApiModelProperty("主图列表")
    private String primaryPicUrls;

    @ApiModelProperty("详情图列表")
    private String listPicUrls;

    @ApiModelProperty("商品简介")
    private String goodsBrief;

    @ApiModelProperty("商品描述")
    private String goodsDesc;

    @ApiModelProperty("专柜价格")
    private BigDecimal counterPrice;

    @ApiModelProperty("市场价格")
    private BigDecimal marketPrice;

    @ApiModelProperty("是否热卖")
    private Boolean isHot;

    @ApiModelProperty("是否新品")
    private Boolean isNew;

    @ApiModelProperty("是否推荐")
    private Boolean isRecommend;

    @ApiModelProperty("销售量")
    private Integer sellVolume;

    @ApiModelProperty("顺序")
    private Integer sortOrder;

    @ApiModelProperty("关键词")
    private String keywords;

    @ApiModelProperty("是否上架")
    private Boolean onSale;





}
