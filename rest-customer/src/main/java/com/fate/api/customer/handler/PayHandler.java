package com.fate.api.customer.handler;

import com.fate.api.customer.dto.PayDto;
import com.fate.common.entity.Order;
import com.fate.common.enums.ConsumeType;
import com.github.binarywang.wxpay.exception.WxPayException;

public interface PayHandler {
    ConsumeType getPayType();

    PayDto preparePay(Order order);

    Boolean queryPayResult(Order order);

    void cancelPay(Order order) throws WxPayException;
}
