const { ajax } = require('../utils/request').default
const { sendLink } = require('../utils/config.js').default

class ApiStatistic {
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

  // 获取统计数据
  getCustomerList(data, success) {
    this.ajaxData('/api/customer/list', 'GET', '', data, success)
  }

  // 获取门店统计明细数据
  getShopStatisticList(data, success, complete) {
    this.ajaxData('/api/statistic/shop/list', 'GET', '', data, success,complete)
  }
  // 获取门店统计趋势图数据
  getShopStatisticChart(data, success, complete) {
    this.ajaxData('/api/statistic/shop/chart', 'GET', '', data, success, complete)
  }
  // 获取门店用户统计明细数据
  getUserStatisticList(data, success, complete) {
    this.ajaxData('/api/statistic/user/list', 'GET', '', data, success, complete)
  }
  // 获取门店用户统计趋势图数据
  getUserStatisticChart(data, success, complete) {
    this.ajaxData('/api/statistic/user/chart', 'GET', '', data, success, complete)
  }


}

export default new ApiStatistic