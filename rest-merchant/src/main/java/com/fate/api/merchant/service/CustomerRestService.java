package com.fate.api.merchant.service;

import com.fate.common.model.StandardResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers("Accept: application/json")

public interface CustomerRestService {
    /**
     * 发送订单取消
     * @param orderId
     * @param applicationId
     * @return
     */
    @RequestLine("POST /api/server/order/cancel?orderId={orderId}")
    @Headers("applicationId: {applicationId}")
    StandardResponse sendOrderCancelToCustomer(@Param("orderId")Long orderId, @Param("applicationId") Long applicationId);

}
