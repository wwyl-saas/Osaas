package com.fate.api.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: parent
 * @description: 商品详情
 * @author: chenyixin
 * @create: 2019-06-08 17:38
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDetailDto implements Serializable {
    @ApiModelProperty("主图列表")
    private List<String> primaryPics;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品简介")
    private String goodsDesc;
    @ApiModelProperty("会员价")
    private BigDecimal counterPrice;
    @ApiModelProperty("市场价")
    private BigDecimal marketPrice;
    @ApiModelProperty("商品标签")
    private List<String> goodsTags;
    @ApiModelProperty("商品规格")
    private List<AttributeDto> goodsAttributes;
    @ApiModelProperty("适用门店")
    private List<MerchantShopDto> merchantShops;
    @ApiModelProperty("评论数量")
    private Integer commentNum;
    @ApiModelProperty("最近评论")
    private CommentDto lastComment;
    @ApiModelProperty("商品详情图")
    private List<String> detailPics;
    @ApiModelProperty("常见问题")
    private List<GoodsIssueDto> goodsIssues;
}
