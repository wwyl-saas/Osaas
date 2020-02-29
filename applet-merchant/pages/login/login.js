import API from '../../api/index.js'
var util = require('../../utils/util')

Page({
  data: {
    send: true,
    second: 60,
    mobile:'',
    submit:false
  },
  getSmsCode: function(e){
    var mobile = this.data.mobile
    if(mobile.length === 11){
      var data = {
        phone: mobile
      }
      API.getSmsCode(data, function (res) {
        util.mytoast('短信已发送，请注意查收', 'none')
      })
      this.setData({
        send: false
      })
      this.timer()
    }
  },
  inputMobile: function (e) {
    let mobile = e.detail.value
    if (mobile.length === 11) {
      let checkedNum = this.checkMobile(mobile)
      if (checkedNum) {
        this.setData({
          mobile: mobile
        })
        if(this.data.smsCode){
          this.setData({
            submit: true
          })
        }
      }else{
        this.setData({
          mobile: ''
        })
      } 
    }else{
      this.setData({
        submit: false
      })
    }
  },
  checkMobile: function (mobile) {
    let str = /^1[3456789]\d{9}$/
    if (str.test(mobile)) {
      return true
    } else {
      util. mytoast('手机号不正确', 'none')
      return false
    }
  },
  timer: function () {
    let promise = new Promise((resolve, reject) => {
      let setTimer = setInterval(
        () => {
          this.setData({
            second: this.data.second - 1
          })
          if (this.data.second <= 0) {
            this.setData({
              second: 60,
              send: true
            })
            resolve(setTimer)
          }
        }
        , 1000)
    })
    promise.then((setTimer) => {
      clearInterval(setTimer)
    })
  },
  bindHideKeyboard: function (e) {
    if (e.detail.value.length >= 6) {
      // 收起键盘
      wx.hideKeyboard()
      this.setData({
        smsCode: e.detail.value
      })
      if(this.data.mobile.length === 11){
        this.setData({
          submit: true
        })
      }
    } else {
      this.setData({
        submit: false
      })
    }
  },
  formSubmit: function (e) {
    // 登录
    wx.login({
      success: res => {
        var data = {
          loginCode: res.code,
          phone: e.detail.value.mobile,
          smsCode: e.detail.value.smsCode
        }
        API.login(data, function (res) {
          console.log(res);
          let token = res.data;
          if (token) {
            wx.setStorage({ key: "token", data: token })
            wx.redirectTo({
              url: "/pages/index/index"
            })
          }
        }, function () {
          wx.hideLoading()
        })
      }
    })

    //导航到首页

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      submit: false
    })
    let token = wx.getStorageSync('token');
    if (token) {
      wx.hideLoading()
      wx.redirectTo({
        url: "/pages/index/index"
      })
    }else{
      wx.showLoading({
        mask: true,
        title: '身份校验中...',
      }),
      // 尝试登录
      wx.login({
        success: res => {
          var data = {
            loginCode: res.code
          }
          API.tryLogin(data, function (res) {
            let token = res.data;
            if (token) {
              wx.setStorage({ key: "token", data: token })
              wx.hideLoading()
              wx.redirectTo({
                url: "/pages/index/index"
              })
            }
          }, function () {
            wx.hideLoading()
          })
        }
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
    let token = wx.getStorageSync('token');
    if (token) {
      wx.redirectTo({
        url: "/pages/index/index"
      })
    }
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