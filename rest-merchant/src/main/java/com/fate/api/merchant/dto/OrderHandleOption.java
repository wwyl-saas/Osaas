package com.fate.api.merchant.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-05-07 18:53
 **/
@Getter
@Setter
public class OrderHandleOption implements Serializable {
    private boolean cancel;// 取消操作
    private boolean delete;// 删除操作
    private boolean pay;// 支付操作
    private boolean comment;// 评论操作
    private boolean delivery;// 确认收货操作
    private boolean confirm;// 完成订单操作
    private boolean refund;// 退换货操作
    private boolean buy;// 再次购买
}
