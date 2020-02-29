package com.fate.api.merchant.exception;

import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;

/**
 * @program: parent
 * @description: 订单状态异常
 * @author: chenyixin
 * @create: 2019-05-28 10:11
 **/
public class OrderStatusException extends BaseException {
    public OrderStatusException() {
        super(ResponseInfo.ORDER_STATUS_ERROR);
    }

    public OrderStatusException(String msg) {
        super(msg);
    }
}
