package com.fate.api.merchant.query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * @program: parent
 * @description:
 * @author:
 * @create: 2019-07-22 14:40
 **/
@ApiModel
@Data
public class AdminGoodsQuery {

    @ApiModelProperty("商品信息")
    AdminGoodsMainQuery goodsMainQuery;

    @ApiModelProperty("商品属性")
    @NotEmpty
    List<GoodsAttributeQuery> goodsAttributeList;

    @ApiModelProperty("商品问题")
    @NotEmpty
    List<GoodsIssueQuery> goodsIssueList;


    @ApiModelProperty("服务对应的技师id列表")
    @NotEmpty
    private List<Long>  merchantUserList;

    @ApiModelProperty("服务对应的商铺id列表")
    @NotEmpty
    private List<Long> shopList;




}
