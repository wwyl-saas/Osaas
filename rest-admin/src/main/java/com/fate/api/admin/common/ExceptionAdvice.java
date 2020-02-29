package com.fate.api.admin.common;

import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;
import com.fate.common.model.StandardResponse;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @program: parent
 * @description: 全局异常处理
 * @author: chenyixin
 * @create: 2019-04-30 19:48
 **/
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public StandardResponse errorHandler(Exception ex) {
        log.error("捕获接口异常", ex);
        if (ex instanceof BaseException) {
            BaseException baseException = (BaseException) ex;
            if (StringUtils.isNotBlank(baseException.getMsg())) {
                return StandardResponse.error(baseException.getMsg());
            } else {
                return StandardResponse.error(baseException.getResponseInfo());
            }
        } else if (ex instanceof MissingServletRequestParameterException) {//参数缺失
            return StandardResponse.error(ResponseInfo.INVALID_PARAM);
        } else if (ex instanceof MethodArgumentNotValidException) {//参数无效，请求参数违反约束
            return StandardResponse.error(createMessage((MethodArgumentNotValidException) ex));
        } else if (ex instanceof BindException) {//绑定失败，如表单对象参数违反约束
            return StandardResponse.error(ResponseInfo.INVALID_PARAM);
        } else if (ex instanceof TypeMismatchException) {//参数类型不匹配
            return StandardResponse.error(ResponseInfo.INVALID_PARAM);
        } else if (ex instanceof HttpMessageNotReadableException) {
            return StandardResponse.error(ResponseInfo.INVALID_PARAM);
        } else if (ex instanceof WxErrorException) {
            return StandardResponse.error(ResponseInfo.WX_ERROR);
        } else if (ex instanceof IllegalArgumentException) {
            return StandardResponse.error(ex.getMessage());
        }else if(ex instanceof HttpRequestMethodNotSupportedException){
            return StandardResponse.error(ResponseInfo.METHOD_ERROR);
        }
        return StandardResponse.error(ResponseInfo.SERVICE_UNAVAILABLE);

    }

    /**
     * 组装校验报错信息
     * @param ex
     * @return
     */
    private String createMessage(MethodArgumentNotValidException ex){
        BindingResult result = ex.getBindingResult();
        String message = "";
        //组装校验错误信息
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            StringBuffer errorMsgBuffer = new StringBuffer();
            for (ObjectError error : list) {
                errorMsgBuffer = errorMsgBuffer.append(error.getDefaultMessage() + ",");
            }
            //返回信息格式处理
            message = errorMsgBuffer.toString().substring(0, errorMsgBuffer.length() - 1);
        }
        return message;
    }

}
