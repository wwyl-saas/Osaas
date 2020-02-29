package com.fate.api.merchant.cons;

/**
 * @program: parent
 * @description: 给商户用户发送的公众号模板
 * @author: chenyixin
 * @create: 2019-06-25 14:52
 **/
public class PublicTemplate {
    /**
     * {{first.DATA}}
     * 反馈人：{{keyword1.DATA}}
     * 反馈时间：{{keyword2.DATA}}
     * 反馈内容：{{keyword3.DATA}}
     * {{remark.DATA}}
     */
    public static final String FEEDBACK_TEMPLATE_ID="SXKnQxJcBo75yO1RSpQtRkdjV9RT7aYgUlY8Uyy0fa4";
    /**
     * 预约创建
     * {{first.DATA}}
     * 客户姓名：{{keyword1.DATA}}
     * 客户手机：{{keyword2.DATA}}
     * 预约时间：{{keyword3.DATA}}
     * 预约内容：{{keyword4.DATA}}
     * {{remark.DATA}}
     */
    public static final String APPOINTMENT_CREATE_TEMPLATE_ID = "viJJfWkVIGnsI9uCy4e0aNADqwzg64cETiPlZNnpuVA";
    /**
     * 预约取消
     * {{first.DATA}}
     * 客户姓名：{{keyword1.DATA}}
     * 客户手机：{{keyword2.DATA}}
     * 预约时间：{{keyword3.DATA}}
     * 预约内容：{{keyword4.DATA}}
     * {{remark.DATA}}
     */
    public static final String APPOINTMENT_CANCEL_TEMPLATE_ID = "Y6xPHzg6Eb2B58AeNdVUK1JrcqFPywq6gWh35DVR-yE";

    /**
     * 支付成功
     * {{first.DATA}}
     * 用户名：{{keyword1.DATA}}
     * 订单号：{{keyword2.DATA}}
     * 订单金额：{{keyword3.DATA}}
     * 商品信息：{{keyword4.DATA}}
     * {{remark.DATA}}
     */
    public static final String PAY_SUCCESS_TEMPLATE_ID = "LVxKpI7PjzAp_zQQjwjfkbrkbNVeh6ZinvJyWapqeD8";
    /**
     * 支付失败
     * {{first.DATA}}
     * 支付金额：{{keyword1.DATA}}
     * 商品信息：{{keyword2.DATA}}
     * 失败原因：{{keyword3.DATA}}
     * {{remark.DATA}}
     */
    public static final String PAY_ERROR_TEMPLATE_ID = "59NmqpwfqaswUt5yaMpX-KMZlevPR2_22yqXNFlgyuE";
    /**
     * 订单取消
     * {{first.DATA}}
     * 订单号：{{keyword1.DATA}}
     * 取消时间：{{keyword2.DATA}}
     * {{remark.DATA}}
     */
    public static final String ORDER_CANCEL_TEMPLATE_ID = "lh5NTwwocxfNGa_RlNnY9hu7voDU6E2_OxoAlL2kJPo";


    /**
     * 充值成功
     * {{first.DATA}}
     * 店铺名称：{{keyword1.DATA}}
     * 会员类型：{{keyword2.DATA}}
     * 充值金额：{{keyword3.DATA}}
     * 当前余额：{{keyword4.DATA}}
     * 充值时间：{{keyword5.DATA}}
     * {{remark.DATA}}
     */
    public static final String CHARGE_TEMPLATE_ID = "nOi2HgEP-srLdrCrMXy_cJE7-Ni0D8cukBaaYDLqLgI";
}
