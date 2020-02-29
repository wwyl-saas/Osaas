import Taro, { Component } from '@tarojs/taro'
import { View, ScrollView } from '@tarojs/components'
import { connect } from '@tarojs/redux'
import * as actions from '../../actions/product'
import {dispatchAuthorized} from '../../actions/users'
import { getWindowHeight } from '../../utils/style'
import withShare from '../../utils/withShare'
import Banner from './banner'
import Menu from './menu'
import IndexModule from './indexModule'
import IndexFooter from './footer'
import IndexModal from './indexModal'
import Settlement from '../settlement'
import youhuijuan from '../../assets/images/youhuijuan.png'
import barcode from '../../assets/images/barcode.png'
import wodeyuyue from '../../assets/images/wodeyuyue.png'
import './index.scss'
import {API_IMAGE_PAY_BARCODE, API_AUTH_TRY_LOGIN, applicationId, hostS} from '../../constants/api'

const CODE_SUCCESS = 0
const CODE_AUTH_EXPIRED = 4002

@connect(state => ({user: state.user, product: state.product}), { ...actions, dispatchAuthorized })
@withShare({
  title: '优联造型',
  imageUrl: '',
  path: 'pages/index/index?'
})
export default class Index extends Component {
  // $setSharePath = () => ''
  // $setShareTitle = () => ''
  // $setShareImageUrl = () => ''
  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '优联造型',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white'
  }

  state = {
    loading: false,
    hasMore: true,
    homeInfo: {
      hotGoods: {},
      newGoods: {},
      recommendGoods: {},
    },
    banners: [],
    shopData: {},
    selector: [],
    selectorChecked: '',
    indexMenu: [
      {img: youhuijuan, name: '优惠券', router: '/pages/myCoupon/index'},
      {img: barcode, name: '付款码', router: ''},
      {img: wodeyuyue, name: '我的预约', router: '/pages/myOrder/index'}
    ],
    showModal: false,
    radarImg: '',
    code: '',
    showMenu: false,
    task: null,
    close: false,
    showSettle: false
  }

  componentWillMount () {
  }

  componentDidMount () {
  }
  componentWillUnmount () {
    this.closeModal()
  }

  componentDidShow () {
    this.initPage()
    this.getLocation()
  }

  componentDidHide () {
    this.closeModal()
  }

  initPage () {
    const token = Taro.getStorageSync('token')
    const customer = Taro.getStorageSync('customer')
    if (!token) {
      console.log('go to trylogin')
      this.taroTryLogin()
    }else{
      this.props.dispatchAuthorized(true)
    }
    if (customer.mobile) {
      this.setState({
        showMenu: true
      })
    }
  }

  taroTryLogin (mySocket) {
    Taro.login().then(response => {
      console.log('微信登录成功', response);
      this.AuthTryLogin(response, mySocket)
    }).catch(err => {
      console.log('微信登录失败', err);
      this.taroHideLoading()
      Taro.showToast({
        title: '发生错误，请重试!',
        icon: 'none'
      })
    })
  }

  AuthTryLogin (response, mySocket) {
    const header = { 'applicationId': applicationId }
    header['content-type'] = 'application/json' // 默认值,
    Taro.request({
      url: API_AUTH_TRY_LOGIN,
      method: 'POST',
      data: {
        loginCode: response.code
      },
      header
    }).then((res) => {
      const { code, data, msg } = res.data
      if (code === CODE_SUCCESS) { // 成功
        Taro.setStorage({
          key: 'token',
          data: data && data.token
        }).then(() => {
          if (mySocket) mySocket()
        })
        Taro.setStorage({
          key: 'customer',
          data: data && data.customer
        })
        if (data && data.customer && data.customer.mobile) {
          this.setState({
            showMenu: true
          })
        }
        this.props.dispatchAuthorized(true)
      } else if (code === CODE_AUTH_EXPIRED) { // 授权登录
        this.props.dispatchAuthorized(false)
      } else {
        Taro.showToast({
          title: msg,
          icon: 'none'
        })
      }
    }).catch((err) => {
      console.log('没有授权', err)
      Taro.showToast({
        title: err && err.msg,
        icon: 'none'
      })
    })
  }


  getLocation = () => {
    Taro.getLocation().then(res => {
      this.props.dispatchSetLatLong(res)
      this.getIndexData(res)
    }).catch(res=>{
      console.log(res)
      this.props.dispatchSetLatLong({})
      this.getIndexData({})
    })
  }

  getIndexData = (res) => {
    Taro.showLoading({title: '加载中...', mask: true})
    this.props.dispatchIndexBanner({
      latitude: res.latitude || '',
      longitude: res.longitude || ''
     }).then((data) => {
      this.setState({
        banners: data
      })
      this.$setShareImageUrl = () => data[0].pictureUrl
    })
    this.props.dispatchIndexData({
      latitude: res.latitude || '',
      longitude: res.longitude || ''
     }).then((data) => {
      Taro.hideLoading()
      this.setState({
        homeInfo: data
      })
    })
    this.props.dispatchIndexShop({
      latitude: res.latitude || '',
      longitude: res.longitude || ''
     }).then((data) => {
      this.setState({
        shopData: data
      })
      this.setMenuData(data)
    })
  }

  setMenuData (shopData) {
    let selector = shopData.shops || []
    const { selectedShop } = this.props
    this.setState({
      selector: selector,
      selectorChecked: selectedShop || selector[0]
    })
  }


  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  mySocket = () => {
    Taro.getStorage({
      key: 'token'
    }).then(res => {
      this.connectSocket(res.data)
    }).catch(() => {
      console.log('获取token失败，无法链接socket')
    })
  }

  connectSocket = (token) => {
    Taro.connectSocket({
      url: `wss://${hostS}/websocket/${applicationId}?token=${token}`,
      success: function () {
        console.log('connect success')
      }
    }).then(task => {
      this.setState({
        task: task
      })
      task.onOpen(() => {
        console.log('onOpen')
        task.send({ data: '继续连接' })
      })
      task.onMessage((msg) => {
        let data = JSON.parse(msg.data)
        if (data.data && data.data.stage === 2) {
          this.props.dispatchSettleData(data.data)
          // 关闭条形码谈层 完了打开结算页面谈层
          this.setState({
            showModal: false,
            showSettle: true
          })
        } else if (data.code === 4001) {
          this.taroTryLogin(this.mySocket())
        }
        console.log('onMessage: ', data)
        // task.close()
      })
      task.onError(() => {
        console.log('onError')
      })
      task.onClose(e => {
        console.log(this.state.close)
        if (!this.state.close) {
          this.mySocket()
        }
        console.log('onClose: ', e)
      })
    })
  }

  payBarcode (isNew) {
    // Taro.showLoading()
    // this.props.dispatchPayBarcode().then(res => {
    //   console.log('res', res)
    //   Taro.hideLoading()
    //   if (!res) return
    //   if (!isNew) this.mySocket()
    //   this.setState({
    //     radarImg: res.path,
    //     code: res.code,
    //     showModal: true
    //   })
    // })
    if (!isNew) this.mySocket()
    const token = Taro.getStorageSync('token')
    let radarImg = `${API_IMAGE_PAY_BARCODE}?applicationId=${applicationId}&token=${token}`
    this.setState({
      radarImg: radarImg,
      showModal: true
    })
  }

  typeClick = item => {
    if (item.name === '付款码') {
      this.payBarcode()
    } else {
      Taro.navigateTo({
        url: item.router
      })
    }
  }

  closeSettle () {
    this.setState({
      close: true,
      showSettle: false,
    }, () => {
      this.state.task && this.state.task.close()
    })
  }

  closeModal = () => {
    this.setState({
      close: true,
      showModal: false
    }, () => {
      this.state.task && this.state.task.close()
    })
  }

  selectorChange = (selectIndex) => {
    const { selector } = this.state
    selector.map((item, index) => {
      if (index === Number(selectIndex)) {
        this.setState({
          selectorChecked: item
        })
        this.props.dispatchSelectedShop(item)
      }
    })
  }

  render () {
    const { settleData } = this.props.product
    const { homeInfo, shopData, radarImg, code, selector, selectorChecked, banners, indexMenu, showModal, showMenu, showSettle } = this.state
    let IndexModalRender = null
    if (showModal) IndexModalRender = (
      <IndexModal radarImg={radarImg} code={code} onPayBarcode={this.payBarcode.bind(this)} onCloseModal={this.closeModal.bind(this)} />
    )
    let SettlementRender = null
    if (showSettle) SettlementRender = (
      <Settlement settleData={settleData} onCloseSettle={this.closeSettle.bind(this)} />
    )
    return (
      <View className='index'>
        {banners && banners.length>0 ? (<ScrollView
          scrollY
          className='home__wrap'
          onScrollToLower={this.loadRecommend}
          style={{ height: getWindowHeight() }}
        >
          <View className='header-more'></View>
          <View className='header-banner'>
            <Banner list={banners} />
          </View>
          <Menu
            list={indexMenu}
            showMenu={showMenu}
            selector={selector}
            selectorChecked={selectorChecked}
            onSelectorChange={this.selectorChange.bind(this)}
            onTypeClick={this.typeClick.bind(this)}
          >
          </Menu>
          <IndexModule obj={homeInfo.hotGoods}></IndexModule>
          <IndexModule obj={homeInfo.newGoods}></IndexModule>
          <IndexModule obj={homeInfo.recommendGoods}></IndexModule>
          <IndexFooter obj={shopData}></IndexFooter>
          <View className='support'>由优联美业提供技术支持</View>
        </ScrollView>) : null}
        {IndexModalRender}
        {SettlementRender}
      </View>
    )
  }
}
