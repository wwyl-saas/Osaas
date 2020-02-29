package com.fate.api.customer.query;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * @program: parent
 * @description: 评论查询对象
 * @author: songjinghuan
 * @create: 2019-06-16 09:45
 **/
@ApiModel
@Data
public class CommentCreateQuery implements Serializable {
    @NotNull
    @ApiModelProperty("订单Id")
    private Long orderId;
    @NotEmpty
    @ApiModelProperty(notes = "评价列表")
    List<CommentQuery> queries;
}
