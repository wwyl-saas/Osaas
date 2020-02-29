package com.fate.api.merchant.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: parent
 * @description: 分类页
 * @author: chenyixin
 * @create: 2019-06-04 23:28
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements Serializable {
    @ApiModelProperty(notes = "分类ID")
    private Long id;
    @ApiModelProperty(notes = "分类名称")
    private String name;
    @ApiModelProperty(notes = "分类显示排序")
    private Integer sortOrder;
    @ApiModelProperty(notes = "图标")
    private String icon;
    @ApiModelProperty(notes = "图标")
    private String iconOn;
    @ApiModelProperty(notes = "是否启用")
    private Boolean enable;
    @ApiModelProperty(notes = "分类商品列表")
    private List<GoodsDto> goodsList;
}
