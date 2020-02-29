import API from '../../api/notice.js'
var util = require('../../utils/util')
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    TabCur: 1,
    noticeCount:0,
    feedbackCount:0,
    announceCount:0,
    noticeList:[],
    announceList:[],
    feedbackList:[],
    scrollLeft: 0,
    loaded1: false,
    loaded2: false,
    loaded3: false
  },
  getNoticeNum(){
    return new Promise((resolve, reject) => {
      API.getNoticeNum(function (res) {
        if (res.data) {
          resolve(res.data)
        } else {
          reject('返回为空')
        }
      })
    })
  },
  getNoticeList() {
    return new Promise((resolve, reject) => {
      API.getNoticeList(function (res) {
        if (res.data) {
          resolve(res.data)
        } else {
          reject('返回为空')
        }
      })
    })
  },
  getAnnounceList() {
    return new Promise((resolve, reject) => {
      API.getAnnounceList(function (res) {
        if (res.data) {
          resolve(res.data)
        } else {
          reject('返回为空')
        }
      })
    })
  },
  getFeedbackList() {
    return new Promise((resolve, reject) => {
      API.getFeedbackList(function (res) {
        if (res.data) {
          resolve(res.data)
        } else {
          reject('返回为空')
        }
      })
    })
  },
  tabSelect(e) {
    const that=this
    let tabcur = e.currentTarget.dataset.id;
    this.setData({
      TabCur: tabcur,
      scrollLeft: (e.currentTarget.dataset.id - 1) * 60
    })
    console.log(tabcur)
   
    if (tabcur ==2 && that.data.feedbackList.length == 0){
        wx.showLoading({
          title: '加载中',
        })
        that.getFeedbackList().then(res=>{
          that.setData({
            feedbackList:res,
            loaded2: true
          })
          wx.hideLoading()
        }).catch(res=>{
          console.log(res)
          wx.hideLoading()
        })
    } else if (tabcur == 3 && that.data.announceList.length == 0){
        wx.showLoading({
          title: '加载中',
        })
        that.getAnnounceList().then(res => {
          that.setData({
            announceList: res,
            loaded3: true
          })
          wx.hideLoading()
        }).catch(res => {
          console.log(res)
          wx.hideLoading()
        })
    }
  },
  navigateToMessage: function(e){
    wx.navigateTo({
      url: '/pages/notice/message?index=' + e.currentTarget.dataset.target
    })
  },
  navigateToFeedback: function (e) {
    wx.navigateTo({
      url: '/pages/notice/feedback?index=' + e.currentTarget.dataset.target
    })
  },
  navigateToAnnounce: function (e) {
    wx.navigateTo({
      url: '/pages/notice/announcement?index=' + e.currentTarget.dataset.target
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const that=this
    wx.showLoading({
      title: '加载中',
    })
    this.getNoticeNum().then(res=>{
      that.setData({
        noticeCount: res.noticeCount,
        feedbackCount: res.feedbackCount,
        announceCount: res.announceCount
      })
      return that.getNoticeList()
    }).then(res=>{
      that.setData({
        noticeList: res,
        loaded1: true
      })
      wx.hideLoading()
    }).catch(res=>{
      console.log(res)
      wx.hideLoading()
    })
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