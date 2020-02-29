package com.fate.api.merchant.dto;

import com.fate.api.merchant.query.GoodsAttributeQuery;
import com.fate.api.merchant.query.GoodsIssueQuery;
import com.fate.common.dao.GoodsShopDao;
import com.fate.common.entity.GoodsShop;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
public class AdminGoodsDto implements Serializable {
    @ApiModelProperty("商品")
    GoodsDto goodDto;
    @ApiModelProperty("商品属性")
    List<AttributeDto> goodsAtrributeList;
    @ApiModelProperty("商品问题")
    List<GoodsIssueDto> goodsIssueList;
    @ApiModelProperty("服务技工列表")
    List<MerchantUserDto>  merchantUserList;
    @ApiModelProperty("服务店铺列表")
    List<MerchantShopDto>  shopList;

}
