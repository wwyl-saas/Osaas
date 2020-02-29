package com.fate.api.merchant.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: parent
 * @description: 收藏
 * @author: chenyixin
 * @create: 2019-05-07 14:34
 **/
@Getter
@Setter
public class CollectDto implements Serializable {
    @NotNull(message = "类型ID不能为null")
    private Integer typeId;
    @NotNull(message = "商品ID不能为null")
    private Integer valueId;
}
