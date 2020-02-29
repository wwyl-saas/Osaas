package com.fate.api.merchant.service;

import com.fate.common.model.StandardResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @program: parent
 * @description: 客户端结算消息
 * @author: chenyixin
 * @create: 2019-09-23 00:00
 **/
@Headers("Accept: application/json")
public interface CustomerSettleService {

    /**
     * 发送订单创建
     * @param orderId
     * @param applicationId
     * @return
     */
    @RequestLine("POST /api/server/order/create?orderId={orderId}")
    @Headers("applicationId: {applicationId}")
    StandardResponse sendOrderCreateToCustomer(@Param("orderId")Long orderId, @Param("applicationId") Long applicationId);
}
