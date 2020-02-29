import Taro, { Component } from '@tarojs/taro'
import { View, Text, Button, Image } from '@tarojs/components'
import { connect } from '@tarojs/redux'
import {API_AUTH_TRY_LOGIN,API_AUTH_LOGIN, applicationId} from '../../constants/api'
import LOGO from '../../assets/images/logo.png'
import './index.scss'

const CODE_SUCCESS = 0
const CODE_AUTH_EXPIRED = 4002

@connect(state => state.user)
export default class Loading extends Component {


  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '加载中...',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white'
  }
  constructor (props) {
    super(props)
    this.state = {
      loading: true
    }
    // 异步函数需要在constructor中指定this
    this.authLogin = this.authLogin.bind(this)
  }

  componentWillMount () {
    this.taroShowLoading()

    // 需要授权登录跳转过来情况
    const params = this.$router.params
    if (params.loading&&params.loading === 2) {
      this.setState({
        loading: false
      })
      this.taroHideLoading()
    }else{
      //首次进入尝试trylogin
      this.checkLogin()
    }

  }

  componentDidMount () {
  }

  componentWillUnmount () { }

  componentDidShow () {}

  componentDidHide () { }

  taroShowLoading () {
    Taro.showLoading({
        title: '加载中...',
        mask: true,
      }).then(res => console.log(res))
  }

  taroHideLoading () {
    Taro.hideLoading()
  }

  tobegin = (res) => {
    console.log(res)
    const detail = res.detail
    if (!detail.encryptedData){
      Taro.switchTab({
        url: '/pages/index/index'
      })
    } else{
      this.setState({
        loading: true
      })
      this.taroShowLoading()
      return this.taroLogin(detail)
    }
  }



  taroLogin (detail) {
    const self=this
    Taro.login().then(response => {
      console.log('微信登录成功', response);
      self.authLogin(response,detail)
    }).catch(err => {
      console.log('微信登录失败', err);
      self.taroHideLoading()
      Taro.showToast({
        title: '发生错误，请重试!',
        icon: 'none'
      })
    })
  }

  authLogin (response, detail) {
    const self=this
    const header = { 'applicationId': applicationId }
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
      self.taroHideLoading()
      const { code, data, msg } = res.data
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
        Taro.switchTab({
          url: '/pages/index/index'
      })
      }else{
        Taro.showToast({
          title: msg,
          icon: 'none'
        })
      }
    })
  }


  checkLogin () {
      const token = Taro.getStorageSync('token')
      if (token) {
        Taro.switchTab({
          url: '/pages/index/index'
        })
        this.taroHideLoading()
      } else {
        this.taroTryLogin()
      }
  }



  taroTryLogin () {
    const self=this
    Taro.login().then(response => {
      console.log('微信登录成功', response);
      self.AuthTryLogin(response)
    }).catch(err => {
      console.log('微信登录失败', err);
      self.taroHideLoading()
      Taro.showToast({
        title: '发生错误，请重试!',
        icon: 'none'
      })
    })
  }

  AuthTryLogin (response) {
    const self=this
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
        Taro.switchTab({
          url: '/pages/index/index'
      })
      } else if (code === CODE_AUTH_EXPIRED) { // 授权登录
        self.setState({
          loading: false
        })
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
  render () {
    const { loading } = this.state
    return (
      <View className='loading'>
        {!loading?(<View className='authorizationRender'>
            <Image className='logo' src={LOGO} />
            <Text className='tip'>请授权优联造型使用您的微信头像等用户信息</Text>
            <Button className='btn' openType='getUserInfo' onGetUserInfo={this.tobegin.bind(this)} type='primary' lang='zh_CN'>用户授权</Button>
        </View>):null}
      </View>
    )
  }
}
