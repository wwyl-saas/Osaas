package com.fate.api.customer.cons;

/**
 * @program: parent
 * @description: 向客户发送的客户端小程序模板
 * @author: chenyixin
 * @create: 2019-06-25 14:56
 **/
public class WxAppletTemplate {
    /**
     * 支付成功
     * 订单号
     * {{keyword1.DATA}}
     *
     * 门店名称
     * {{keyword2.DATA}}
     *
     * 商品详情
     * {{keyword3.DATA}}
     *
     * 消费金额
     * {{keyword4.DATA}}
     *
     * 减免金额
     * {{keyword5.DATA}}
     *
     * 获取积分
     * {{keyword6.DATA}}
     *
     */
    public static final String PAY_SUCCESS_TEMPLATE_ID = "0L5uZ7PGZdj5ay8Db5Mjz8i6ebDTbHK10mSLYFOG87Q";

    /**
     * 充值成功
     * 充值时间
     * {{keyword1.DATA}}
     *
     * 充值金额
     * {{keyword2.DATA}}
     *
     * 赠送金额
     * {{keyword3.DATA}}
     *
     * 账户余额
     * {{keyword4.DATA}}
     */
    public static final String CHARGE_SUCCESS_TEMPLATE_ID = "nUxlnWnV9Y9h2AH1ljp0RITlbRf1omte97H3L9ofdpk";

    /**
     * 预约确认
     * 预约人
     * {{keyword1.DATA}}
     *
     * 电话
     * {{keyword2.DATA}}
     *
     * 预约门店
     * {{keyword3.DATA}}
     *
     * 预约时间
     * {{keyword4.DATA}}
     *
     * 设计师名称
     * {{keyword5.DATA}}
     *
     * 预约产品
     * {{keyword6.DATA}}
     */
    public static final String APPOINTMENT_CONFIRM_TEMPLATE_ID = "H-lDxfYvrbTqFx3XbUzzN-RiNtD8h1do3YlCHYTbscQ";
    /**
     * 改约
     * 预约时间
     * {{keyword1.DATA}}
     *
     * 门店名称
     * {{keyword2.DATA}}
     *
     * 预约技师
     * {{keyword3.DATA}}
     *
     * 预约项目
     * {{keyword4.DATA}}
     *
     * 备注
     * {{keyword5.DATA}}
     */
    public static final String APPOINTMENT_CHANGE_TEMPLATE_ID = "ZQ0ZyorMgCVHU8-mIe7ZkdxSFiv2P7uV67ZTHyDQD1I";
    /**
     * 预约取消
     * 预约人
     * {{keyword1.DATA}}
     *
     * 原预约时间
     * {{keyword2.DATA}}
     *
     * 预约门店
     * {{keyword3.DATA}}
     *
     * 预约项目
     * {{keyword4.DATA}}
     *
     * 预约技师
     * {{keyword5.DATA}}
     */
    public static final String APPOINTMENT_CANCEL_TEMPLATE_ID = "SMMOZ1hdHHgix1LSD538a8wNGgGryZhnrN3xiTJ-TFY";

    /**
     * 订单取消
     * 订单编号
     * {{keyword1.DATA}}
     *
     * 门店
     * {{keyword2.DATA}}
     *
     * 下单时间
     * {{keyword3.DATA}}
     *
     * 订单金额
     * {{keyword4.DATA}}
     *
     * 商品详情
     * {{keyword5.DATA}}
     *
     */
    public static final String ORDER_CANCEL_TEMPLATE_ID = "HvcLGyhs5M2i6MMC9wgRayYc-ywg_EKWDY1xzSDNVgg";

}
