package com.fate.common.enums;

import lombok.Getter;

@Getter
public enum  SmsEnum {

    CAPTCHA("MYVEx1", "【优联美业】尊敬的用户：您的短信验证码为$code，请在$minutes分钟内输入，若非本人操作，请忽略。");

    SmsEnum(String tplId, String context){
        this.tplId = tplId;
        this.context = context;
    }
    /**
     * 短信模板id
     */
    private String tplId;

    /**
     * 描述
     */
    private String context;

}
