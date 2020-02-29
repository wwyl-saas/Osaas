package com.fate.api.customer.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fate.common.cons.Regex;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @program: parent
 * @description: 预约查询对象
 * @author: chenyixin
 * @create: 2019-06-06 09:45
 **/
@ApiModel
@Data
public class  AppointmentCreateQuery implements Serializable {
    @NotNull
    @ApiModelProperty(notes = "表单ID")
    private String formId;
    @NotNull
    @ApiModelProperty(notes = "商铺ID")
    private Long shopId;
    @NotBlank
    @ApiModelProperty(notes = "门店名称")
    private String shopName;
    @NotNull
    @ApiModelProperty(notes = "预约日期")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate arriveDate;
    @NotNull
    @ApiModelProperty(notes = "预约时间")
    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime arriveTime;
    @NotBlank
    @Pattern(regexp = Regex.CHINESE, message = "请输入汉字")
    @ApiModelProperty(notes = "预约人姓名")
    private String customerName;
    @NotBlank
    @Length(min = 11,max=11,message = "请输入正确的手机号")
    @Pattern(regexp = Regex.PHONE, message = "请输入正确的手机号")
    @ApiModelProperty(notes = "预约人手机号")
    private String phone;

    @ApiModelProperty(notes = "预约技师ID")
    private Long merchantUserId;
    @ApiModelProperty(notes = "预约项目ID")
    private Long goodsId;
    @ApiModelProperty(notes = "预约项目名称")
    private String goodsName;

}
