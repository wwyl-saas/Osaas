import API from '../../api/member.js'
var util = require('../../utils/util')
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    phone: '',
    memberList: [],
    memberLevelList:[],
    customerSourceList:[],
    memberLevel:null,
    customerSource:null,
    loaded: false
  },
  searchCustomer: function() {
    const that = this
    if (this.data.query.length>0){
      let data = {
        query: this.data.query
      }
      this.getCustomerList(data)
    }else{
      util.mytoast('请输用户手机号','none')
    }
  
  },
  getCustomerList: function(param){
    let that=this
    API.getCustomerList(param, function (res) {
      let list = res.data.results
      list = list.map(item => {
        if (item.memberLevelCode == 0) {
          item.color = 'bg-blue'
        } else if (item.memberLevelCode == 1) {
          item.color = 'bg-gray'
        } else if (item.memberLevelCode == 2) {
          item.color = 'bg-orange'
        } else if (item.memberLevelCode == 3) {
          item.color = 'bg-grey'
        } else if (item.memberLevelCode == 4) {
          item.color = 'bg-black'
        } else {
          item.color = 'bg-white'
        }
        return item
      })
      that.setData({
        memberList: list,
        pageIndex: res.data.pageIndex,
        pageSize: res.data.pageSize,
        totalPage: res.data.totalPage,
        loaded: true
      })
    })
  },
  inputMobile: function(e) {
    let mobile = e.detail.value
    this.setData({
      query: mobile
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
  selectMemberLevel: function(e){
    this.setData({
      memberLevel: e.currentTarget.dataset.value
    })
  },
  selectCustomerSource: function(e){
    this.setData({
      customerSource: e.currentTarget.dataset.value
    })
  },
  cleanCondition: function (e) {
    this.setData({
      memberLevel: null,
      customerSource: null,
      query: null
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
    let data={}
    if (this.data.memberLevel!=null&&this.data.memberLevel>=0){
      data.memberLevel=this.data.memberLevel
    }
    if (this.data.customerSource!=null&&this.data.customerSource >= 0) {
      data.customerSource = this.data.customerSource
    }
    this.getCustomerList(data)
    this.setData({
      modalName: null
    })
    wx.hideLoading()
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let that = this
    let data = {}
    this.getCustomerList(data)

    app.getMemberLevelList().then(res=>{
      that.setData({
        memberLevelList: res
      })
    })

    app.getCustomerSourceList().then(res => {
      that.setData({
        customerSourceList: res
      })
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
    if (this.data.pageIndex < this.data.totalPage) {
      let data = {
        pageIndex: this.data.pageIndex+1,
        pageSize: this.data.pageSize
      }
      API.getCustomerList(data, function(res) {
        let list = res.data.results
        list = list.map(item => {
          if (item.memberLevelCode == 0) {
            item.color = 'bg-blue'
          } else if (item.memberLevelCode == 1) {
            item.color = 'bg-gray'
          } else if (item.memberLevelCode == 2) {
            item.color = 'bg-orange'
          } else if (item.memberLevelCode == 3) {
            item.color = 'bg-grey'
          } else if (item.memberLevelCode == 4) {
            item.color = 'bg-black'
          } else {
            item.color = 'bg-white'
          }
          return item
        })

        let memberList = that.data.memberList.concat(list)
        that.setData({
          memberList: memberList,
          pageIndex: res.data.pageIndex,
          pageSize: res.data.pageSize,
          totalPage: res.data.totalPage
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