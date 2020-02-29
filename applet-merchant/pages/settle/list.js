var util = require('../../utils/util.js');
import API from '../../api/settle.js'
const app = getApp()
// pages/settle/list.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    Custom: app.globalData.Custom,
    startDate: util.futureDate(-15),
    endDate: util.todayDate(),
    orderCreateDate: util.todayDate(),
    statusKey: 0,
    orderList: [],
    waitingPay: 0,
    payed: 0,
    total: 0,
    canceled: 0,
    order:{},
    cusomerPhone: null,
    loaded: false,
  },
  getOrderList: function() {
    return new Promise((resolve, reject) => {
      let that = this
      let data = {
        shopId: app.globalData.currentShop.id,
        date: this.data.orderCreateDate
      }
      if (this.data.statusKey >= 0) {
        data.status = this.data.statusKey
      }
      if (this.data.cusomerPhone) {
        data.customerPhone = this.data.cusomerPhone
      }
      if (this.data.pageIndex) {
        data.pageIndex = this.data.pageIndex
      }
      if (this.data.pageSize) {
        data.pageSize = this.data.pageSize
      }
      API.getOrderList(data, function(res) {
        resolve(res.data)
      })
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
    const that = this
    let date = e.detail
    this.setData({
      orderCreateDate: date
    })
    this.getOrderList().then(res => {
      that.setData({
        orderList: res.pageList.results,
        pageIndex: res.pageList.pageIndex,
        pageSize: res.pageList.pageSize,
        totalPage: res.pageList.totalPage,
        waitingPay: res.waitingPay,
        payed: res.payed,
        total: res.total,
        canceled: res.canceled,
        loaded: true
      })
      wx.hideLoading()
    }).catch(res => {
      wx.hideLoading()
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
    this.getOrderList().then(res => {
      that.setData({
        orderList: res.pageList.results,
        pageIndex: res.pageList.pageIndex,
        pageSize: res.pageList.pageSize,
        totalPage: res.pageList.totalPage,
        waitingPay: res.waitingPay,
        payed: res.payed,
        total: res.total,
        canceled: res.canceled
      })
      wx.hideLoading()
    }).catch(res=>{
      wx.hideLoading()
    })
  },
  cancel: function(e) {
    const that = this
    let orderId = e.currentTarget.dataset.target
    let list = this.data.orderList;
    let data = {
      orderId: orderId
    }
    API.cancelOrder(data, function(res) {
      list.splice(list.findIndex(item => item.id == orderId), 1) //删除
      util.mytoast('取消成功', 'none')
      that.setData({
        orderList: list
      });
    })
  },
  charge: function(e) {
    const that = this
    let orderId = e.currentTarget.dataset.target
    let list = this.data.orderList;
    let data = {
      orderId: orderId
    }
    API.remainderSettle(data, function(res) {
      list.splice(list.findIndex(item => item.id == orderId), 1, res.data) //替换
      util.mytoast('支付成功', 'none')
      that.setData({
        orderList: list
      });
    })
  },
  inputPhone: function(e) {
    let mobile = e.detail.value
    if (mobile.length === 11) {
      let checkedNum = this.checkMobile(mobile)
      if (checkedNum) {
        this.setData({
          cusomerPhone: mobile
        })
      } else {
        util.mytoast('手机号不正确', 'none');
        this.setData({
          cusomerPhone: null
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
  showDetail(e) {
    let that=this
    let orderId = e.currentTarget.dataset.target
    let data = {
      orderId: orderId
    }
    API.getDetail(data, function (res) {
      that.setData({
        order: res.data,
        modalName: 'viewModal'
      })
    })
  },
  showModal(e) {
    this.setData({
      modalName: e.currentTarget.dataset.target
    })
  },
  hideModal(e) {
    this.setData({
      order: null,
      modalName: null
    })
  },
  cleanCondition: function (e) {
    this.setData({
      cusomerPhone: null
    })
    this.submitCondition()
  },
  submitCondition: function (e) {
    wx.showLoading({
      mask: true,
      title: '加载中...',
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 2000)
    let that = this
    this.getOrderList().then(res => {
      that.setData({
        orderList: res.pageList.results,
        pageIndex: res.pageList.pageIndex,
        pageSize: res.pageList.pageSize,
        totalPage: res.pageList.totalPage,
        waitingPay: res.waitingPay,
        payed: res.payed,
        total: res.total,
        canceled: res.canceled,
        modalName: null
      })
      wx.hideLoading()
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

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
    if (this.data.pageIndex < this.data.totalPage) {
      this.setData({
        pageIndex: this.data.pageIndex+1,
        pageSize: this.data.pageSize+1
      })
      this.getOrderList().then(res => {
        let list = that.data.orderList.concat(res.pageList.results)
        that.setData({
          orderList: list,
          pageIndex: res.pageList.pageIndex,
          pageSize: res.pageList.pageSize,
          totalPage: res.pageList.totalPage,
          waitingPay: res.waitingPay,
          payed: res.payed,
          total: res.total,
          canceled: res.canceled
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