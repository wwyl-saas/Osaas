import Taro, {Component, Config} from '@tarojs/taro'
import {Provider} from '@tarojs/redux'
import '@tarojs/async-await'
import 'taro-ui/dist/style/index.scss'
import configStore from './store'
import Index from './pages/index/index.js'
import './app.scss'
import './assets/fonts/iconfont.css'
import {API_MEMBER_ACTIVE, applicationId} from './constants/api'

const store = configStore()

// 如果需要在 h5 环境中开启 React Devtools
// 取消以下注释：
// if (process.env.NODE_ENV !== 'production' && process.env.TARO_ENV === 'h5')  {
//   require('nerv-devtools')
// }
class App extends Component {
  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config: Config = {
    pages: [
      'pages/index/index',
      'pages/myCoupon/index',
      'pages/vipBill/index',
      'pages/vip/index',
      'pages/evaluate/index',
      'pages/feedback/index',
      'pages/myOrder/index',
      'pages/payOther/index',
      'pages/evaluateAll/index',
      'pages/personalCenter/index',
      'pages/serviceRecords/index',
      'pages/orderDetail/index',
      'pages/payment/index',
      'pages/appointment/index',
      'pages/serverProject/index',
      'pages/stylist/index',
      'pages/serverDetail/index',
      'pages/serverClass/index',
      'pages/categorySearch/index'
    ],
    "permission": {
      "scope.userLocation": {
        "desc": "你的位置信息将用于小程序定位最近门店"
      }
    },
    window: {
      backgroundTextStyle: 'light',
      navigationBarBackgroundColor: '#fff',
      navigationBarTitleText: 'WeChat',
      navigationBarTextStyle: 'black'
    },
    tabBar: {
      color: "#666",
      selectedColor: "#9D9063",
      backgroundColor: "#fafafa",
      borderStyle: 'black',
      list: [{
        pagePath: "pages/index/index",
        iconPath: "./assets/tab-bar/home.png",
        selectedIconPath: "./assets/tab-bar/home-active.png",
        text: "主页"
      }, {
        pagePath: "pages/serverClass/index",
        iconPath: "./assets/tab-bar/cate.png",
        selectedIconPath: "./assets/tab-bar/cate-active.png",
        text: "分类"
      }, {
        pagePath: "pages/appointment/index",
        iconPath: "./assets/tab-bar/cart.png",
        selectedIconPath: "./assets/tab-bar/cart-active.png",
        text: "预约"
      }, {
        pagePath: "pages/personalCenter/index",
        iconPath: "./assets/tab-bar/user.png",
        selectedIconPath: "./assets/tab-bar/user-active.png",
        text: "我的"
      }]
    },
    navigateToMiniProgramAppIdList: ['wxeb490c6f9b154ef9']
  }

  componentWillMount() {
  }

  componentDidMount() {
  }

  componentDidShow() {
    console.log('会员卡', this.$router.params)
    const data = this.$router.params
    // 来自会员卡组件
    if (data.referrerInfo && data.referrerInfo.appId == 'wxeb490c6f9b154ef9') {
      console.log("来自会员卡组件")
      if (data.referrerInfo.extraData && data.referrerInfo.extraData != 'undefined' && data.referrerInfo.extraData !== '<Undefined>') {
        // 若用户激活成功，可以从 data.referrerInfo.extraData 中获取 activate_ticket，card_id，code 参数用于下一步操作
        console.log(data.referrerInfo.extraData)
        let extraData = data.referrerInfo.extraData
        const header = {'applicationId': applicationId}
        header['content-type'] = 'application/x-www-form-urlencoded'
        header['token'] = Taro.getStorageSync('token')
        Taro.request({
          url: API_MEMBER_ACTIVE,
          method: 'POST',
          data: {
            activateTicket: extraData.activate_ticket,
            cardId: extraData.card_id,
            code: extraData.code
          },
          header
        }).then(async (res) => {
          const { code, data, msg } = res.data
          if (code === 0) { // 成功
            console.log('res3', data)
            Taro.setStorage({
              key: 'customer',
              data: data
            }).then(()=>{
              Taro.reLaunch({
                url: '/pages/personalCenter/index'
              })
            })
          } else {
            Taro.showToast({
              title: msg,
              icon: 'none'
            })
          }
        }).catch((err) => {
          Taro.showToast({
            title: err && err.msg,
            icon: 'none'
          })
        })
      }
    }
  }

  componentDidHide() {
  }

  componentDidCatchError() {
  }

  // 在 App 类中的 render() 函数没有实际作用
  // 请勿修改此函数
  render() {
    return (
      <Provider store={store}>
        <Index />
      </Provider>
    )
  }
}

Taro.render(<App />, document.getElementById('app'))
