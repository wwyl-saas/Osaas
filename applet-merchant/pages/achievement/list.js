var util = require('../../utils/util.js');
import API from '../../api/achievement.js'
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    cycleId: 'day',
    statusKey: 0,
    startDate: util.todayDate(),
    endDate: util.todayDate(),
    achievementList: []
  },
  onClickTabCycle: function onClickTabCycle(e) {
    this.setData({
      cycleId: e.detail.id
    })
  },
  onCallbackDate: function onCallbackDate(e) {
    wx.showLoading({
      mask: true,
      title: '加载中...',
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 2000)
    let that = this
    this.setData({
      startDate: util.formatStandardDate(e.detail.date.startDate),
      endDate: util.formatStandardDate(e.detail.date.endDate)
    })
    this.getAchievementList().then(res => {
      that.setData({
        achievementList: res
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
    this.getAchievementList().then(res => {
      that.setData({
        achievementList: res
      })
      wx.hideLoading()
    }).catch(res => {
      wx.hideLoading()
    })
  },
  getAchievementList: function() {
    let data = {
      shopId: app.globalData.currentShop.id,
      dataType: this.data.statusKey,
      dateType: this.data.cycleId,
      startDate: this.data.startDate,
      endDate: this.data.endDate
    } 
    return new Promise((resolve, reject) => {
      API.getAchievementList(data, function(res) {
        let list = res.data
        list = list.map(item => {
          if (item.rank === 1) {
            item.color = 'text-red'
            item.icon = true
          } else if (item.rank === 2) {
            item.color = 'text-orange'
            item.icon = true
          } else if (item.rank === 3) {
            item.color = 'text-yellow'
            item.icon = true
          }
          return item
        })
        resolve(list)
      })
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    wx.showLoading({
      mask: true,
      title: '加载中...',
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 2000)
    const that = this
    this.getAchievementList().then(res=>{
      that.setData({
        achievementList: res
      })
      wx.hideLoading()
    }).catch(res=>{
      wx.hideLoading()
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

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})