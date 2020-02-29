package com.fate.api.customer.exception;

import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;

/**
 * @program: parent
 * @description: socket session异常
 * @author: chenyixin
 * @create: 2019-05-27 15:53
 **/
public class NoSessionException extends BaseException {
    public NoSessionException(){
        super(ResponseInfo.NO_SOCKET_SESSION);
    }
    public NoSessionException(String msg){
        super(msg);
    }
}
