package com.fate.api.customer.dto;

import com.fate.common.enums.ConsumeType;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: parent
 * @description: 支付dto
 * @author: chenyixin
 * @create: 2019-06-17 21:43
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayDto implements Serializable {
    @ApiModelProperty(notes = "是否完成支付")
    private boolean pay;
    @ApiModelProperty(notes = "消费支付类型")
    private ConsumeType type;
    @ApiModelProperty(notes = "查询支付结果凭证")
    private Long payId;
    @ApiModelProperty(notes = "时间戳")
    private String timeStamp;
    @ApiModelProperty(notes = "随机字符串")
    private String nonceStr;
    @ApiModelProperty(notes = "签名算法")
    private String signType;
    @ApiModelProperty(notes = "签名")
    private String paySign;
    @ApiModelProperty(notes = "package参数")
    private String packageValue;
}
