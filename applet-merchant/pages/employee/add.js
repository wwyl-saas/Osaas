import API from '../../api/employee.js'
import WxValidate from '../../utils/validate.js'
var util = require('../../utils/util')
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    positionNameList:[],
    positionList:[],
    roleNameList:[],
    roleList:[],
    mobile:null,
    serviceList: [],
    ifAppointment:true
  },
  formSubmit: function (e) {
    const params = e.detail.value
    if (!this.WxValidate.checkForm(params)) {
      const error = this.WxValidate.errorList[0]
      util.mytoast(error.msg,'none')
      return false
    }

    let that = this
    let data={
      name:params.name,
      nickname:params.nickname,
      mobile:params.mobile,
      shopId: app.globalData.currentShop.id,
      postTitleId: that.data.positionList[params.positionIndex].code,
      roleTypeCode: that.data.roleList[params.roleIndex].code,
      ifAppointment: that.data.ifAppointment
    }
    if(params.id!=""){
      data.id=params.id
    }
    let goodsIds = that.data.serviceList.filter(item=> item.checked);
    goodsIds=goodsIds.map(item=>item.code)
    data.goodsIds=goodsIds

    API.saveUser(data, function (res) {
      util.mytoast('保存成功', 'none')
      wx.navigateBack({
        delta: 1
      })
    })
  },
  onPositionChange: function (e) {
    this.setData({
      positionIndex: e.detail.value
    })
  },
  onRoleChange: function (e) {
    this.setData({
      roleIndex: e.detail.value
    })
  },
  //验证函数
  initValidate() {
    const rules = {
      name: {
        required: true,
        minlength: 2
      },
      nickname: {
        required: false,
      },
      mobile:{
        required: true,
        tel: true
      },
      positionIndex:{
        required: true,
        number: true
      },
      roleIndex:{
        required: true,
        number: true
      }
    }
    const messages = {
      name: {
        required: '请填写姓名',
        minlength: '请输入正确的名称'
      },
      mobile: {
        required: '请填写手机号',
        tel: '请填写正确的手机号'
      },
      positionIndex: {
        required: '请选择职称'
      },
      roleIndex: {
        required: '请选择角色权限'
      }
    }
    this.WxValidate = new WxValidate(rules, messages)
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
  ChooseCheckbox(e) {
    let items = this.data.serviceList;
    let code = e.currentTarget.dataset.value;
    for (let i = 0, lenI = items.length; i < lenI; ++i) {
      if (items[i].code == code) {
        items[i].checked = !items[i].checked;
        break
      }
    }
    this.setData({
      serviceList: items
    })
  },
  inputMobile: function (e) {
    let mobile = e.detail.value
    this.setData({
      mobile: mobile
    })
  },
  checkMobile: function (mobile) {
    let str = /^1[3456789]\d{9}$/
    if (str.test(mobile)) {
      return true
    } else {
      util.mytoast('手机号不正确', 'none')
      return false
    }
  },
  searchUser: function(e){
    let that=this
    let mobile = this.data.mobile
    if (mobile>10000000000){
      let checkedNum = this.checkMobile(mobile)
      if (checkedNum) {
        let data={
          mobile: mobile,
          shopId:app.globalData.currentShop.id
        }
        API.getDetailByMobile(data,res=>{
          let data=res.data
          let index = that.data.positionList.findIndex(item => item.code === res.data.postTitleId)
          if (index != -1) {
            data.positionIndex=index
          }
          let serviceList = that.data.serviceList
          for (let i = 0; i < res.data.goodsIds.length;i++){
            let index = serviceList.findIndex(item => item.code === res.data.goodsIds[i])
            if (index != -1) {
              serviceList[index].checked=true
            }
          }
          data.serviceList = serviceList
          that.setData(data)
        })
      } else {
        this.setData({
          mobile: ''
        })
      }
    }else{
      util.mytoast('请输入导入员工手机号','none')
    }
  },
  switchIfAppointment: function(e){
    this.setData({
      ifAppointment: e.detail.value
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let that=this
    this.initValidate()//验证规则函数

    app.getPositionList().then(res=>{
      let positionNameList = res.map(item => { return item.name })
      that.setData({
        positionList: res,
        positionNameList: positionNameList
      })
    })
    app.getRoleList().then(function (res) {
      let roleNameList = res.map(item => { return item.name })
      that.setData({
        roleList: res,
        roleNameList: roleNameList
      })
    })
    app.getServiceList().then(res=>{
      that.setData({
        serviceList: res
      })
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
    this.data.serviceList.forEach(item => {
      item.checked = false
    })
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