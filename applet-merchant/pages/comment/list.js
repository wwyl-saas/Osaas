var util = require('../../utils/util.js');
import API from '../../api/employee.js'
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    CustomBar: app.globalData.CustomBar,
    TabCur: 0,
    scrollLeft: 0,
    startDate: util.futureDate(-31),
    endDate: util.todayDate(),
    commentList:[],
    commentType:-1,
    createDateStart:null,
    createDateEnd:null,
    havePicture:null,
    loaded:false
  },
  bindDateChange: function (e) {
    this.setData({
      date: e.detail.value
    })
  },
  selectCommentType:function(e){
    let commentType=e.currentTarget.dataset.target
    this.setData({
      commentType: commentType
    })
    this.getCommentList()
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
  haveCommentPicture: function(e){
    let flag = e.currentTarget.dataset.value
    this.setData({
      havePicture: flag
    })
  },
  cleanCondition: function (e) {
    this.setData({
      commentType: -1,
      createDateStart: null,
      createDateEnd: null,
      havePicture: null
    })
    this.getCommentList()
  },
  submitCondition: function (e) {
    this.getCommentList()
    this.setData({
      modalName: null
    })
  },
  getCommentList:function(){
    wx.showLoading({
      mask: true,
      title: '加载中...',
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 2000)
    let that = this
    let param={}
    if (this.data.commentType>=0){
      param.commentType = this.data.commentType
    }
    if (this.data.havePicture != null) {
      param.havePicture = this.data.havePicture
    }
    if (this.data.createDateStart != null && this.data.createDateEnd!=null){
      param.createDateStart = this.data.createDateStart
      param.createDateEnd = this.data.createDateEnd
    }
    API.getCommentList(param, function (res) {
      let list = res.data.results
      for (let comment of list) {
        let grade = comment.gradeString
        if (grade >= 1) {
          comment.star1 = 1
        }
        if (grade >= 2) {
          comment.star2 = 1
        }
        if (grade >= 3) {
          comment.star3 = 1
        }
        if (grade >= 4) {
          comment.star4 = 1
        }
        if (grade >= 5) {
          comment.star5 = 1
        }
      }
      that.setData({
        commentList: list,
        pageIndex: res.data.pageIndex,
        pageSize: res.data.pageSize,
        totalPage: res.data.totalPage,
        loaded:true
      })
      wx.hideLoading()
    })
    wx.hideLoading()
  },
  bindStartDateChange: function (e) {
    let startDate = e.detail.value
    let endDate = this.data.createDateEnd

    if (endDate) {
      let start = util.toDate(startDate)
      let end = util.toDate(endDate)
      if (start > end) {
        util.mytoast('结束日期不能早于起始日期', 'none');
        return false;
      }
    }
    this.setData({
      createDateStart: startDate
    })
  },
  bindEndDateChange: function (e) {
    let startDate = this.data.createDateStart
    let endDate = e.detail.value

    if (startDate) {
      let start = util.toDate(startDate)
      let end = util.toDate(endDate)
      if (start > end) {
        util.mytoast('结束日期不能早于起始日期', 'none');
        return false;
      }
    }
    this.setData({
      createDateEnd: e.detail.value
    })
  },
  /**
 * 生命周期函数--监听页面加载
 */
  onLoad: function (options) {
    this.getCommentList()
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
    let that = this
    if (this.data.pageIndex < this.data.totalPage) {
      let data = {
        pageIndex: this.data.pageIndex+1,
        pageSize: this.data.pageSize
      }
      if (this.data.commentType >= 0) {
        data.commentType = this.data.commentType
      }
      if (this.data.havePicture != null) {
        data.havePicture = this.data.havePicture
      }
      if (this.data.createDateStart != null && this.data.createDateEnd != null) {
        data.createDateStart = this.data.createDateStart
        data.createDateEnd = this.data.createDateEnd
      }
      API.getCommentList(data, function (res) {
        let list1 = res.data.results
        for (let comment of list1) {
          let grade = comment.gradeString
          if (grade >= 1) {
            comment.star1 = 1
          }
          if (grade >= 2) {
            comment.star2 = 1
          }
          if (grade >= 3) {
            comment.star3 = 1
          }
          if (grade >= 4) {
            comment.star4 = 1
          }
          if (grade >= 5) {
            comment.star5 = 1
          }
        }
        let list = that.data.commentList
        list=list.concat(list1)
        that.setData({
          commentList: list,
          pageIndex: res.data.pageIndex,
          pageSize: res.data.pageSize,
          totalPage: res.data.totalPage
        })
      })
    }else{
      
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})