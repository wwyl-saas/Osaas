const { ajax } = require('../utils/request').default
const { sendLink } = require('../utils/config.js').default

class ApiApplication {
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

  // 获取导航数据
  getNavigatorList(data, success) {
    this.ajaxData('/api/index/navigator/list', 'GET', '', data, success)
  }
  // 获取最近门店
  getNearestShop(data, success) {
    this.ajaxData('/api/index/shop/nearest', 'GET', '', data, success)
  }
  //获取门店列表
  getShopList(success) {
    this.ajaxData('/api/drop/list/shops', 'GET', '', {}, success)
  }
  //获取职称列表
  getPositionList(success) {
    this.ajaxData('/api/drop/list/positions', 'GET', '', {}, success)
  }
  //获取门店角色列表
  getRoleList(success) {
    this.ajaxData('/api/drop/list/shop/roles', 'GET', '', {}, success)
  }
  //获取门店服务列表
  getServiceList(data,success) {
    this.ajaxData('/api/drop/list/shop/services', 'GET', '', data, success)
  }
  //获取会员等级列表
  getMemberLevel(success) {
    this.ajaxData('/api/drop/list/member/level', 'GET', '', {}, success)
  }
  //获取会员来源列表
  getCustomerSource( success) {
    this.ajaxData('/api/drop/list/customer/source', 'GET', '', {}, success)
  }
  // 获取用户
  getUserCurrent(success) {
    this.ajaxData('/api/auth/current', 'GET', '', {}, success)
  }
}

export default new ApiApplication