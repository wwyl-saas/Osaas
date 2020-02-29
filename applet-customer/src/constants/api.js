/**
 * NOTE HOST、HOST_SOCKET 是在 config 中通过 defineConstants 配置的
 * 只所以不在代码中直接引用，是因为 eslint 会报 no-undef 的错误，因此用如下方式处理
 */
/* eslint-disable */
export const host = HOST
export const hostS = HOST_SOCKET
export const applicationId = APPID
/* eslint-enable */

// pic
export const CDN = 'https://www.wanwuyoulian.com'

// user api
// 个人中心页首页
export const API_PERSON = `${host}/api/v1/person/index`
// 获取会员插件参数
export const API_MEMBER_CARD_PLUGIN = `${host}/api/v1/wx/member/card/plugin/param`
// 激活会员卡并刷新个人中心页数据
export const API_ACTIVE_MEMBER = `${host}/api/v1/wx/member/card/active/card`
// 根据类型显示个人账单
export const API_PERSON_BILL = `${host}/api/v1/person/bill`
// 获取会员福利信息
export const API_PERSON_WELFARE = `${host}/api/v1/person/welfare`
// 用户授权登录（用户已注册的情况下直接返回用户信息）
export const API_AUTH_LOGIN = `${host}/api/v1/auth/login`
// 手机号授权获取完整用户信息并重新刷新token
export const API_AUTH_MOBILE_LOGIN = `${host}/api/v1/auth/mobile/login`
// 尝试后台登录（用户已注册的情况下直接返回用户信息）
export const API_AUTH_TRY_LOGIN = `${host}/api/v1/auth/try/login`
// 意见反馈类型
export const API_FEEDBACK_FEEDBACKTYPE = `${host}/api/v1/feedback/feedbacktype/get`
// 提交意见反馈
export const API_FEEDBACK_SUBMIT = `${host}/api/v1/feedback/submit`
// 反馈提交图片
export const API_FEEDBACK_UPLOAD_IMG = `${host}/api/v1/feedback/upload/img`
// 小程序二维码
export const API_MINICODE = `${host}/api/v1/another/pay/generate/minicode`
// 代付小程序码2
export const API_APPLET_CODE = `${host}/image/applet/code`
//商品小程序码
export const API_GOODS_APPLET_CODE = `${host}/image/applet/code/goods`
// 代付二维码2
export const API_ANORHER_PAY_QRCODE = `${host}/image/another/pay/qrcode`
// 代付二维码
export const API_QRCODE = `${host}/api/v1/another/pay/generate/qrcode`
// getMyCoupons
export const API_COUPONS = `${host}/api/v1/person/coupons`
// addCoupon
export const API_COUPONS_ADD = `${host}/api/v1/person/coupons/add`
// 会员充值接口
export const API_MEMBER_CHARGE = `${host}/api/v1/charge/member/charge`
// 查询充值记录微信支付结果
export const API_QUERY_CHARGE = `${host}/api/v1/charge/query`
// 激活会员卡并刷新个人中心页数据
export const API_MEMBER_ACTIVE = `${host}/api/v1/wx/member/card/active/card`
// 获取用户信息
export const API_PERSON_INFO = `${host}/api/v1/person/info`


// index首页 api
// 获取分类商品列表
export const API_INDEX_DATA = `${host}/api/v1/index/data`
// 获取首页轮播图
export const API_INDEX_BANNER = `${host}/api/v1/index/banner`
// 获取门店信息
export const API_INDEX_SHOP = `${host}/api/v1/index/shop`

// product api
// 获取分类商品列表
export const API_CATEGORY_LIST = `${host}/api/v1/category/list`
// 根据关键字搜索信息
export const API_CATEGORY_SEARCH = `${host}/api/v1/category/search`
// 查询商品详情
export const API_GOODS_DETAIL = `${host}/api/v1/goods/detail`
// 查询评论列表
export const API_GOODS_COMMENT = `${host}/api/v1/goods/comment/list`
// 查询评论列表分页
export const API_GOODS_COMMENTS = `${host}//api/v1/goods/comments/list`
// 提交订单评价
export const API_COMPLETE_COMMENT = `${host}/api/v1/order/complete/comment`
// 订单详情
export const API_ORDER_DESC = `${host}/api/v1/order/desc`
// 历史订单
export const API_ORDER_HISTORY = `${host}/api/v1/order/history`
// 提交预约信息接口
export const API_APPOINTMENT_CREATE = `${host}/api/v1/appointment/create`
// 预约页-获取技师列表
export const API_APPOINTMENT_HAIR = `${host}/api/v1/appointment/hairdresser/list`
// 预约列表接口 个人中心-我的预约
export const API_APPOINTMENT_MY = `${host}/api/v1/appointment/my`
// 取消预约接口 个人中心-我的预约-取消
export const API_APPOINTMENT_CANCEL = `${host}/api/v1/appointment/my/cancel`
// 查询服务列表 预约页-获取服务列表
export const API_APPOINTMENT_SERVICE = `${host}/api/v1/appointment/service/list`
// 支付条形码
export const API_PAY_BARCODE = `${host}/api/v1/pay/barcode`
// 支付条形码2
export const API_IMAGE_PAY_BARCODE = `${host}/image/pay/barcode`
// 确认支付
export const API_PAY_CONFIRM = `${host}/api/v1/pay/confirm`
// 放弃支付
export const API_PAY_ABANDON = `${host}/api/v1/pay/abandon`
// 取消订单
export const API_ORDER_CLOSE = `${host}/api/v1/order/close`
// 修改订单优惠券
export const API_ORDER_CHANGE_COUPON = `${host}/api/v1/order/change/coupon`





