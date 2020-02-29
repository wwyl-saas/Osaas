package com.fate.api.customer.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: parent
 * @description: 评论对象
 * @author: chenyixin
 * @create: 2019-09-14 20:31
 **/
@ApiModel
@Data
public class CommentQuery {
    @NotNull
    @ApiModelProperty(notes = "商品ID")
    private Long goodsId;
    @ApiModelProperty("评论")
    private String remark;
    @ApiModelProperty("评价图地址")
    private String  pictureUrls;
    @NotNull
    @ApiModelProperty("评分")
    private Integer grade;
    @NotNull
    @ApiModelProperty("是否匿名，其中0表示是")
    private Boolean anonymousType;
}
