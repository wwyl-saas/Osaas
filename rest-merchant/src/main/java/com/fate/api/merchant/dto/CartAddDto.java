package com.fate.api.merchant.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: parent
 * @description: 购物车添加
 * @author: chenyixin
 * @create: 2019-05-07 14:58
 **/
@Getter
@Setter
public class CartAddDto implements Serializable {
    @NotNull
    private Integer goodsId;
    @NotNull
    private Integer number;
    @NotNull
    private Integer productId;
}
