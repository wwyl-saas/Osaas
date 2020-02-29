import * as echarts from '../../components/ec-canvas/echarts';
import API from '../../api/statistic.js'
var util = require('../../utils/util')
const app = getApp();
let InitChart = null;

Page({

  /**
   * 页面的初始数据
   */
  data: {
    cycleId: 'day',
    startDate: util.todayDate(),
    endDate: util.todayDate(),
    tabCur: 'list',
    titleName: '', //页面名称
    customerList:[],
    dataList:[],
    xList: [],
    yList: [],
    ec: {
      onInit: function (canvas, width, height) {
        InitChart = echarts.init(canvas, null, {
          width: width,
          height: height
        })
        canvas.setChart(InitChart)
        return InitChart
      }
    }
  },
  changeTap: function(e) {
    let tab = e.currentTarget.dataset.target
    this.setData({
      tabCur: tab
    })
    this.getStatisticData()
  },
  onClickTabCycle: function(e) {
    this.setData({
      cycleId: e.detail.id
    })
  },
  onCallbackDate: function(e) {
    this.setData({
      startDate: util.formatStandardDate(e.detail.date.startDate),
      endDate: util.formatStandardDate(e.detail.date.endDate)
    })
    this.getStatisticData()
  },

  getStatisticData:function(param){
    const that=this
    wx.showLoading({
      mask: true,
      title: '加载中...',
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 2000)
    if(this.data.user){//门店用户
      if (this.data.tabCur === "list") { //明细
        that.getUserStatisticList(param).then(res => {
          let data = {
            pageIndex: res.pageIndex,
            pageSize: res.pageIndex,
            totalPage: res.totalPage
          }
          if (that.data.cycleId === "day") { //日明细
            data.customerList = res.customerList
          } else { //区间统计
            data.dataList = res.dataList
          }
          that.setData(data)
        })
      } else {
        that.getUserStatisticChart().then(res => {
          let dataList = res
          let xList = dataList.map(item => item.dataIndex)
          let yList = dataList.map(item => item.dataValue)
          that.setOption(xList, yList)
        })
      }
    }else{//门店
      if (that.data.tabCur === "list") { //明细
        that.getShopStatisticList(param).then(res => {
          let data = {
            pageIndex: res.pageIndex,
            pageSize: res.pageIndex,
            totalPage: res.totalPage
          }
          if (that.data.cycleId === "day") { //日明细
            data.customerList = res.customerList
          } else { //区间统计
            data.dataList = res.dataList
          }
          that.setData(data)
        })
      } else {
        that.getShopStatisticChart().then(res => {
          let dataList = res
          let xList = dataList.map(item => item.dataIndex)
          let yList = dataList.map(item => item.dataValue)
          that.setOption(xList, yList)
        })
      }

    }
  },
  getShopStatisticList: function (param) {
    const that = this
    return new Promise((resolve, reject) => {
      let data = {
        shopId: app.globalData.currentShop.id,
        dataType: that.data.type,
        dateType: that.data.cycleId,
        startDate: that.data.startDate,
        endDate: that.data.endDate,
      }
      if (param) {
        data.pageIndex = param.pageIndex
        data.pageSize = param.pageSize
      }
      API.getShopStatisticList(data, function (res) {
        resolve(res.data)
      }, function () {
        wx.hideLoading()
      })
    })
  },
  getShopStatisticChart: function() {
    const that = this
    return new Promise((resolve, reject) => {
      let data = {
        shopId: app.globalData.currentShop.id,
        dataType: that.data.type,
        dateType: that.data.cycleId,
        startDate: that.data.startDate,
        endDate: that.data.endDate,
      }
      API.getShopStatisticChart(data, function(res) {
        resolve(res.data)
      },function(){
        wx.hideLoading()
      })
    })
  },
  getUserStatisticList: function(param) {
    const that = this
    return new Promise((resolve, reject) => {
      let data = {
        shopId: app.globalData.currentShop.id,
        dataType: that.data.type,
        dateType: that.data.cycleId,
        startDate: that.data.startDate,
        endDate: that.data.endDate,
      }
      if (param) {
        data.pageIndex = param.pageIndex
        data.pageSize = param.pageSize
      }
      API.getUserStatisticList(data, function(res) {
        resolve(res.data)
      }, function () {
        wx.hideLoading()
      })
    })
  },
  getUserStatisticChart: function() {
    const that = this
    return new Promise((resolve, reject) => {
      let data = {
        shopId: app.globalData.currentShop.id,
        dataType: that.data.type,
        dateType: that.data.cycleId,
        startDate: that.data.startDate,
        endDate: that.data.endDate,
      }
      API.getUserStatisticChart(data, function(res) {
        resolve(res.data)
      }, function () {
        wx.hideLoading()
      })
    })
  },
  setOption: function (xList, yList) {
    let option = {
      backgroundColor: '#ffffff',
      xAxis: {
        type: 'category',
        data: xList
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        data: yList,
        type: 'line',
        smooth: true
      }]
    };
    InitChart.setOption(option)
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const that=this
    let type = options.type
    let data = {
      type: type,
      titleName: options.titleName
    }
    let user = options.user
    if (user) {
      data.user = user
    }
    this.setData(data)
    this.getStatisticData()
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