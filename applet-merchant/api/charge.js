const { ajax } = require('../utils/request').default
const { sendLink } = require('../utils/config.js').default

class ApiMember {
  constructor() {
  }
  // 这个是封装调用的，可以根据自己的需求做修改
  ajaxData(url, method, dataType, data, success, complete) {
    ajax({
      url: sendLink() + url,
      method: method,
      dataType: dataType,
      data: data,
      success: function (ret) {
        typeof success == 'function' && success.call(this, ret)
      },
      complete: function () {
        if (complete) {
          typeof complete == 'function' && complete.call(this)
        }
      }
    })
  }
  //会员充值
  recharge(data, success) {
    this.ajaxData('/api/recharge/offline', 'POST', '', data, success)
  }
  //会员充值
  getChargeDetail(data, success,complete) {
    this.ajaxData('/api/recharge/detail', 'GET', '', data, success, complete)
  }
}

export default new ApiMember