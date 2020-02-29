import API from '/api/application.js'
var util = require('/utils/util')
App({
  onLaunch: function() {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 获取系统状态栏信息
    wx.getSystemInfo({
      success: e => {
        this.systemInfo = e;
        this.globalData.StatusBar = e.statusBarHeight;
        let custom = wx.getMenuButtonBoundingClientRect();
        this.globalData.Custom = custom;
        this.globalData.CustomBar = custom.bottom + custom.top - e.statusBarHeight;
        //适配全面屏底部距离
        if (this.globalData.CustomBar > 75) {
          this.globalData.tabbar_bottom = "y"
        };
      }
    })

  },
  globalData: {
    today: util.todayDate(),
    shopList: [],
    positionList: [],
    roleList: [],
    memberLevelList:[],
    customerSourceList:[],
    //和用户更新有关系
    currentUser: null,
    //和门店切换有关系
    currentShop: null,
    navigatorList: [], //+预约确认有关
    serviceList: [],
    userList: [],
  },
  systemInfo: null,//系统信息
  build:{
    env:"test"
  },
  getShopList: function () {
    let app=this
    return new Promise((resolve, reject) => {
      if (app.globalData.shopList.length>0){
        resolve(app.globalData.shopList)
      }else{
        API.getShopList(function (res) {
          if (res.data.length > 0) {
            app.globalData.shopList=res.data
            resolve(res.data)
          } else {
            reject("门店未配置")
          }
        })
      }
    })
  },
  getNearestShop: function (param) {
    let app = this
    return new Promise((resolve, reject) => {
      if (app.globalData.currentShop){
        resolve(app.globalData.currentShop)
      }else{
        API.getNearestShop(param, function (res) {
          if (res.data) {
            app.globalData.currentShop = res.data
            resolve(res.data)
          } else {
            reject('返回为空')
          }
        })
      }
    })
  },
  getNavigatorList: function () {
    let app = this
    return new Promise((resolve, reject) => {
      if (app.globalData.navigatorList.length>0){
        resolve(app.globalData.navigatorList)
      }else{
        let data = {
          shopId: app.globalData.currentShop.id
        }
        API.getNavigatorList(data, function (res) {
          app.globalData.navigatorList = res.data
          resolve(res.data)
        })
      }
    })
  },
  getPositionList: function(){
    let app = this
    return new Promise((resolve, reject) => {
      if (app.globalData.positionList.length > 0) {
        resolve(app.globalData.positionList)
      } else {
        API.getPositionList(function (res) {
          app.globalData.positionList = res.data
          resolve(res.data)
        })
      }
    })
  },
  getRoleList: function(){
    let app = this
    return new Promise((resolve, reject) => {
      if (app.globalData.roleList.length > 0) {
        resolve(app.globalData.roleList)
      } else {
          API.getRoleList(function (res) {
          app.globalData.roleList = res.data
          resolve(res.data)
        })
      }
    })
  },
  getServiceList: function(){
    let app = this
    return new Promise((resolve, reject) => {
      if (app.globalData.serviceList.length > 0) {
        resolve(app.globalData.serviceList)
      } else {
        let data = {
          shopId: app.globalData.currentShop.id
        }
        API.getServiceList(data, function (res) {
          app.globalData.serviceList = res.data
          resolve(res.data)
        })
      }
    })
  },
  getMemberLevelList:function(){
    let app = this
    return new Promise((resolve, reject) => {
      if (app.globalData.memberLevelList.length > 0) {
        resolve(app.globalData.memberLevelList)
      } else {
        API.getMemberLevel(function (res) {
          app.globalData.memberLevelList = res.data
          resolve(res.data)
        })
      }
    })
  },
  getCustomerSourceList: function () {
    let app = this
    return new Promise((resolve, reject) => {
      if (app.globalData.customerSourceList.length > 0) {
        resolve(app.globalData.customerSourceList)
      } else {
        API.getCustomerSource(function (res) {
          app.globalData.customerSourceList = res.data
          resolve(res.data)
        })
      }
    })
  },
  getCurrentUser: function () {
    let app = this
    return new Promise((resolve, reject) => {
      if (app.globalData.currentUser) {
        resolve(app.globalData.currentUser)
      } else {
        API.getUserCurrent(function (res) {
          app.globalData.currentUser = res.data
          resolve(res.data)
        })
      }
    })
  }
})