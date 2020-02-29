import API from '../../api/notice.js'
var util = require('../../utils/util')
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    announce:{}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let pages = getCurrentPages(); // 获取页面栈
    let prevPage = pages[pages.length - 2]; // 父级页面（返回上个页面）
    let announceList = prevPage.data.announceList
    this.setData({
      announce: announceList[options.index]
    })
    if (announceList[options.index].isRead == false) {
      announceList[options.index].isRead = true
      prevPage.setData({
        announceList: announceList,
        announceCount: prevPage.data.announceCount - 1
      })
      let data = {
        announceId: announceList[options.index].id
      }
      API.setAnnounceRead(data, function (res) {
        app.globalData.navigatorList = []
      })
    
    }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})