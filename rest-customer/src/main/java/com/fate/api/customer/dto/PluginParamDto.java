package com.fate.api.customer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: parent
 * @description: 插件跳转参数
 * @author: chenyixin
 * @create: 2019-09-09 12:33
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginParamDto {
    @ApiModelProperty(notes = "加密 card_id，传入前须 urldecode")
    @JsonProperty("encrypt_card_id")
    String encryptCardId;
    @ApiModelProperty(notes = "会员卡领取渠道值，会在卡券领取事件回传给商户")
    @JsonProperty("outer_str")
    String outerStr;
    @ApiModelProperty(notes = "商户公众号标识参数，传入前须 urldecode")
    @JsonProperty("biz")
    String biz;

}
