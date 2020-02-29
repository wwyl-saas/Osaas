const { ajax } = require('../utils/request').default
const { sendLink } = require('../utils/config.js').default

class ApiSettle {
  constructor() {
  }
  // 这个是封装调用的，可以根据自己的需求做修改
  ajaxData(url, method, dataType, data, success,fail, complete) {
    ajax({
      url: sendLink() + url,
      method: method,
      dataType: dataType,
      data: data,
      success: function (ret) {
        typeof success == 'function' && success.call(this, ret)
      },
      fail: function(res){
        if (fail) {
          typeof fail == 'function' && fail.call(this,res)
        }
      },
      complete: function () {
        if (complete) {
          typeof complete == 'function' && complete.call(this)
        }
      }
    })
  }

  // 获取订单列表
  getOrderList(data, success) {
    this.ajaxData('/api/order/list', 'GET', '', data, success)
  }
  // 获取订单详情
  getDetail(data, success) {
    this.ajaxData('/api/order/detail', 'GET', '', data, success)
  }
  //取消订单
  cancelOrder(data, success) {
    this.ajaxData('/api/order/cancel', 'POST', '', data, success)
  }
  // 获取商品列表
  getGoodsList(data, success) {
    this.ajaxData('/api/goods/list', 'GET', '', data, success)
  }
  //余额扣款
  remainderSettle(data, success) {
    this.ajaxData('/api/order/remainder/settle', 'POST', '', data, success)
  }
  //扫码下单
  barcodePlace(data, success, fail) {
    this.ajaxData('/api/order/barcode/place', 'POST', 'json', data, success,fail)
  }
  //代付扫码下单
  qrCodePlace(data, success, fail) {
    this.ajaxData('/api/order/qrcode/place', 'POST', 'json', data, success, fail)
  }
  // 获取门店用户选择列表
  getUserDropList(data, success) {
    this.ajaxData('/api/drop/list/shop/users', 'GET', '', data, success)
  }
}

export default new ApiSettle