import API from '../../api/member.js'
import WxValidate from '../../utils/validate.js'
var util = require('../../utils/util')
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    rechargeAmount:0,
    giftAmount:0,
    customer:{},
    source: 'normal'
  },
  inputRechargeAmount: function(e){
    this.setData({
      rechargeAmount: this.money(e.detail.value)  //money匹配金额输入规则，返回输入值
    });
  },
  inputGiftAmount: function (e) {
    this.setData({
      giftAmount: this.money(e.detail.value)  //money匹配金额输入规则，返回输入值
    });
  },
  callPhone(e) {
    console.log(e)
    let phone = e.currentTarget.dataset['phone'];
    wx.makePhoneCall({
      phoneNumber: phone
    })
  },
  money(val) {
    let num = val.toString(); //先转换成字符串类型
    if (num.indexOf('.') == 0) { //第一位就是 .
      num = '0' + num
    }
    num = num.replace(/[^\d.]/g, "");  //清除“数字”和“.”以外的字符
    num = num.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
    num = num.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
    num = num.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
    if (num.indexOf(".") < 0 && num != "") {
      num = parseFloat(num);
    }
    return num
  },
  formSubmit: function(e){
    let that=this
    let rechargeAmount = e.detail.value.rechargeAmount
    let giftAmount = e.detail.value.giftAmount
    if (rechargeAmount <= 0){
      util.mytoast('充值金额须大于0','none')
      return false
    }
    wx.showLoading({
      mask: true,
      title: '加载中...',
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 2000)
    let data={
      shopId: app.globalData.currentShop.id,
      customerId:this.data.customer.id,
      amount:rechargeAmount,
      giftAmount:giftAmount
    }
    API.recharge(data,function(res){
      let customer=that.data.customer
      customer.balance = res.data.balance
      customer.cumulativeRecharge = res.data.cumulativeRecharge
      customer.chargeNum = res.data.chargeNum
      customer.memberLevel = res.data.memberLevel
      that.setData({
        customer:customer,
        modalName: null,
        rechargeAmount: 0,
        giftAmount: 0
      })
      wx.hideLoading()
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
  showToastInfo1(e) {
    wx.showToast({
      title: '实际收款金额',
      icon: 'none',
      duration: 2000
    });
    setTimeout(function () {
      wx.hideToast()
    }, 2000)
  },
  showToastInfo2(e) {
    wx.showToast({
      title: '充返活动赠送额',
      icon: 'none',
      duration: 2000
    });
    setTimeout(function () {
      wx.hideToast()
    }, 2000)
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
    if(options.source){
      this.setData({
        source: options.source
      })
    }
    const that=this
    let id = options.customerId
    let data={
      userId:id
    }
    API.getDetail(data,function(res){
      that.setData({
        customer:res.data
      })
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