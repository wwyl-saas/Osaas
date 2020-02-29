import API from '../../api/appointment.js'
import WxValidate from '../../utils/validate.js'
var util = require('../../utils/util')
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    startDate: util.todayDate(),
    endDate: util.futureDate(20),
    goodsNameList:[],
    goodsList:[],
    userNameList: [],
    userList: [],
    source:'appointment'
  },
  
  formSubmit: function (e) {
    let param = e.detail.value
    if (!this.WxValidate.checkForm(param)) {
      const error = this.WxValidate.errorList[0]
      util.mytoast(error.msg, 'none')
      return false
    }
    let newDate = util.toDateTime(this.data.appointment.arriveDate, this.data.appointment.arriveTime)
    let now = new Date()

    if (now > newDate) {
      util.mytoast('请选择未来时间做为预约时间', 'none');
      return false;
    }

    let page=this
    let pages = getCurrentPages(); //获取当前页面js里面的pages里的所有信息。
    let prevPage = pages[pages.length - 2];
    let list = prevPage.data.appointmentList;
    let data={
      appointmentId: param.appointmentId,
      arriveDate: param.arriveDate,
      arriveTime: param.arriveTime,
      customerName: param.appointName,
      phone: param.appointMobile
    }
    if (param.goodsIndex){
      data.goodsId = this.data.goodsList[param.goodsIndex].id
      data.goodsName = this.data.goodsList[param.goodsIndex].name
    }
    if (param.userIndex){
      data.merchantUserId = this.data.userList[param.userIndex].id
    }
    API.changeAppointment(data, function (res) {
      if (res.data.merchantUserId){
        let index = page.data.userList.findIndex(item => item.id === res.data.merchantUserId)
        if (index != -1) {
          res.data.merchantUserName = page.data.userList[index].name
        }
      }
      list.splice(list.findIndex(item => item.id === res.data.id), 1, res.data)//替换
      prevPage.setData({
        appointmentList:list
      })
      util.mytoast('改约成功','none')
      wx.navigateBack({
        delta: 1
      })
    })
  },
  getAppointment: function(param){
    return new Promise((resolve,reject)=>{
      API.getAppointment(param, function (res) {
        resolve(res.data)
      })
    })
  },
  hairdresserList:function(p){
    let that=this
    let param = {
      shopId: app.globalData.currentShop.id
    }
    if (p.goodsId) {
      param.goodsId = p.goodsId
    }
    API.hairdresserList(param, res => {
      console.log(res)
      let userNameList = res.data.map(item => { return item.name })
      let data = {
        userList: res.data,
        userNameList: userNameList
      }
      if (p.merchantUserId) {
        let index = res.data.findIndex(item => item.id == p.merchantUserId)
        if (index != -1) {
          data.userIndex = index
        }
      }
      that.setData(data)
    })
  },
  serviceList: function (p) {
    let that = this
    let param = {
      shopId: app.globalData.currentShop.id
    }
    if (p.merchantUserId) {
      param.merchantUserId = p.merchantUserId
    }
    API.serviceList(param, res => {
      let goodsNameList = res.data.map(item => { return item.name })
      let data = {
        goodsList: res.data,
        goodsNameList: goodsNameList
      }
      if (p.goodsId) {
        let index = res.data.findIndex(item => item.id === p.goodsId)
        if (index != -1) {
          data.goodsIndex = index
        }
      }
      that.setData(data)
    })
  },
  goodsChange: function(e){
    this.setData({
      goodsIndex:e.detail.value
    })
  },
  dateChange: function(e){
    let appointment=this.data.appointment
    appointment.arriveDate = e.detail.value
    this.setData({
      appointment: appointment
    })
  },
  timeChange: function(e){
    let appointment = this.data.appointment
    appointment.arriveTime = e.detail.value
    this.setData({
      appointment: appointment
    })
  },
  userChange: function(e){
    this.setData({
      userIndex: e.detail.value
    })
  },
  //验证函数
  initValidate() {
    const rules = {
      appointmentId: {
        required: true
      },
      arriveDate: {
        required: true
      },
      arriveTime: {
        required: true
      },
      appointName: {
        required: true
      },
      appointMobile: {
        required: true,
        tel: true
      }
    }
    const messages = {
      appointmentId: {
        required: 'id不见了'
      },
      arriveDate: {
        required: '请选择预约日期'
      },
      arriveTime: {
        required: '请选择预约时间'
      },
      appointName: {
        required: '请填写预约人称呼'
      },
      appointMobile: {
        required: '请填写预约人手机号',
        tel: '请填写正确的手机号'
      }
    }
    this.WxValidate = new WxValidate(rules, messages)
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let that=this
    this.initValidate()//验证规则函数
    let param={
      appointmentId: options.appointment
    }
    this.getAppointment(param).then(res=>{
      that.setData({
        appointment:res
      })
      that.hairdresserList(res)
      that.serviceList(res)
    })

    let source = options.source
    if(source!=null){
      this.setData({
        source:source
      })
    }
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