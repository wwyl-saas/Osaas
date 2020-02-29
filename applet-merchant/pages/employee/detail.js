import API from '../../api/employee.js'
import WxValidate from '../../utils/validate.js'
var util = require('../../utils/util')
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    user:{},
    positionNameList: [],
    positionList: [],
    roleNameList: [],
    roleList: [],
    serviceList: [],
  },
  formSubmit: function (e) {
    let that=this
    const params = e.detail.value
    if (!this.WxValidate.checkForm(params)) {
      const error = this.WxValidate.errorList[0]
      util.mytoast(error.msg, 'none')
      return false
    }

    let data = {
      id:params.id,
      name: params.name,
      nickname: params.nickname,
      mobile: params.mobile,
      shopId: app.globalData.currentShop.id,
      postTitleId: that.data.positionList[params.positionIndex].code,
      roleTypeCode: that.data.roleList[params.roleIndex].code,
      ifAppointment: that.data.user.ifAppointment
    }
    let goodsIds = that.data.serviceList.filter(item => item.checked);
    goodsIds = goodsIds.map(item => item.code)
    data.goodsIds = goodsIds
    API.updateUser(data, function (res) {
      util.mytoast('修改成功', 'none')
      wx.navigateBack({
        delta: 1
      })
    })
  },
  deleteUser: function(e){
    let userId = e.target.dataset['target']
    let pages = getCurrentPages(); //获取当前页面js里面的pages里的所有信息。
    let prevPage = pages[pages.length - 2];
    let list = prevPage.data.employeeList;
    let data={
      userId: userId,
      shopId: app.globalData.currentShop.id
    }
    API.deleteUser(data,function(res){
      list.splice(list.findIndex(item => item.id == userId), 1)//删除
      prevPage.setData({
        employeeList: list
      })
      util.mytoast('删除成功', 'none')
      wx.navigateBack({
        delta: 1
      })
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
      mobile: {
        required: true,
        tel: true
      },
      positionIndex: {
        required: true,
        number: true
      },
      roleIndex: {
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
  switchIfAppointment: function (e) {
    let user=this.data.user
    user.ifAppointment=e.detail.value
    this.setData({
      user:user
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.showLoading({
      mask: true,
      title: '加载中...',
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 2000)
    let that = this
    this.initValidate()//验证规则函数
    let positionList
    let roleList
    app.getPositionList().then(res => {
      let positionNameList = res.map(item => { return item.name })
      positionList = res
      that.setData({
        positionList: res,
        positionNameList: positionNameList
      })
      return app.getRoleList()
    }).then(function (res) {
      let roleNameList = res.map(item => { return item.name })
      roleList = res
      that.setData({
        roleList: res,
        roleNameList: roleNameList
      })
      return app.getServiceList()
    }).then(res=>{
      that.setData({
        serviceList: res
      })
      let param = {
        userId: options.userId,
        shopId: app.globalData.currentShop.id
      }
      API.getDetail(param, function (res) {
        let data = {
          user: res.data
        }
        let index = positionList.findIndex(item => item.code == res.data.postTitleId)
        if (index != -1) {
          data.positionIndex = index
        }
        index = roleList.findIndex(item => item.code == res.data.roleTypeCode)
        if (index != -1) {
          data.roleIndex = index
        }
        let serviceList = that.data.serviceList
        for (let i = 0; i < res.data.goodsIds.length; i++) {
          let index = serviceList.findIndex(item => item.code === res.data.goodsIds[i])
          if (index != -1) {
            serviceList[index].checked = true
          }
        }
        data.serviceList = serviceList
        that.setData(data)
      })
      wx.hideLoading()
    }).catch(res=>{
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