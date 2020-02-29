package com.fate.common.model;

import com.fate.common.enums.ResponseInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: parent
 * @description: Rest统一返回信息格式
 * @author: chenyixin
 * @create: 2019-04-30 23:15
 **/

@Getter
@Setter
@ToString
public class StandardResponse<T> implements Serializable {
    private int code;
    private String msg;
    private Long time;
    private T data;

    private StandardResponse(){
        super();
    }

    private StandardResponse(ResponseInfo responseInfo, T data){
        super();
        this.code=responseInfo.getCode();
        this.msg=responseInfo.getMsg();
        this.time=System.currentTimeMillis();
        this.data=data;
    }

    private StandardResponse(ResponseInfo responseInfo, String msg, T data){
        super();
        this.code=responseInfo.getCode();
        this.msg=msg;
        this.time=System.currentTimeMillis();
        this.data=data;
    }

    /**
     * 成功，无数据
     * @return
     */
    public static StandardResponse success(){
        return new StandardResponse(ResponseInfo.SUCCESS,null);
    }

    /**
     * 成功，有数据
     * @param data
     * @return
     */
    public static StandardResponse success(Object data){
        return new StandardResponse(ResponseInfo.SUCCESS,data);
    }

    /**
     * 错误，有错误定义
     * @param responseInfo
     * @return
     */
    public static StandardResponse error(ResponseInfo responseInfo){
        return new StandardResponse(responseInfo,null);
    }

    /**
     * 错误，无错误定义
     * @param msg
     * @return
     */
    public static StandardResponse error(String msg){
        return new StandardResponse(ResponseInfo.UNDEFINE_ERROR,msg,null);
    }
}
