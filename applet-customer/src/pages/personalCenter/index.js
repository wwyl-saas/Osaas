import Taro, {Component} from '@tarojs/taro'
import {View, Text, Image, Navigator, Button} from '@tarojs/components'
import {AtButton, AtModal, AtModalHeader, AtModalContent, AtModalAction} from 'taro-ui'
import {connect} from '@tarojs/redux'
import * as actions from '@actions/users'
import './index.scss'
import daifukuan from '../../assets/images/daifukuan.png'
import daiquhuo from '../../assets/images/daiquhuo.png'
import daipingjia from '../../assets/images/daipingjia.png'
import yiwancheng from '../../assets/images/yiwancheng.png'
import youhuijuan from '../../assets/images/youhuijuan.png'
import fukuanma from '../../assets/images/fukuanma.png'
import wodeyuyue from '../../assets/images/wodeyuyue.png'
import wodefuli from '../../assets/images/wodefuli.png'
import wodezhangdan from '../../assets/images/wodezhangdan.png'
import lianxikefu from '../../assets/images/lianxikefu.png'
import yijianfankui from '../../assets/images/yijianfankui.png'
import LOGO from '../../assets/images/logo.png'
import {API_AUTH_LOGIN, applicationId} from '../../constants/api'

const CODE_SUCCESS = 0

@connect(state => state.user, {...actions})
export default class PersonalCenter extends Component {
  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '个人中心',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    isOpened: false,
    person: {},
    customer: {},
    statusList: [
      {icon: daifukuan, name: '待支付', router: '/pages/serviceRecords/index?status=0'},
      {icon: daiquhuo, name: '已取消', router: '/pages/serviceRecords/index?status=1'},
      {icon: daipingjia, name: '待评价', router: '/pages/serviceRecords/index?status=2'},
      {icon: yiwancheng, name: '已完成', router: '/pages/serviceRecords/index?status=3'}
    ],
    myBill: [
      {icon: wodezhangdan, cName: 'box0', router: '/pages/vipBill/index', name: '我的账单'}
    ],
    mySelect: [
      {icon: youhuijuan, cName: 'box1', router: '/pages/myCoupon/index', name: '我的卡劵'},
      {icon: lianxikefu, cName: 'box2', router: '', name: '联系客服'},
      {icon: fukuanma, cName: 'box3', router: '/pages/payOther/index', name: '代人支付'},
      {icon: wodefuli, cName: 'box4', router: '/pages/vip/index', name: '会员福利'},
      {icon: wodeyuyue, cName: 'box5', router: '/pages/myOrder/index', name: '我的预约'}
    ],
    feedback: [
      {icon: yijianfankui, cName: 'box6', router: '/pages/feedback/index', name: '意见反馈'}
    ],
  }

  componentWillMount() {
  }

  componentDidMount() {
  }

  componentWillUnmount() {
  }

  componentDidShow() {
    this.loadPersonData()
  }

  componentDidHide() {
  }

  loadPersonData(Authorized, defaultCustomer) {
    if (Authorized || this.props.Authorized) {
      const customer = defaultCustomer || Taro.getStorageSync('customer')
      this.setState({
        customer: customer || {}
      })
      this.props.dispatchPersonIndex().then(res => {
        if (res.memberId !== undefined) {
          this.setState({
            person: res
          })
        } else {
          this.props.dispatchPluginParam({
            cardId: res.wxMemberCardId
          }).then(result => {
            this.setState({
              params: result
            })
          }).catch(e => {
            console.log(e)
          })
        }
      }).catch(res => {
        console.log(res)
      })
    }
  }

  taroShowLoading() {
    Taro.showLoading({
      title: '加载中...',
      mask: true,
    }).then(res => console.log(res))
  }

  taroHideLoading() {
    Taro.hideLoading()
  }

  gotoClick(router) {
    if (!this.props.Authorized) {
      Taro.showToast({
        title: '请先登录授权',
        icon: 'none'
      })
      return
    }
    if (!this.state.person.memberId) {
      Taro.showToast({
        title: '请先领取会员卡',
        icon: 'none'
      })
      return
    }
    if (!router) return
    Taro.navigateTo({
      url: router
    })
  }

  handleContact(e) {
    console.log(e.path)
    console.log(e.query)
  }

  navigatePluginFail(e) {
    console.log('navigatePluginFail', e)
  }

  handleLogin() {
    this.setState({
      isOpened: true
    })
  }

  handleConfirm(res) {
    const detail = res.detail
    if (detail.encryptedData) {
      this.taroShowLoading()
      this.taroLogin(detail)
    }
    this.setState({
      isOpened: false
    })
  }

  taroLogin(detail) {
    Taro.login().then(response => {
      console.log('微信登录成功', response);
      this.authLogin(response, detail)
    }).catch(err => {
      console.log('微信登录失败', err);
      this.taroHideLoading()
      Taro.showToast({
        title: '发生错误，请重试!',
        icon: 'none'
      })
    })
  }

  authLogin(response, detail) {
    const self = this
    const header = {'applicationId': applicationId}
    header['content-type'] = 'application/json' // 默认值,
    Taro.request({
      url: API_AUTH_LOGIN,
      method: 'POST',
      data: {
        "encryptedData": detail.encryptedData,
        "iv": detail.iv,
        "loginCode": response.code,
        "rawData": detail.rawData,
        "signature": detail.signature
      },
      header
    }).then(res => {
      const {code, data, msg} = res.data
      self.taroHideLoading()
      if (code === CODE_SUCCESS) { // 成功
        Taro.setStorage({
          key: 'token',
          data: data && data.token
        })
        Taro.setStorage({
          key: 'customer',
          data: data && data.customer
        })
        this.props.dispatchAuthorized(true)
        this.loadPersonData(true, data && data.customer)
      } else {
        Taro.showToast({
          title: msg,
          icon: 'none'
        })
      }
    })
  }

  handleCancel() {
    this.setState({
      isOpened: false
    })
  }

  render() {
    const {Authorized} = this.props
    const {isOpened, person, customer, statusList, myBill, mySelect, feedback, params} = this.state
    const colors = ['#559aed', '#9d9e9f', '#f0c673', '#8d87b3', '#2c2f34']
    const styleBackImg = {
      background: (person.memberLevel || person.memberLevel===0) && colors[person.memberLevel] || '#9d9063'
      // backgroundImage: `http://cdn.wanwuyoulian.com/member${person.memberLevel + 1}.png`
    }
    return (
      <View className='personalCenter'>
        <View className='loginModel'>
          <AtModal isOpened={isOpened} className='model'>
            <AtModalHeader className='modelHeader'>授权登录</AtModalHeader>
            <AtModalContent className='modelContent'>
              <Image className='logo' src={LOGO} />
              <Text className='tip'>请授权小程序使用您的微信头像等信息</Text>
            </AtModalContent>
            <AtModalAction className='modelAction'>
              <Button className='button' onClick={this.handleCancel.bind(this)}>暂不登录</Button>
              <Button
                className='button'
                openType='getUserInfo'
                onGetUserInfo={this.handleConfirm.bind(this)}
                lang='zh_CN'
              >
                立即登录
              </Button>
            </AtModalAction>
          </AtModal>
        </View>
        <View className='info-box'>
          <View className='info'>
            <View className='left'>
              <Image className='img' src={customer.avatar || 'http://cdn.wanwuyoulian.com/portrait%20%282%29.png'} />
              {Authorized ? <Text className='name'>{person.name || customer.nickname}</Text> :
                <AtButton onClick={this.handleLogin.bind(this)} className='name'>您还未登录，请点击登录</AtButton>}
            </View>
            {Authorized ? <View className='right'>
              <View className='balance r-child'>
                <Text className='bottom'>余额:</Text>
                <Text className='top'>{person.balance || 0}</Text>

              </View>
              <View className='integral r-child'>
                <Text className='bottom'>积分:</Text>
                <Text className='top'>{person.consumeScore || 0}</Text>
              </View>
            </View> : null}
          </View>
          <View className='an'>
            {Authorized ? <View>
              {person.memberId ? (<View
                className='vip vip_pop'
                onClick={this.gotoClick.bind(this, '/pages/vip/index')}
                style={styleBackImg}
              >
                <Text>{person.memberLevelName || ''}</Text>
                <Text>{person.memberId || ''}</Text>
              </View>) : (
                <Navigator
                  className={'vip' + ' ' + (params?'vip_pop':'')}
                  backgroundColor='#9d9063'
                  target='miniProgram'
                  app-id='wxeb490c6f9b154ef9'
                  extra-data={params}
                  open-type='navigate'
                  onSuccess={this.navigatePluginFail.bind(this)}
                  onFail={this.navigatePluginFail.bind(this)}
                >
                  <Text>点击免费领取会员卡成为VIP会员</Text>
                </Navigator>)}
            </View> : null}
          </View>
        </View>
        <View className='statusOrder'>
          {statusList && statusList.map((item, index) => (
            <View className='statu-item' key={'status' + index} onClick={this.gotoClick.bind(this, item.router)}>
              <Image className='icon' src={item.icon} />
              <Text className='name'>{item.name}</Text>
            </View>
          ))}
        </View>
        <View className='myBill per-pro'>
          {myBill && myBill.map((item, index) => (
            <View className='m-item' key={'myBill' + index} onClick={this.gotoClick.bind(this, item.router)}>
              <View className={'icon-box' + ' ' + item.cName}>
                <Image className='icon' src={item.icon} />
              </View>
              <Text className='name'>{item.name}</Text>
            </View>
          ))}
        </View>
        <View className='mySelect per-pro'>
          {mySelect && mySelect.map((item, index) => (
            <View key={'mySelect' + index}>
              {item.name === '联系客服' ? (<AtButton
                className='m-item'
                openType='contact' onContact={this.handleContact.bind(this)}
              >
                <View className='m-item'>
                  <View className={'icon-box' + ' ' + item.cName}>
                    <Image className='icon' src={item.icon} />
                  </View>
                  <Text className='name'>{item.name}</Text>
                </View>
              </AtButton>) : (<View className='m-item' onClick={this.gotoClick.bind(this, item.router)}>
                <View className={'icon-box' + ' ' + item.cName}>
                  <Image className='icon' src={item.icon} />
                </View>
                <Text className='name'>{item.name}</Text>
              </View>)}
            </View>
          ))}
        </View>
        <View className='feedback per-pro'>
          {feedback && feedback.map((item, index) => (
            <View className='m-item' key={'feedback' + index} onClick={this.gotoClick.bind(this, item.router)}>
              <View className={'icon-box' + ' ' + item.cName}>
                <Image className='icon' src={item.icon} />
              </View>
              <Text className='name'>{item.name}</Text>
            </View>
          ))}
        </View>
      </View>
    )
  }
}

