package com.fate.api.merchant.exception;

import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;

/**
 * @program: parent
 * @description: 认证异常
 * @author: chenyixin
 * @create: 2019-05-01 22:06
 **/
public class AuthException extends BaseException {
    public AuthException(){
        super(ResponseInfo.TOKEN_ERROR);
    }
    public AuthException(String msg){
        super(msg);
    }
}
