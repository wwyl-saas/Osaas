package com.fate.api.merchant.query;

import com.fate.common.cons.Regex;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@ApiModel
@Data
public class PhoneQuery {
    @NotBlank
    @Length(min = 11,max=11,message = "请输入正确的手机号")
    @Pattern(regexp = Regex.PHONE, message = "请输入正确的手机号")
    @ApiModelProperty(notes = "预约人手机号")
    private String phone;
}
