import * as echarts from '../../components/ec-canvas/echarts';

const app = getApp();

function initChart(canvas, width, height) {
  const chart = echarts.init(canvas, null, {
    width: width,
    height: height
  });
  canvas.setChart(chart);

  var option = {
    series: [
      {
        name: '访问来源',
        type: 'pie',
        radius: '60%',
        center: ['50%', '30%'],
        data: [
          { value: 335, name: '男性' },
          { value: 310, name: '女性' },
          { value: 234, name: '未知' }
        ]
      }
    ]
  };

  chart.setOption(option);
  return chart;
}



Page({

  /**
   * 页面的初始数据
   */
  data: {
    ec: {}
  },
  echartInit(e) {
    initChart(e.detail.canvas, e.detail.width, e.detail.height);
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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