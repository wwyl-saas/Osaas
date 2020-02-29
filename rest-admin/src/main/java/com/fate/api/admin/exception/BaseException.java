package com.fate.api.admin.exception;

import com.fate.common.enums.ResponseInfo;
import lombok.Getter;

/**
 * @program: parent
 * @description: ApiException的基类
 * @author: chenyixin
 * @create: 2019-04-30 23:07
 **/
@Getter
public class BaseException extends RuntimeException{
    private ResponseInfo responseInfo;
    private String msg;

    public BaseException(ResponseInfo responseInfo){
        super(responseInfo.getMsg());
        this.responseInfo = responseInfo;
    }

    public BaseException(String msg){
        super(msg);
        this.msg=msg;
    }
}
