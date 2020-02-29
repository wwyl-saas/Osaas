import {
  CATEGORY_LIST,
  CATEGORY_SEARCH,
  GOODS_COMMENT,
  GOODS_COMMENTS,
  GOODS_DETAIL,
  INDEX_DATA,
  INDEX_SHOP,
  INDEX_BANNER,
  APPOINTMENT_CREATE,
  APPOINTMENT_HAIR,
  APPOINTMENT_SERVICE,
  APPOINTMENT_MY,
  APPOINTMENT_DATA,
  APPOINTMENT_CANCEL,
  ORDER_HISTORY,
  SETTLE_DATA,
  PAY_BARCODE,
  PAY_CONFIRM,
  SET_LAT_LONG,
  SELECTED_SHOP,
  PAY_ABANDON,
  ORDER_CLOSE,
  COMPLETE_COMMENT,
  ORDER_DESC,
  ORDER_CHANGE_COUPON,
  PERSON_INFO
} from '../constants/product'
import {
  API_CATEGORY_LIST,
  API_CATEGORY_SEARCH,
  API_GOODS_COMMENT,
  API_GOODS_COMMENTS,
  API_GOODS_DETAIL,
  API_INDEX_DATA,
  API_INDEX_SHOP,
  API_INDEX_BANNER,
  API_APPOINTMENT_CREATE,
  API_APPOINTMENT_HAIR,
  API_APPOINTMENT_SERVICE,
  API_APPOINTMENT_MY,
  API_APPOINTMENT_CANCEL,
  API_ORDER_HISTORY,
  API_PAY_BARCODE,
  API_PAY_CONFIRM,
  API_MINICODE,
  API_PAY_ABANDON,
  API_ORDER_CLOSE,
  API_COMPLETE_COMMENT,
  API_ORDER_DESC,
  API_ORDER_CHANGE_COUPON,
  API_PERSON_INFO
} from '../constants/api'
import {
  MINICODE,
} from '../constants/users'
import { createAction, setData } from '../utils/redux'


/**
 * 获取用户信息
 * @param {*} payload
 */
export const dispatchPersonInfo = () => createAction({
  url: API_PERSON_INFO,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  method: 'GET',
  type: PERSON_INFO
})

/**
 * 修改优惠券
 * @param {*} payload
 */
export const dispatchOrderChangeCoupon = payload => createAction({
  url: API_ORDER_CHANGE_COUPON,
  fetchOptions: {
    showToast: true,
    autoLogin: false
  },
  method: 'POST',
  dataType: 'www-form',
  type: ORDER_CHANGE_COUPON,
  payload
})


/**
 * 订单详情
 * @param {*} payload
 */
export const dispatchOrderDesc = payload => createAction({
  url: API_ORDER_DESC,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  method: 'GET',
  type: ORDER_DESC,
  payload
})

/**
 * 提交评价
 * @param {*} payload
 */
export const dispatchCompleteComment = payload => createAction({
  url: API_COMPLETE_COMMENT,
  fetchOptions: {
    showToast: true,
    autoLogin: false
  },
  method: 'POST',
  type: COMPLETE_COMMENT,
  payload
})


/**
 * 取消订单
 * @param {*} payload
 */
export const dispatchOrderClose = payload => createAction({
  url: API_ORDER_CLOSE,
  fetchOptions: {
    showToast: true,
    autoLogin: false
  },
  method: 'POST',
  dataType: 'www-form',
  type: ORDER_CLOSE,
  payload
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
 * 保存已经选择的门店
 * @param {*} payload
 */
export const dispatchSelectedShop = payload => setData({
  type: SELECTED_SHOP,
  payload
})

/**
 * 保存定位坐标
 * @param {*} payload
 */
export const dispatchSetLatLong = payload => setData({
  type: SET_LAT_LONG,
  payload
})

/**
 * 放弃支付
 * @param {*} payload
 */
export const dispatchPayAbandon = payload => createAction({
  url: API_PAY_ABANDON,
  fetchOptions: {
    showToast: true,
    autoLogin: false
  },
  type: PAY_ABANDON,
  dataType: 'www-form',
  method: 'POST',
  payload
})

/**
 * 确认支付
 * @param {*} payload
 */
export const dispatchPayConfirm = payload => createAction({
  url: API_PAY_CONFIRM,
  fetchOptions: {
    showToast: true,
    autoLogin: false
  },
  type: PAY_CONFIRM,
  method: 'POST',
  payload
})

/**
 * 支付条形码
 * @param {*} payload
 */
export const dispatchPayBarcode = payload => createAction({
  url: API_PAY_BARCODE,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: PAY_BARCODE,
  method: 'GET',
  payload
})

/**
 * 结算页面数据
 * @param {*} payload
 */
export const dispatchSettleData = payload => setData({
  type: SETTLE_DATA,
  payload
})

/**
 * 历史订单
 * @param {*} payload
 */
export const dispatchOrderHistory = payload => createAction({
  url: API_ORDER_HISTORY,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: ORDER_HISTORY,
  method: 'GET',
  payload
})


/**
 * 预约页-我的预约 数据
 * @param {*} payload
 */
export const dispatchSetAppointmentData = payload => setData({
  type: APPOINTMENT_DATA,
  payload
})

/**
 * 预约页-我的预约, 取消
 * @param {*} payload
 */
export const dispatchAppointmentCancle = payload => createAction({
  url: API_APPOINTMENT_CANCEL,
  fetchOptions: {
    showToast: true,
    autoLogin: false
  },
  type: APPOINTMENT_CANCEL,
  dataType: 'www-form',
  method: 'POST',
  payload
})

/**
 * 预约页-我的预约
 * @param {*} payload
 */
export const dispatchAppointmentMy = payload => createAction({
  url: API_APPOINTMENT_MY,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: APPOINTMENT_MY,
  method: 'GET',
  payload
})

/**
 * 预约页-获取服务列表
 * @param {*} payload
 */
export const dispatchAppointmentServer = payload => createAction({
  url: API_APPOINTMENT_SERVICE,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: APPOINTMENT_SERVICE,
  method: 'GET',
  payload
})

/**
 * 预约页-获取技师列表
 * @param {*} payload
 */
export const dispatchAppointmentHair = payload => createAction({
  url: API_APPOINTMENT_HAIR,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: APPOINTMENT_HAIR,
  method: 'GET',
  payload
})

/**
 * 提交预约信息接口
 * @param {*} payload
 */
export const dispatchAppointmentSearch = payload => createAction({
  url: API_APPOINTMENT_CREATE,
  fetchOptions: {
    showToast: true,
    autoLogin: false
  },
  type: APPOINTMENT_CREATE,
  method: 'POST',
  payload
})

/**
 * 获取分类商品列表
 * @param {*} payload
 */
export const dispatchCategoryList = () => createAction({
  url: API_CATEGORY_LIST,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: CATEGORY_LIST
})

/**
 * 根据关键字搜索信息
 * @param {*} payload
 */
export const dispatchCategorySearch = (payload) => createAction({
  url: API_CATEGORY_SEARCH,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: CATEGORY_SEARCH,
  payload
})

/**
 * 查询商品详情
 * @param {*} payload
 */
export const dispatchGoodsDetail = (payload) => createAction({
  url: API_GOODS_DETAIL,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: GOODS_DETAIL,
  payload
})

/**
 * 查询评论列表
 * @param {*} payload
 */
export const dispatchGoodsComment = (payload) => createAction({
  url: API_GOODS_COMMENT,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: GOODS_COMMENT,
  payload
})

/**
 * 查询评论列表分页
 * @param {*} payload
 */
export const dispatchGoodsComments = (payload) => createAction({
  url: API_GOODS_COMMENTS,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: GOODS_COMMENTS,
  payload
})

/**
 * 获取首页商品信息
 * @param {*} payload
 */
export const dispatchIndexData = (payload) => createAction({
  url: API_INDEX_DATA,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: INDEX_DATA,
  payload
})

/**
 * 商铺信息
 * @param {*} payload
 */
export const dispatchIndexShop = (payload) => createAction({
  url: API_INDEX_SHOP,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: INDEX_SHOP,
  payload
})

/**
 * banner图
 * @param {*} payload
 */
export const dispatchIndexBanner = (payload) => createAction({
  url: API_INDEX_BANNER,
  fetchOptions: {
    showToast: false,
    autoLogin: false
  },
  type: INDEX_BANNER,
  payload
})


