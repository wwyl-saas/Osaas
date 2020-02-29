package com.fate.api.merchant.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author:
 * @create: 2019-07-22 14:40
 **/
@ApiModel
@Data
public class AdminGoodsMainQuery {
    @ApiModelProperty("商品Id")
    private Long id;

    @ApiModelProperty("商品分类id")
    private Long categoryId;

    @ApiModelProperty("商品名称")
    @NotBlank
    private String name;

    @ApiModelProperty("简图url")
    private String briefPicUrl;

    @ApiModelProperty("市场价格")
    @NotNull
    private BigDecimal marketPrice;

    @ApiModelProperty("专柜价格")
    @NotNull
    private BigDecimal counterPrice;

    @ApiModelProperty("是否推荐")
    @NotNull
    private Boolean isRecommend;

    @ApiModelProperty("销售量")
    @NotNull
    private Integer sellVolume;

    @ApiModelProperty("是否上架")
    @NotNull
    private Boolean onSale;

    @ApiModelProperty("关键字")
    @NotBlank
    private String keywords;

    @ApiModelProperty("主图列表")
    private String primaryPicUrls;

    @ApiModelProperty("详情图列表")
    private String listPicUrls;

    @ApiModelProperty("商品简介")
    @NotBlank
    private String goodsBrief;

    @ApiModelProperty("商品描述")
    @NotNull
    private String goodsDesc;

    @ApiModelProperty("是否热卖")
    @NotNull
    private Boolean isHot;

    @ApiModelProperty("是否新品")
    @NotNull
    private Boolean isNew;
    @ApiModelProperty("简图url")
    private Integer sortOrder;



}
