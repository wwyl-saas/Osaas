const { ajax } = require('../utils/request').default
const { sendLink } = require('../utils/config.js').default

class ApiNotice {
  constructor() {
  }
  // 这个是封装调用的，可以根据自己的需求做修改
  ajaxData(url, method, dataType, data, success, fail, complete) {
    ajax({
      url: sendLink() + url,
      method: method,
      dataType: dataType,
      data: data,
      success: function (ret) {
        typeof success == 'function' && success.call(this, ret)
      },
      fail: function (res) {
        if (fail) {
          typeof fail == 'function' && fail.call(this, res)
        }
      },
      complete: function () {
        if (complete) {
          typeof complete == 'function' && complete.call(this)
        }
      }
    })
  }


  // 获取未读消息数
  getNoticeNum(success) {
    this.ajaxData('/api/message/notice/num', 'GET', '', {}, success)
  }
  // 获取通知消息列表
  getNoticeList(success) {
    this.ajaxData('/api/message/notice/list', 'GET', '', {}, success)
  }
  // 获取公告列表
  getAnnounceList(success) {
    this.ajaxData('/api/message/announce/list', 'GET', '', {}, success)
  }
  //获取反馈列表
  getFeedbackList(success) {
    this.ajaxData('/api/message/feedback/list', 'GET', '', {}, success)
  }
  //获取反馈详情
  getFeedbackDetail(data,success) {
    this.ajaxData('/api/feedback/detail', 'GET', '', data, success)
  }
  // 设置通知已读
  setNoticeRead(data, success) {
    this.ajaxData('/api/message/update/notice', 'POST', '', data, success)
  }
  //设置公告已读
  setAnnounceRead(data, success) {
    this.ajaxData('/api/message/update/announce', 'POST', '', data, success)
  }
  // 设置通知已读
  setFeedbackRead(data, success) {
    this.ajaxData('/api/message/update/feedback', 'POST', '', data, success)
  }
}

export default new ApiNotice