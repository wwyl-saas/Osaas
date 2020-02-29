package com.fate.api.merchant.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @program: parent
 * @description: 购物车勾选
 * @author: chenyixin
 * @create: 2019-05-06 17:29
 **/
@Getter
@Setter
public class CartCheckedDto implements Serializable {
    @NotNull(message = "商品ID不能为空！")
    private List<Integer> productIds;
    @NotNull(message = "勾选项不能为空")
    private Boolean isChecked;
}
