const { ajax } = require('../utils/request').default
const { sendLink } = require('../utils/config.js').default

class ApiAppointment {
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

  // 获取预约列表
  getAppointmentList(data, success) {
    this.ajaxData('/api/appointment/list', 'GET', '', data, success)
  }
  // 获取预约列表
  getAppointmentListPrefilter(data, success) {
    this.ajaxData('/api/appointment/prefilter', 'GET', '', data, success)
  }
  // 获取预约
  getAppointment(data, success) {
    this.ajaxData('/api/appointment/one', 'GET', '', data, success)
  }
  // 取消预约
  cancelAppointment(data, success) {
    this.ajaxData('/api/appointment/cancel', 'POST', '', data, success)
  }
  // 确定预约
  confirmAppointment(data, success) {
    this.ajaxData('/api/appointment/confirm', 'POST', '', data, success)
  }
  // 修改预约
  changeAppointment(data, success) {
    this.ajaxData('/api/appointment/change', 'POST', 'json', data, success)
  }
  // 查询发型师预约列表
  hairdresserList(data, success) {
    this.ajaxData('/api/appointment/hairdresser/list', 'GET', '', data, success)
  }
  // 查询服务列表
  serviceList(data, success) {
    this.ajaxData('/api/appointment/service/list', 'GET', '', data, success)
  }
}

export default new ApiAppointment