package com.fate.api.customer.handler;

import com.fate.api.customer.dto.PayDto;
import com.fate.common.entity.CustomerCharge;

public interface ChargeHandler {
    PayDto preparePay(CustomerCharge charge);

    Boolean queryChargeResult(CustomerCharge charge);
}
