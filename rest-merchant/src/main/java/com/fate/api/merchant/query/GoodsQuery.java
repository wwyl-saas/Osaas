package com.fate.api.merchant.query;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: parent
 * @description:
 * @author:
 * @create: 2019-07-22 14:40
 **/
@ApiModel
@Data
public class GoodsQuery {
    @ApiModelProperty("商品Id")
    @NotNull
    private Long id;
    @ApiModelProperty("商品数量")
    @NotNull
    private Integer goodsNum;









}
