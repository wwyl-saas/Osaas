package com.fate.api.merchant.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @program: parent
 * @description: 微信登录
 * @author: chenyixin
 * @create: 2019-05-08 09:25
 **/
@Getter
@Setter
public class WxLoginDto implements Serializable {
    @NotBlank
    private String code;
    @NotBlank
    private String encryptedData;
    @NotBlank
    private String iv;
    @NotBlank
    private String rawData;
    @NotBlank
    private String signature;
}

