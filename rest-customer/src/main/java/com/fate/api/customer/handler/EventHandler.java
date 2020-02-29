package com.fate.api.customer.handler;

import com.fate.common.entity.*;
import com.github.binarywang.wxpay.exception.WxPayException;

public interface EventHandler {

    void pushQueryPaySuccess(Order order);

    void pushNotifyPaySuccess(Order order);

    void pushQueryChargeSuccess(CustomerCharge charge);

    void pushNotifyChargeSuccess(CustomerCharge charge);

    void pushOrderCancel(Order order) throws WxPayException;

    void pushOrderSettle(Order order);

    void pushCustomerAppointmentConfirmMsg(CustomerAppointment appointment);

    void pushCustomerAppointmentChangeMsg(CustomerAppointment appointment);

    void pushCustomerAppointmentCancelMsg(CustomerAppointment appointment);
}
