package com.fate.api.merchant.query;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @program: parent
 * @description:
 * @author:
 * @create: 2019-07-22 14:40
 **/
@ApiModel
@Data
public class GoodsAttributeQuery {

    @ApiModelProperty("商品属性ID")
    private Long id;

    @ApiModelProperty("产品ID")
    private Long goodsId;

    @ApiModelProperty("属性名称")
    @NotBlank
    private String name;

    @ApiModelProperty("属性值")
    @NotBlank
    private String value;

}
