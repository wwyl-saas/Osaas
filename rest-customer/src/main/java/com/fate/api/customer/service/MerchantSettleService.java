package com.fate.api.customer.service;

import com.fate.common.model.StandardResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers("Accept: application/json")
public interface MerchantSettleService {
    /**
     * 发送订单结算通知
     * @param orderId
     * @return
     */
    @RequestLine("POST /api/server/order/settle?orderId={orderId}")
    StandardResponse sendOrderSettleToUser(@Param("orderId")Long orderId);

    /**
     * 发送订单放弃通知
     * @param orderId
     * @return
     */
    @RequestLine("POST /api/server/order/abandon?orderId={orderId}")
    StandardResponse sendOrderAbandonToUser(@Param("orderId")Long orderId);

    /**
     * 发送订单改变通知
     * @param orderId
     * @return
     */
    @RequestLine("POST /api/server/order/change?orderId={orderId}")
    StandardResponse sendOrderChangeToUser(@Param("orderId")Long orderId);
}
