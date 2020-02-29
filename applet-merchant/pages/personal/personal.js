import API from '../../api/employee.js'
import UPLOAD from '../../api/common.js'
var util = require('../../utils/util')
const app = getApp();

const Tucao = requirePlugin('tucao').default;
if (this) {
  Tucao.init(this);
} else {
  Tucao.set({
    navigateTo: wx.navigateTo
  });
}

Component({
  options: {
    addGlobalClass: true,
  },
  /**
   * 页面的初始数据
   */
  data: {
    currentShop: {},
    user:{},
    grade:0,
    appointmentCount: 0,
    serviceCount: 0,
    saleTotal: 0,
  },
  attached: function () {
    const that = this
    this.setData({
      currentShop: app.globalData.currentShop
    })
    let data = {
      shopId: app.globalData.currentShop.id
    }
    API.personalCenter(data, function (res) {
      let data={
        user: res.data
      }
      let grade = res.data.gradeString
      if(grade>=1){
        data.star1=1
      }
      if(grade>=2){
        data.star2 = 1
      }
      if (grade >= 3) {
        data.star3 = 1
      }
      if (grade >= 4) {
        data.star4 = 1
      }
      if (grade >= 5) {
        data.star5 = 1
      }
      that.setData(data)
    })

    API.getUserIndex(function(res){
      console.log(res.data)
      that.setIndexData(res.data)
    })

  },
  methods: {
    setIndexData: function(data){
      const that=this
      let minData=0
      if (data.rechargeAmount < data.serviceTime){
        minData = data.rechargeAmount
      }else{
        minData = data.serviceTime
      }
      if (minData > data.serviceAmount){
        minData = data.serviceAmount
      }

      let i = 0;
      numDH();
      function numDH() {
        if (i < minData) {
          setTimeout(function () {
            that.setData({
              appointmentCount: i,
              serviceCount: i,
              saleTotal: i
            })
            i=i+10
            numDH();
          }, 20)
        } else {
          that.setData({
            appointmentCount: data.serviceAmount,
            serviceCount: data.serviceTime,
            saleTotal: data.rechargeAmount
          })
        }
      }

    },
    showModal: function (e) {
      console.log(e)
      this.setData({
        modalName: e.currentTarget.dataset.target
      })
    },
    hideModal: function (e) {
      this.setData({
        modalName: null
      })
    },
    changeWorkName: function (e) {
      const that=this
      let data={
        nickname: e.detail.value.nickname
      }
      API.updateUserSelf(data,function(res){
        let user=that.data.user
        user.nickname = e.detail.value.nickname
        that.setData({
          modalName: null,
          user:user
        })
      })
    },
    changeUserBrief: function (e) {
      const that = this
      let data = {
        userBrief: e.detail.value.userBrief
      }
      API.updateUserSelf(data, function (res) {
        let user = that.data.user
        user.userBrief = e.detail.value.userBrief
        that.setData({
          modalName: null,
          user: user
        })
      })
    },
    changeAvatar: function () {
      var that = this;
      wx.chooseImage({
        count: 1, // 默认9
        sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
          let user=that.data.user
          user.profileUrl = res.tempFilePaths[0]
          // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
          that.setData({//赋值应该使用冒号，因为里面是对象
            user: user
          })
          UPLOAD.uploadImage(res.tempFilePaths[0],function(res1){
            console.log(res1)
            let data={
              profileUrl:res1.data
            }
            API.updateUserSelf(data,function(res2){
              console.log(res2)
            })
          })
        }
      })
    },
    logout: function (){
      API.logout(function(res){
        wx.redirectTo({
          url: '/pages/login/login',
        })
      })
    },
    tucao:function(e){
      const userInfo = {
        avatar: this.data.user.profileUrl,
        nickname: this.data.user.name,
        openid: this.data.user.id,
        productId: 77178
      };
      Tucao.set(userInfo);
      Tucao.go();
    },
    switchIfAppointment: function (e) {
      const that = this
      let data = {
        ifAppointment: e.detail.value
      }
      API.updateUserSelf(data, function (res) {
        let user = that.data.user
        user.ifAppointment = e.detail.value
        that.setData({
          user: user
        })
      })
    },
  }
})