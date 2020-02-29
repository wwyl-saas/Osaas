package com.fate.common.enums;

import lombok.Getter;

/**
 * 接口返回信息
 */
@Getter
public enum ResponseInfo {
    SUCCESS(0,"成功"),
    UNDEFINE_ERROR(999,"未定义异常"),//自己改写报错信息
    INVALID_PARAM(1000,"请求参数缺失或不正确"),
    PARAM_TOO_BIG(1001,"批量查询不得超过10条记录"),
    THRID_ERROR(1002,"第三方接口异常"),
    DATA_IS_EMPTY(1003,"暂无数据"),
    AUTH_CODE_ERROR(1004,"生产验证码异常"),
    WX_ERROR(1005,"请求微信接口异常"),
    WX_VALIDATE_ERROR(1006,"微信认证参数校验失败"),
    NO_SOCKET_SESSION(1007,"socket session不存在"),
    INTERFACE_ERROR(1008,"接口异常"),
    PARAM_NULL(1009,"参数为空"),
    WX_CANCEL_ERROR(1010,"微信取消订单错误"),


    METHOD_ERROR(1012,"方法不匹配"),
    SEND_SMS_ERROR(1013,"发送短信失败"),
    PROCESS_ERROR(1014,"处理消息失败"),
    ENUM_ERROR(1015,"枚举值不存在"),
    NON_GOODS(2001,"商品已下架"),
    LESS_GOODS(2002,"库存不足"),
    NON_ORDER(2003,"订单不存在"),
    ORDER_STATUS_ERROR(2004,"订单状态与当前操作不匹配"),
    COUPON_STATUS_ERROR(2005,"卡券状态与当前操作不匹配"),
    IMAGE_TOO_LARGE_1M(2006,"图片过大，请控制在1MB以内"),

    WX_CODE_SESSION_ERROR(3001,"微信code to session异常"),


    TOKEN_ERROR(4001,"无效的认证信息，请重新登录"),
    NO_AUTH(4002,"微信用户未授权"),
    NO_MEMBER(4003,"您还未领取会员卡不能查看"),
    USER_NONE(4006,"用户不存在或禁用"),
    USER_UNAUTH(4010,"用户未配置权限"),

    PHONE_NUM_ERROR(4011,"手机号错误"),
    SMSCAPTCHA_OVER_LIMIT(4012,"短信发送过于频繁，请稍后再试"),
    WX_BAND_PHONE(4013,"手机号有误"),
    USERNAME_OR_PWD_ERROR(4014,"手机号或密码错误"),
    MESSAGE_CODE_EXPIRE(4015,"验证码已过期"),
    SMS_CODE_ERROR(4016,"验证码错误"),
    PSW_CONFIRMPSW_NOTEQ(4017,"密码不一致"),

    SERVICE_UNAVAILABLE(5000,"服务异常，请稍后再试"),
    TIMEOUT_UNAVAILABLE(5001,"超时异常"),
    SESSION_INFO_ERROR(5002,"长连接信息异常"),
    SESSION_EXIST_ERROR(5003,"已存在长连接"),
    SESSION_NOT_EXIST_ERROR(5004,"不存在长连接");
    private int code;
    private String msg;

    ResponseInfo(int status, String msg){
        this.code = status;
        this.msg = msg;
    }
}
