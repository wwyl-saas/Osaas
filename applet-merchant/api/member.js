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

  // 获取会员列表
  getCustomerList(data, success) {
    this.ajaxData('/api/customer/list', 'GET', '', data, success)
  }
  // 获取会员详情
  getDetail(data, success) {
    this.ajaxData('/api/customer/detail', 'GET', '', data, success)
  }
  //会员充值
  recharge(data,success) {
    this.ajaxData('/api/recharge/offline', 'POST', '', data, success)
  }
}

export default new ApiMember