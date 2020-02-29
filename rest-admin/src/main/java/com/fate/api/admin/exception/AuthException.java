package com.fate.api.admin.exception;

import com.fate.common.enums.ResponseInfo;

/**
 * @program: parent
 * @description: 认证异常
 * @author: chenyixin
 * @create: 2019-05-01 22:06
 **/
public class AuthException extends BaseException{
    public AuthException(){
        super(ResponseInfo.TOKEN_ERROR);
    }
    public AuthException(String msg){
        super(msg);
    }
}
