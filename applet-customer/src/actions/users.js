import {
  PERSON_INDEX,
  PERSON_BILL,
  PERSON_WELFARE,
  AUTH_TRY_LOGIN,
  AUTH_LOGIN,
  FEEDBACK_SUBMIT,
  FEEDBACK_UPLOAD_IMG,
  FEEDBACK_FEEDBACKTYPE,
  MINICODE,
  QRCODE,
  COUPONS,
  COUPONS_ADD,
  MEMBER_CHARGE,
  QUERY_CHARGE,
  AUTHORIZED,
  MEMBER_ACTIVE,
  APPLET_CODE
} from '../constants/users'
import {
  API_PERSON,
  API_MEMBER_CARD_PLUGIN,
  API_ACTIVE_MEMBER,
  API_PERSON_BILL,
  API_PERSON_WELFARE,
  API_AUTH_TRY_LOGIN,
  API_AUTH_LOGIN,
  API_FEEDBACK_SUBMIT,
  API_FEEDBACK_UPLOAD_IMG,
  API_FEEDBACK_FEEDBACKTYPE,
  API_MINICODE,
  API_QRCODE,
  API_COUPONS,
  API_COUPONS_ADD,
  API_MEMBER_CHARGE,
  API_QUERY_CHARGE,
  API_MEMBER_ACTIVE,
  API_APPLET_CODE
} from '../constants/api'
import { createAction, setData } from '../utils/redux'

/**
 * 万能接口请求，但参数需要全部
 * @param {*} payload
 */
export const dispatchAllCan = options => createAction({
  url: options.url,
  fetchOptions: {
    showToast: options.showToast || true,
    autoLogin: options.autoLogin,
  },
  method: options.method || 'GET',
  dataType: options.dataType || 'json',
  type: options.type,
  payload: options.payload || ''
})

/**
 * 激活会员卡并刷新个人中心页数据
 * @param {*} payload
 */
export const dispatchMemberActive = payload => createAction({
  url: API_MEMBER_ACTIVE,
  fetchOptions: {
    showToast: true,
    autoLogin: false
  },
  method: 'POST',
  dataType: 'www-form',
  type: MEMBER_ACTIVE,
  payload
})

/**
 * 保存是否授权
 * @param Boolean payload
 */
export const dispatchAuthorized = payload => setData({
  type: AUTHORIZED,
  payload
})

/**
 * 会员充值后查询结果
 * @param {*} payload
 */
export const dispatchQueryCharge = payload => createAction({
  url: API_QUERY_CHARGE,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  method: 'GET',
  type: QUERY_CHARGE,
  payload
})

/**
 * 会员充值
 * @param {*} payload
 */
export const dispatchMemberCharge = payload => createAction({
  url: API_MEMBER_CHARGE,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  method: 'POST',
  dataType: 'www-form',
  type: MEMBER_CHARGE,
  payload
})


/**
 * 优惠添加
 * @param {*} payload
 */
export const dispatchCouponsAdd = payload => createAction({
  url: API_COUPONS_ADD,
  fetchOptions: {
    showToast: true,
    autoLogin: false
  },
  method: 'GET',
  type: COUPONS_ADD,
  payload
})

/**
 * 优惠
 * @param {*} payload
 */
export const dispatchCoupons = payload => createAction({
  url: API_COUPONS,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  method: 'GET',
  type: COUPONS,
  payload
})

/**
 * 代付二维码
 * @param {*} payload
 */
export const dispatchQrcode = payload => createAction({
  url: API_QRCODE,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  method: 'GET',
  type: QRCODE,
  payload
})

/**
 * 代付小程序码
 * @param {*} payload
 */
// eslint-disable-next-line import/export
export const dispatchAppletCode = () => createAction({
  url: API_APPLET_CODE,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  method: 'GET',
  type: APPLET_CODE
})

/**
 * 小程序码
 * @param {*} payload
 */
export const dispatchMinicode = payload => createAction({
  url: API_MINICODE,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  method: 'GET',
  type: MINICODE,
  payload
})

/**
 * 个人反馈类型
 * @param {*} payload
 */
export const dispatchFeedbackFeedbackType = payload => createAction({
  url: API_FEEDBACK_FEEDBACKTYPE,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  method: 'GET',
  type: FEEDBACK_FEEDBACKTYPE,
  payload
})

/**
 * 个人反馈提交
 * @param {*} payload
 */
export const dispatchFeedbackSubmit = payload => createAction({
  url: API_FEEDBACK_SUBMIT,
  fetchOptions: {
    showToast: true,
    autoLogin: true
  },
  method: 'POST',
  type: FEEDBACK_SUBMIT,
  payload
})

/**
 * 个人反馈提交 图片
 * @param {*} payload
 */
export const dispatchFeedbackUpload = payload => createAction({
  url: API_FEEDBACK_UPLOAD_IMG,
  fetchOptions: {
    showToast: true,
    autoLogin: false
  },
  method: 'POST',
  type: FEEDBACK_UPLOAD_IMG,
  payload
})

/**
 * 个人中心页首页
 * @param {*} payload
 */
export const dispatchPersonIndex = () => createAction({
  url: API_PERSON,
  fetchOptions: {
    showToast: true,
    autoLogin: true
  },
  type: PERSON_INDEX
})

/**
 * 获取会员卡插件参数
 * @param {*} payload
 */
export const dispatchPluginParam = payload => createAction({
  url: API_MEMBER_CARD_PLUGIN,
  fetchOptions: {
    showToast: true,
    autoLogin: true
  },
  type: PERSON_INDEX,
  payload
})


/**
 * 激活会员卡并获取最新数据
 * @param {*} payload
 */
export const dispatchActiveMemberCard = payload => createAction({
  url: API_ACTIVE_MEMBER,
  fetchOptions: {
    showToast: true,
    autoLogin: true
  },
  method: 'POST',
  type: PERSON_INDEX,
  payload
})

/**
 * 根据类型显示个人账单
 * @param {*} payload
 */
export const dispatchPersonBill = payload => createAction({
  url: API_PERSON_BILL,
  fetchOptions: {
    showToast: true,
    autoLogin: false
  },
  type: PERSON_BILL,
  payload
})

/**
 * 获取会员福利信息
 * @param {*} payload
 */
export const dispatchPersonWelfare = () => createAction({
  url: API_PERSON_WELFARE,
  fetchOptions: {
    showToast: true,
    autoLogin: false
  },
  type: PERSON_WELFARE
})

/**
 * 尝试后台登录（用户已注册的情况下直接返回用户信息）
 * @param {*} payload
 */
export const dispatchAuthTryLogin = payload => createAction({
  url: API_AUTH_TRY_LOGIN,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  method: 'POST',
  type: AUTH_TRY_LOGIN,
  payload
})

/**
 * 用户授权登录（用户已注册的情况下直接返回用户信息）
 * @param {*} payload
 */
export const dispatchAuthLogin = payload => createAction({
  url: API_AUTH_LOGIN,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  method: 'POST',
  type: AUTH_LOGIN,
  payload
})


