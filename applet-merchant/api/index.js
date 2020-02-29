const { ajax } = require('../utils/request').default
const { sendLink } = require('../utils/config.js').default

class ApiIndex {
  constructor() {
  }
  // 这个是封装调用的，可以根据自己的需求做修改
  ajaxData(url, method,dataType, data, success,complete) {
    ajax({
      url: sendLink() + url,
      method: method,
      dataType: dataType,
      data: data,
      success: function (ret) {
        typeof success == 'function' && success.call(this, ret)
      },
      complete: function () {
        if(complete){
          typeof complete == 'function' && complete.call(this)
        }
      }
    })
  }

  // 尝试登录
  tryLogin(data, success,complete) {
    this.ajaxData('/api/auth/try/login', 'POST', 'json', data, success,complete)
  }
  //获取验证码
  getSmsCode(data, success) {
    this.ajaxData('/api/auth/sms/send', 'GET','', data, success)
  }
  // 验证码登录
  login(data, success,complete) {
    this.ajaxData('/api/auth/login/sms', 'POST','json', data, success,complete)
  }
  // 获取门店营业数据
  getShopTodayData(data, success) {
    this.ajaxData('/api/statistic/shop/today/data', 'GET', '', data, success)
  }
  // 获取个人营业数据
  getUserTodayData(data, success) {
    this.ajaxData('/api/statistic/user/today/data', 'GET', '', data, success)
  }
}

export default new ApiIndex