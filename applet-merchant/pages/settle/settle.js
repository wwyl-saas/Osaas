var util = require('../../utils/util.js');
import API from '../../api/settle.js'
import SocketApi from '../../api/socket.js'
const app = getApp()
// pages/settle/settle.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    Custom: app.globalData.Custom,
    TabCur: 0,
    MainCur: 0,
    VerticalNavTop: 0,
    load: true,
    merchantUser: {},
    list: [],
    listCur: {},
    selectedGoodsList: [],
    selectedGoodsAmount: 0,
    selectedGoodsNum: 0,
    payStatus:0,
    userList: [],
    userNameList: [],
    userIndex: null,
  },
  onLoad() {
    const that = this
    let data = {
      shopId: app.globalData.currentShop.id
    }
    this.getGoodsList().then(res=>{
      let categoryList = res.categorys
      let bestSellGoodsIds = res.bestSell
      let list = {
        id: 0,
        name: '畅销列表',
        goodsList: []
      }
      //保证快捷列表和分类列表的相同项目为同一个对象
      for (let category of categoryList) {
        let goodsList = category.goodsList
        for (let goods of goodsList) {
          let index = bestSellGoodsIds.findIndex(item => item == goods.id)
          if (index != -1) {
            list.goodsList.push(goods)
          }
        }
      }
      categoryList.unshift(list)
      that.setData({
        list: categoryList,
        listCur: categoryList[0]
      })
      return that.getUserDropList()
    }).then(res=>{
      let userList = res
      let userNameList = res.map(item => {
        return item.name
      })
      that.setData({
        userList: userList,
        userNameList: userNameList
      })
      app.getCurrentUser().then(res1=>{
        let data = {
          merchantUser: res1
        }
        let index = userList.findIndex(item => item.code == res1.id)
        if (index != -1) {
          data.userIndex = index
        }
        that.setData(data)
      })
    }).catch(res=>{
      console.log(res)
    })

    SocketApi.reconnect()
    const watchIndex = SocketApi.addWatcher(this.onSocketMessage)
    this.setData({
      watchIndex
    })
  },
  onReady() {

  },

  getGoodsList(){
    return new Promise((resolve, reject) => {
      let data = {
        shopId: app.globalData.currentShop.id
      }
      API.getGoodsList(data, function (res) {
        if (res.data) {
          resolve(res.data)
        } else {
          reject('返回为空')
        }
      })
    })
  },
  getUserDropList(){
  return new Promise((resolve, reject) => {
    let data = {
      shopId: app.globalData.currentShop.id
    }
    API.getUserDropList(data, function (res) {
      if (res.data) {
        resolve(res.data)
      } else {
        reject('返回为空')
      }
    })
  })
  },
  tabSelect(e) {
    this.setData({
      TabCur: e.currentTarget.dataset.id,
      MainCur: e.currentTarget.dataset.id,
      VerticalNavTop: (e.currentTarget.dataset.id - 1) * 50
    })
  },
  VerticalMain(e) {
    let that = this;
    let list = this.data.list;
    let tabHeight = 0;
    if (this.data.load) {
      for (let i = 0; i < list.length; i++) {
        let view = wx.createSelectorQuery().select("#main-" + i);
        view.fields({
          size: true
        }, data => {
          list[i].top = tabHeight;
          tabHeight = tabHeight + data.height;
          list[i].bottom = tabHeight;
        }).exec();
      }
      that.setData({
        load: false,
        list: list
      })
    }
    let scrollTop = e.detail.scrollTop + 20;
    for (let i = 0; i < list.length; i++) {
      if (scrollTop > list[i].top && scrollTop < list[i].bottom) {
        that.setData({
          VerticalNavTop: (list[i].id - 1) * 50,
          TabCur: list[i].id
        })
        return false
      }
    }
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
  hideSettleModal(e) {
    const that=this
    this.setData({
      modalName: null,
      orderId: null,
      selectedGoodsList: [],
      selectedGoodsAmount: 0,
      selectedGoodsNum: 0,
      payStatus: 0,
    })
    this.getGoodsList().then(res=>{
      let categoryList = res.categorys
      let bestSellGoodsIds = res.bestSell
      let list = {
        id: 0,
        name: '畅销列表',
        goodsList: []
      }
      //保证快捷列表和分类列表的相同项目为同一个对象
      for (let category of categoryList) {
        let goodsList = category.goodsList
        for (let goods of goodsList) {
          let index = bestSellGoodsIds.findIndex(item => item == goods.id)
          if (index != -1) {
            list.goodsList.push(goods)
          }
        }
      }
      categoryList.unshift(list)
      that.setData({
        list: categoryList,
        listCur: categoryList[0]
      })
    })
  },
  bindUserChange(e) {
    let index = e.detail.value
    this.setData({
      merchantUser: this.data.userList[index],
      userIndex: index
    })
  },
  scanQrCode() {
    const page = this;
    if (this.data.selectedGoodsList.length > 0) {
      wx.scanCode({
        onlyFromCamera: false,
        scanType: ["qrCode"],
        success(res) {
          page.setData({
            modalName: "settleModal"
          })
          console.log(res)
          let data = {
            code: res.result,
            goods: page.data.selectedGoodsList,
            shopId: app.globalData.currentShop.id
          }
          if (page.data.userIndex) {
            data.serviceUserId = page.data.userList[page.data.userIndex].code
          } else {
            data.serviceUserId = page.data.merchantUser.id
          }
          console.log(data)
          API.qrCodePlace(data, function (res) {
            console.log(res.data)
            page.setData({
              couponAmount: res.data.couponAmount,
              discountAmount: res.data.discountAmount,
              payAmount: res.data.payAmount,
              orderId:res.data.id,
              payStatus:2
            })
          }, function (res) {
            page.setData({
              payStatus: null
            })
          })
        },
        fail(res) {
          console.log(res);
          util.mytoast('扫码失败', 'none')
        }
      })
    } else {
      util.mytoast('请选择商品下单', 'none')
    }
  },
  scanBarCode() {
    const page = this;
    if (this.data.selectedGoodsList.length >0){
      wx.scanCode({
        onlyFromCamera: true,
        scanType: ["barCode"],
        success(res) {
          page.setData({
            modalName: "settleModal"
          })
          let data = {
            code: res.result,
            goods: page.data.selectedGoodsList,
            shopId: app.globalData.currentShop.id
          }
          if (page.data.userIndex){
            data.serviceUserId = page.data.userList[page.data.userIndex].code
          }else{
            data.serviceUserId = page.data.merchantUser.id
          }
          API.barcodePlace(data,function(res){
            console.log(res)
            page.setData({
              couponAmount: res.data.couponAmount,
              discountAmount: res.data.discountAmount,
              payAmount: res.data.payAmount,
              orderId: res.data.id,
            })
          },function(res){
            console.log(res)
            page.setData({
              payStatus: null
            })
          })
        },
        fail(res) {
          console.log(res);
          util.mytoast('扫码失败', 'none')
        }
      })
    }else{
      util.mytoast('请选择商品下单','none')
    }
  },
  goodsRemove: function(e) {
    let list = this.data.list
    let categoryIndex = e.currentTarget.dataset.index
    let goodsIndex = e.currentTarget.dataset.goods
    let goods = list[categoryIndex].goodsList[goodsIndex]
    if (goods.goodsNum && goods.goodsNum > 0) {
      goods.goodsNum = goods.goodsNum - 1
      list[categoryIndex].goodsList.splice(goodsIndex, 1, goods) //替换
      let selectedGoodsNum = this.data.selectedGoodsNum - 1
      let selectedGoodsAmount = util.sub(this.data.selectedGoodsAmount, goods.counterPrice)
      let selectedGoodsList = this.data.selectedGoodsList
      let index = selectedGoodsList.findIndex(item => item.id == goods.id)
      if (index != -1) {
        if (selectedGoodsList[index].goodsNum == 0) {
          selectedGoodsList.splice(index, 1) //删除
        }
      } else {
        console.log("大错特错")
        console.log(goods)
        console.log(selectedGoodsList)
      }
      this.setData({
        selectedGoodsNum: selectedGoodsNum,
        selectedGoodsAmount: selectedGoodsAmount,
        selectedGoodsList: selectedGoodsList,
        list: list
      })
    }
  },
  goodsAdd: function(e) {
    let list = this.data.list
    let categoryIndex = e.currentTarget.dataset.index
    let goodsIndex = e.currentTarget.dataset.goods
    let goods = list[categoryIndex].goodsList[goodsIndex]
    if (goods.goodsNum) {
      goods.goodsNum = goods.goodsNum + 1
    } else {
      goods.goodsNum = 1
    }
    list[categoryIndex].goodsList.splice(goodsIndex, 1, goods) //替换
    let selectedGoodsNum = this.data.selectedGoodsNum + 1
    let selectedGoodsAmount = util.add(this.data.selectedGoodsAmount, goods.counterPrice)
    let selectedGoodsList = this.data.selectedGoodsList
    let index = selectedGoodsList.findIndex(item => item.id == goods.id)
    if (index == -1) {
      selectedGoodsList.push(goods)
    }
    this.setData({
      selectedGoodsNum: selectedGoodsNum,
      selectedGoodsAmount: selectedGoodsAmount,
      selectedGoodsList: selectedGoodsList,
      list: list
    })
  },


  onSocketMessage(res){
    const that=this
    console.log(res)
    if (this.data.modalName === 'settleModal' && this.data.orderId === res.orderId){
        let data = {
          selectedGoodsAmount: res.sumAmount,
          couponAmount: res.couponAmount,
          discountAmount: res.discountAmount,
          payAmount: res.payAmount
        }
        if (res.result) {
          data.payStatus = 2
        } else {
          if(res.stage===0){
            data.payStatus = 0
          }else if(res.stage===1){
            data.payStatus = 1
          }else if(res.stage===2){
            data.payStatus = 3
          }
        }
        this.setData(data)
    }
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
    console.log("onUnload")
    SocketApi.delWatcher(this.data.watchIndex)
    SocketApi.closeSocket()
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