const { ajax } = require('../utils/request').default
const { sendLink } = require('../utils/config.js').default

class ApiEmployee {
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

  // 获取员工列表
  getUserList(data, success) {
    this.ajaxData('/api/user/list', 'GET', '', data, success)
  }
  // 获取员工列表
  getUserIndexList(data, success) {
    this.ajaxData('/api/user/index/list', 'GET', '', data, success)
  }
  // 获取员工详情
  getDetail(data, success) {
    this.ajaxData('/api/user/detail', 'GET', '', data, success)
  }
  // 获取总店员工详情
  getDetailByMobile(data, success) {
    this.ajaxData('/api/user/detail/mobile', 'GET', '', data, success)
  }
  // 保存员工
  saveUser(data, success) {
    this.ajaxData('/api/user/save', 'POST', 'json', data, success)
  }
  // 修改
  updateUser(data, success) {
    this.ajaxData('/api/user/update', 'POST', 'json', data, success)
  }
  // 修改个人信息
  updateUserSelf(data, success) {
    this.ajaxData('/api/user/personal/update', 'POST', 'json', data, success)
  }
  // 删除员工
  deleteUser(data, success) {
    this.ajaxData('/api/user/delete', 'POST', '', data, success)
  }
  // 个人中心
  personalCenter(data, success) {
    this.ajaxData('/api/user/personal/center', 'GET', '', data, success)
  }
  // 服务反馈
  getCommentList(data, success) {
    this.ajaxData('/api/user/comment/list', 'GET', '', data, success)
  }
  //退出登录
  logout(success) {
    this.ajaxData('/api/auth/logout', 'POST', '', {}, success)
  }

  //获取员工个人评论列表
  getCommentList(data,success) {
    this.ajaxData('/api/user/comment/list', 'GET', '', data, success)
  }

  //获取员工累计指标
  getUserIndex(success) {
    this.ajaxData('/api/statistic/user/index', 'GET', '', {}, success)
  }
}

export default new ApiEmployee