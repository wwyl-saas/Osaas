import * as echarts from '../../components/ec-canvas/echarts';

const app = getApp();

function initChart(canvas, width, height) {
  const chart = echarts.init(canvas, null, {
    width: width,
    height: height
  });
  canvas.setChart(chart);

  var option = {
    calculable: true,
    series: [
      {
        name: '漏斗图',
        type: 'funnel',
        left: '5%',
        right: '5%',
        top: '5%',
        bottom: '40%',
        width: '90%',
        height: '60%',
        min: 0,
        max: 100,
        minSize: '0%',
        maxSize: '100%',
        sort: 'descending',
        gap: 2,
        label: {
          show: true,
          position: 'inside'
        },
        labelLine: {
          length: 10,
          lineStyle: {
            width: 1,
            type: 'solid'
          }
        },
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 1
        },
        emphasis: {
          label: {
            fontSize: 20
          }
        },
        data: [
          { value: 60, name: '激活' },
          { value: 40, name: '充值' },
          { value: 20, name: '下单' },
          { value: 80, name: '授权' },
          { value: 100, name: '访问' }
        ]
      }
    ]
  };

  chart.setOption(option);
  return chart;
}


Page({
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
});
