package com.fate.api.customer.exception;

import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;

/**
 * @program: parent
 * @description: 认证异常
 * @author: chenyixin
 * @create: 2019-05-01 22:06
 **/
public class NoAuthException extends BaseException {
    public NoAuthException(){
        super(ResponseInfo.NO_AUTH);
    }
    public NoAuthException(String msg){
        super(msg);
    }
}
