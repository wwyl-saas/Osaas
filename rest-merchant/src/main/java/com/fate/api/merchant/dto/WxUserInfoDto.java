package com.fate.api.merchant.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: parent
 * @description: 微信
 * @author: chenyixin
 * @create: 2019-05-08 10:33
 **/
@Getter
@Setter
public class WxUserInfoDto implements Serializable {
    @JSONField(name = "avatarUrl")
    private String avatarUrl;
    private String birthday;
    @JSONField(name = "openId")
    private String openId;
    @JSONField(name = "nickName")
    private String nickName;
    @JSONField(name = "gender")
    private Integer gender;
    @JSONField(name = "city")
    private String city;
    @JSONField(name = "province")
    private String province;
    @JSONField(name = "country")
    private String country;
    @JSONField(name = "unionId")
    private String unionId;
}