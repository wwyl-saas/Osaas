import API from '../../api/index.js'
const app = getApp();

Component({
  options: {
    addGlobalClass: true,
  },
  data: {
    iconList: [],
    shopDataList: [],
    userDataList:[],
    flag:false,
    attention:false
  },
  //初始加载
  attached: function() {
   if(!this.data.flag){
     this.setData({
       flag: true
     })
     this.getIndexInfo();
   }
  }, 
  // 在组件实例被从页面节点树移除时执行
  detached: function () {
    this.setData({
      flag: false
    })
  },
  pageLifetimes: {
    show: function () {
      if (!this.data.flag) {
        this.setData({
          flag: true
        })
        this.getIndexInfo();
      }
    },
    hide: function () {
      this.setData({
        flag: false
      })
    }
  },
  methods: {
    getLocation: function() {
      const page=this
      return new Promise((resolve, reject) => {
        wx.getLocation({
          type: 'wgs84', // 默认wgs84
          success: function(res) {
            resolve(res)
          },
          fail: function(res) {
            page.setData({
              modalName: 'ShopModal'
            })
            reject("拒绝定位")
          }
        });
      })
    },
    getShopTodayData: function(param) {
      return new Promise((resolve, reject) => {
        let data = {
          shopId: param
        }
        API.getShopTodayData(data, function(res) {
          resolve(res.data)
        })
      })
    },
    getUserTodayData: function (param) {
      return new Promise((resolve, reject) => {
        let data = {
          shopId: param
        }
        API.getUserTodayData(data, function (res) {
          resolve(res.data)
        })
      })
    },
    getIndexInfo:function(){
      wx.showLoading({
        mask: true,
        title: '加载中...',
      })
      setTimeout(function () {
        wx.hideLoading()
      }, 2000)
      let component = this
      let data = {}
      app.getShopList().then(res => {
        data.shopList = res
        return component.getLocation()
      }).then(res => {
        return app.getNearestShop(res)
      }).then(res => {
        data.currentShop = res
        return app.getNavigatorList()
      }).then(res => {
        data.iconList = res
        component.setData(data)
        return component.getShopTodayData(data.currentShop.id)
      }).then(res => {
        data.shopDataList = res
        return component.getUserTodayData(data.currentShop.id)
      }).then(res=>{
        data.userDataList = res
        component.setData(data)
        wx.hideLoading()
      }).catch(result => {
        console.log(result)
        wx.hideLoading()
      })
    },
    showModal: function(e) {
      this.setData({
        modalName: e.currentTarget.dataset.target
      })
    },
    hideModal: function(e) {
      this.setData({
        modalName: null
      })
    },
    shopChange: function(e) {
      wx.showLoading({
        mask: true,
        title: '加载中...',
      })
      setTimeout(function () {
        wx.hideLoading()
      }, 2000)
      var shop = app.globalData.shopList.filter(shop => {
        return shop.id == e.detail.value
      })
      app.globalData.currentShop = shop[0]
      app.globalData.navigatorList =[]
      app.globalData.serviceList = []
      app.globalData.userList = []
      let component=this;
      let data={
        currentShop: shop[0]
      }
      app.getNavigatorList().then(res => {
        data.iconList = res
        return component.getShopTodayData(shop[0].id)
      }).then(res => {
        data.shopDataList=res
        return component.getUserTodayData(shop[0].id)
      }).then(res=>{
        data.userDataList = res
        component.setData(data)
        wx.hideLoading()
      }).catch(result => {
        wx.hideLoading()
        console.log(result)
      })
    },
  }
})

