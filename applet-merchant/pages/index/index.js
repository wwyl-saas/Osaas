Page({
  data: {
    PageCur: 'home'
  },
  NavChange(e) {
    this.setData({
      PageCur: e.currentTarget.dataset.cur
    })
  },
  onShareAppMessage() {
    return {
      title: '优联美业商家端',
      imageUrl: 'http://cdn.wanwuyoulian.com/login-bg.png',
      path: '/pages/index/index'
    }
  },
})