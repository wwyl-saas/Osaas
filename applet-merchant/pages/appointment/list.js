import API from '../../api/appointment.js'
var util = require('../../utils/util')
const app = getApp();
Page({
  /**
   * 页面的初始数据
   */
  data: {
    CustomBar: app.globalData.CustomBar,
    TabCur: 0,
    loaded:false,
    scrollLeft: 0,
    arriveDate: util.todayDate(),
    startDate: util.futureDate(-31),
    endDate: util.todayDate(),
    statusKey: 0,
    createDateStart: null,
    createDateEnd: null,
    appointmentPhone: null,
    appointmentList: [],
    dateList: [],
    cancelAppointmentName:"",
    cancelAppointmentId: null
  },
  getAppointmentList: function() {
    return new Promise((resolve, reject) => {
      let data = {
        shopId: app.globalData.currentShop.id,
        arriveDate: this.data.arriveDate
      }
      if (this.data.statusKey >= 0) {
        data.appointmentStatus = this.data.statusKey
      }
      if (this.data.createDateStart && this.data.createDateEnd) {
        data.createDateStart = this.data.createDateStart
        data.createDateEnd = this.data.createDateEnd
      }
      if (this.data.appointmentPhone && this.data.appointmentPhone.length === 11) {
        data.appointmentPhone = this.data.appointmentPhone
      }
      API.getAppointmentList(data, function(res) {
        resolve(res.data)
      })
    })
  },
  getAppointmentListPrefilter: function() {
    return new Promise((resolve, reject) => {
      let data = {
        shopId: app.globalData.currentShop.id,
        arriveDate: this.data.arriveDate
      }
      if (this.data.createDateStart && this.data.createDateEnd) {
        data.createDateStart = this.data.createDateStart
        data.createDateEnd = this.data.createDateEnd
      }
      if (this.data.appointmentPhone && this.data.appointmentPhone.length === 11) {
        data.appointmentPhone = this.data.appointmentPhone
      }
      API.getAppointmentListPrefilter(data, function(res) {
        resolve(res.data)
      })
    })
  },
  tabSelect(e) {
    this.setData({
      TabCur: e.currentTarget.dataset.id,
      scrollLeft: (e.currentTarget.dataset.id - 1) * 60
    })
  },
  callPhone(e) {
    let phone = e.currentTarget.dataset['phone'];
    wx.makePhoneCall({
      phoneNumber: phone 
    })
  },
  change: function(e) {
    let appointmentId = e.currentTarget.dataset.target
    wx.navigateTo({
      url: '/pages/appointment/change?appointment=' + appointmentId,
    })
  },
  cancel: function(e) {
    console.log(e)
    let appointmentId = e.currentTarget.dataset.target
    let appointmentName = e.currentTarget.dataset.name
    this.setData({
      cancelAppointmentName: appointmentName,
      cancelAppointmentId: appointmentId,
      modalName: 'DeleteModal'
    })
  },
  cancelConfirm: function(){
    let that = this
    let data = {
      appointmentId: this.data.cancelAppointmentId
    }
    API.cancelAppointment(data, function (res) {
      wx.showToast({
        title: '取消成功',
        icon: 'none',
        duration: 1000,
        mask: true
      })
      that.getAppointmentList().then(res => {
        that.setData({
          appointmentList: res.pageList.results,
          pageIndex: res.pageList.pageIndex,
          pageSize: res.pageList.pageSize,
          totalPage: res.pageList.totalPage,
          total: res.total,
          waitingConfirm: res.waitingConfirm,
          confirmed: res.confirmed,
          canceled: res.canceled
        })
      })
      that.getAppointmentListPrefilter().then(res => {
        that.setData({
          dateList: res
        })
      })
    })
    this.setData({
      modalName: null,
      cancelAppointmentName: "",
      cancelAppointmentId: null
    })
  },
  cancleCancel:function(){
    this.setData({
      modalName: null,
      cancelAppointmentName: "",
      cancelAppointmentId: null
    })
  },
  confirm: function(e) {
    let that = this
    let appointmentId = e.currentTarget.dataset.target
    let data = {
      appointmentId: appointmentId
    }
    API.confirmAppointment(data, function(res) {
      wx.showToast({
        title: '确认成功',
        icon: 'none',
        duration: 1000,
        mask: true
      })
      that.getAppointmentList().then(res => {
        that.setData({
          appointmentList: res.pageList.results,
          pageIndex: res.pageList.pageIndex,
          pageSize: res.pageList.pageSize,
          totalPage: res.pageList.totalPage,
          total: res.total,
          waitingConfirm: res.waitingConfirm,
          confirmed: res.confirmed,
          canceled: res.canceled
        })
      })
      that.getAppointmentListPrefilter().then(res => {
        that.setData({
          dateList: res
        })
      })
      app.globalData.navigatorList = []
    })
  },
  showModal(e) {
    this.setData({
      modalName: e.currentTarget.dataset.target
    })
  },
  hideModal(e) {
    this.setData({
      modalName: null
    })
  },
  selectStatus: function(e) {
    wx.showLoading({
      mask: true,
      title: '加载中...',
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 2000)
    let that = this
    let status = e.currentTarget.dataset.target
    this.setData({
      statusKey: status
    })
    this.getAppointmentList().then(res => {
      that.setData({
        appointmentList: res.pageList.results,
        pageIndex: res.pageList.pageIndex,
        pageSize: res.pageList.pageSize,
        totalPage: res.pageList.totalPage,
        total: res.total,
        waitingConfirm: res.waitingConfirm,
        confirmed: res.confirmed,
        canceled: res.canceled
      })
      wx.hideLoading()
    }).catch(res=>{
      wx.hideLoading()
    })
  },
  selectDate: function(e) {
    wx.showLoading({
      mask: true,
      title: '加载中...',
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 2000)
    let that = this
    that.setData({
      arriveDate: e.detail
    })
    this.getAppointmentList().then(res => {
      that.setData({
        appointmentList: res.pageList.results,
        pageIndex: res.pageList.pageIndex,
        pageSize: res.pageList.pageSize,
        totalPage: res.pageList.totalPage,
        total: res.total,
        waitingConfirm: res.waitingConfirm,
        confirmed: res.confirmed,
        canceled: res.canceled,
        loaded: true,
      })
      wx.hideLoading()
    }).catch(res => {
      wx.hideLoading()
    })
  },
  bindStartDateChange: function(e) {
    let startDate = e.detail.value
    let endDate = this.data.createDateEnd

    if (endDate) {
      let start = util.toDate(startDate)
      let end = util.toDate(endDate)
      if (start > end) {
        util.mytoast('结束日期不能早于起始日期', 'none');
        return false;
      }
    }
    this.setData({
      createDateStart: startDate
    })
  },
  bindEndDateChange: function(e) {
    let startDate = this.data.createDateStart
    let endDate = e.detail.value

    if (startDate) {
      let start = util.toDate(startDate)
      let end = util.toDate(endDate)
      if (start > end) {
        util.mytoast('结束日期不能早于起始日期', 'none');
        return false;
      }
    }
    this.setData({
      createDateEnd: e.detail.value
    })
  },
  inputAppointmentPhone: function(e) {
    let mobile = e.detail.value
    if (mobile.length === 11) {
      let checkedNum = this.checkMobile(mobile)
      if (checkedNum) {
        this.setData({
          appointmentPhone: mobile
        })
      } else {
        util.mytoast('手机号不正确', 'none');
        this.setData({
          appointmentPhone: null
        })
      }
    }

  },
  checkMobile: function(mobile) {
    let str = /^1[3456789]\d{9}$/
    if (str.test(mobile)) {
      return true
    } else {
      util.mytoast('手机号不正确', 'none')
      return false
    }
  },
  cleanCondition: function(e){
    this.setData({
      createDateStart: null,
      createDateEnd: null,
      appointmentPhone: null
    })
    this.submitCondition()
  },
  submitCondition:function(e){
    wx.showLoading({
      mask: true,
      title: '加载中...',
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 2000)
    let that = this
    this.getAppointmentList().then(res => {
      that.setData({
        appointmentList: res.pageList.results,
        pageIndex: res.pageList.pageIndex,
        pageSize: res.pageList.pageSize,
        totalPage: res.pageList.totalPage,
        total: res.total,
        waitingConfirm: res.waitingConfirm,
        confirmed: res.confirmed,
        canceled: res.canceled,
        modalName: null
      })
      wx.hideLoading()
    })
  },
  // ListTouch触摸开始
  ListTouchStart(e) {
    this.setData({
      ListTouchStart: e.touches[0].pageX
    })
  },

  // ListTouch计算方向
  ListTouchMove(e) {
    this.setData({
      ListTouchDirection: e.touches[0].pageX - this.data.ListTouchStart > 0 ? 'right' : 'left'
    })
  },

  // ListTouch计算滚动
  ListTouchEnd(e) {
    if (this.data.ListTouchDirection == 'left') {
      this.setData({
        modalName: e.currentTarget.dataset.target
      })
    } else {
      this.setData({
        modalName: null
      })
    }
    this.setData({
      ListTouchDirection: null
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let that = this
    this.getAppointmentListPrefilter()
      .then(res => {
        that.setData({
          dateList: res
        })
      }).catch(result => {
        console.log(result)
      })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {
    let that = this
    if (this.data.pageIndex < this.data.totalPage){
      this.setData({
        pageIndex: this.data.pageIndex + 1,
        pageSize: this.data.pageSize + 1
      })
      this.getAppointmentList().then(res => {
        let list = that.data.appointmentList.concat(res.pageList.results)
        that.setData({
          appointmentList: list,
          pageIndex: res.pageList.pageIndex,
          pageSize: res.pageList.pageSize,
          totalPage: res.pageList.totalPage,
          total: res.total,
          waitingConfirm: res.waitingConfirm,
          confirmed: res.confirmed,
          canceled: res.canceled,
        })
      })
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})