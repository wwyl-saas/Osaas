import Taro, {Component} from '@tarojs/taro'
import {View, Text, Image, ScrollView, Button, Picker, Form} from '@tarojs/components'
import {AtModal, AtModalHeader, AtModalContent, AtModalAction, AtIcon, AtInput} from 'taro-ui'
import {connect} from '@tarojs/redux'
import * as actions from '../../actions/product'
import {dispatchAuthorized} from '../../actions/users'
import {getWindowHeight} from '../../utils/style'
import {isPhone, futureDate, todayDate, tomorrowDate, toDateTime} from '../../utils/date'
import LOGO from '../../assets/images/logo.png'
import './index.scss'
import {API_AUTH_LOGIN, applicationId} from '../../constants/api'


const CODE_SUCCESS = 0

@connect(state => state.product, {...actions})
@connect(state => state.user, {dispatchAuthorized})
export default class Appointment extends Component {
  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '服务预约',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    isOpened: false,
    loading: false,
    startDate: todayDate(),
    endDate: futureDate(15),
    searchData: {
      arriveDate: '',	// 预约日期
      arriveTime: '',
      customerName: '',	// 预约人姓名
      goodsId: -1,	// 预约项目ID
      goodsName: '到店确认',
      merchantUserId: -1,	// 预约技师ID
      merchantUserName: '到店确认',
      phone: '', // 预约人手机号
      shopId: '', // 商铺ID
      shopName: '',
      shopPhone: '',
      isDetail: false
    },
    person: {
      avatar: '',
      cityId: 0,
      createTime: '',
      gender: '',
      id: '',
      lastLoginTime: '',
      merchantId: '',
      mobile: '',
      name: '',
      nickname: ''
    }
  }

  componentWillMount() {

  }

  componentDidMount() {

  }

  componentWillUnmount() {
  }

  componentDidShow() {
    this.initData()
    this.personInfo()
  }

  componentDidHide() {
  }

  personInfo () {
    this.props.dispatchPersonInfo().then((data=>{
      const {appointmentData} = this.props
      let { searchData } = this.state
      searchData.customerName = appointmentData.customerName || data.name || ''
      searchData.phone = appointmentData.phone || data.mobile || ''
      this.props.dispatchSetAppointmentData(searchData)
      this.setState({
        searchData: searchData
      })
    }))
  }

  initData () {
    const {indexShop, appointmentData, selectedShop} = this.props
    let item = indexShop.shops && indexShop.shops[0]
    let { searchData } = this.state
    searchData = appointmentData
    searchData.arriveDate = appointmentData.arriveDate || tomorrowDate()
    searchData.arriveTime = appointmentData.arriveTime || '10:00:00'
    searchData.shopId = selectedShop.id || item.id
    searchData.shopName = selectedShop.name || item.name
    searchData.shopPhone = selectedShop.telephone || item.telephone
    searchData.picture = item.picture
    const params = this.$router.params
    console.log('params', params)
    searchData.isDetail = params.detail?true:false || appointmentData.detail || searchData.detail
    searchData.merchantUserId = params.merchantUserId || appointmentData.merchantUserId || searchData.merchantUserId || -1
    searchData.merchantUserName = params.merchantUserName || appointmentData.merchantUserName || searchData.merchantUserName
    searchData.goodsId = params.goodsId || appointmentData.goodsId || searchData.goodsId || -1
    searchData.goodsName = params.goodsName || appointmentData.goodsName || searchData.goodsName
    this.props.dispatchSetAppointmentData(searchData)
    this.setState({
      searchData: searchData
    })
  }

  submitClick = (e) => {
    if (this.props.Authorized) {
      console.log('formId', e.detail.formId)
      const {searchData} = this.state
      const {appointmentData} = this.props
      if (!(searchData.arriveDate || appointmentData.arriveDate)) {
        Taro.showToast({
          title: '请选择预约到店日期',
          icon: 'none',
          duration: 1000
        })
        return
      }
      if (!(searchData.arriveTime || appointmentData.arriveTime)) {
        Taro.showToast({
          title: '请选择预约到店时间',
          icon: 'none',
          duration: 1000
        })
        return
      }
      let newDate = toDateTime(searchData.arriveDate || appointmentData.arriveDate, searchData.arriveTime || appointmentData.arriveTime)
      let now = new Date()
      if (now > newDate) {
        Taro.showToast({
          title: '请选择未来时间做为预约时间',
          icon: 'none'
        })
        return;
      }
      if (!(searchData.customerName || appointmentData.customerName)) {
        Taro.showToast({
          title: '请填写预约人姓名',
          icon: 'none'
        })
        return
      }
      if (!(searchData.phone || appointmentData.phone)) {
        Taro.showToast({
          title: '请填写预约人手机号',
          icon: 'none'
        })
        return
      }
      if (!isPhone(searchData.phone || appointmentData.phone)) {
        Taro.showToast({
          title: '请填写正确的手机号',
          icon: 'none'
        })
        return
      }
      Taro.showLoading({title: '加载中...', mask: true})
      let goodsId = searchData.goodsId || appointmentData.goodsId
      let merchantUserId = searchData.merchantUserId || appointmentData.merchantUserId
      this.props.dispatchAppointmentSearch({
        arriveDate: searchData.arriveDate || appointmentData.arriveDate,	// 预约日期
        arriveTime: searchData.arriveTime || appointmentData.arriveTime,
        customerName: searchData.customerName || appointmentData.customerName,	// 预约人姓名
        formId: e.detail.formId,	// 表单ID
        goodsId: goodsId == -1 ? '' : goodsId,	// 预约项目ID
        goodsName: searchData.goodsName || appointmentData.goodsName,
        merchantUserId: merchantUserId == -1 ? '' : merchantUserId,	// 预约技师ID
        phone: searchData.phone || appointmentData.phone, // 预约人手机号
        shopId: searchData.shopId || appointmentData.shopId, // 商铺ID
        shopName: searchData.shopName || appointmentData.shopName
      }).then((data) => {
        Taro.hideLoading()
        if (!data) return
        Taro.showToast({
          title: '预约成功,等待商家确认',
          icon: 'success'
        })
        this.props.dispatchSetAppointmentData({
          arriveDate: '',	// 预约日期
          arriveTime: '',
          customerName: '',	// 预约人姓名
          goodsId: -1,	// 预约项目ID
          goodsName: '到店确认',
          merchantUserId: -1,	// 预约技师ID
          merchantUserName: '到店确认',
          phone: '', // 预约人手机号
          shopId: '', // 商铺ID
          shopName: '',
          shopPhone: '',
          isDetail: false
        })
        if (searchData.isDetail) {
          Taro.navigateTo({
            url: `/pages/serverDetail/index?goodsId=${searchData.goodsId || appointmentData.goodsId}`
          })
        } else {
          Taro.switchTab({
            url: `/pages/index/index`
          })
        }
      })
    } else {
      this.setState({
        isOpened: true
      })
    }
  }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  onTimeChange = e => {
    let {searchData} = this.state
    searchData.arriveTime = e.detail.value + ':00'
    this.setState({
      searchData: searchData
    })
  }

  onDateChange = e => {
    let {searchData} = this.state
    searchData.arriveDate = e.detail.value
    this.setState({
      searchData: searchData
    })
  }

  customerNameChange(value) {
    let {searchData} = this.state
    searchData.customerName = value
    this.setState({
      searchData: searchData
    })
    // 在小程序中，如果想改变 value 的值，需要 `return value` 从而改变输入框的当前值
    return value
  }

  phoneChange(value) {
    let {searchData} = this.state
    searchData.phone = value
    this.setState({
      searchData: searchData
    })
    // 在小程序中，如果想改变 value 的值，需要 `return value` 从而改变输入框的当前值
    return value
  }

  gotoClick(router) {
    if (!this.state.searchData.shopId) return
    this.props.dispatchSetAppointmentData(this.state.searchData)
    Taro.navigateTo({
      url: router
    })
  }

  shopChange = e => {
    const {indexShop} = this.props
    let item = indexShop.shops && indexShop.shops[e.detail.value]
    this.props.dispatchSelectedShop(item)
    let {searchData} = this.state
    searchData.shopId = item.id
    searchData.shopName = item.name
    searchData.shopPhone = item.telephone
    searchData.picture = item.picture
    this.setState({
      searchData: searchData
    })
  }

  callPhone() {
    let {searchData} = this.state
    Taro.makePhoneCall({
      phoneNumber: searchData.shopPhone
    }).then(
    )
  }

  handleConfirm(res) {
    console.log(res)
    const detail = res.detail
    this.setState({
      isOpened: false
    })
    if (detail.encryptedData) {
      this.taroShowLoading()
      return this.taroLogin(detail)
    }
  }

  taroLogin(detail) {
    const self = this
    Taro.login().then(response => {
      console.log('微信登录成功', response);
      self.authLogin(response, detail)
    }).catch(err => {
      console.log('微信登录失败', err);
      self.taroHideLoading()
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
      self.taroHideLoading()
      const {code, data, msg} = res.data
      if (code === CODE_SUCCESS) { // 成功
        Taro.setStorage({
          key: 'token',
          data: data && data.token
        })
        Taro.setStorage({
          key: 'customer',
          data: data && data.customer
        })
        self.props.dispatchAuthorized(true)
        Taro.showToast({
          title: '授权登录成功',
          icon: 'none',
          duration: 1000
        })
      } else {
        Taro.showToast({
          title: msg,
          icon: 'none'
        })
      }
    }).catch(res => {
      self.taroHideLoading()
      console.log(res)
    })
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

  handleCancel() {
    this.setState({
      isOpened: false
    })
  }

  render() {
    const {searchData, isOpened, startDate, endDate} = this.state
    const {indexShop} = this.props
    let stylistRouter = `/pages/stylist/index?goodsId=${searchData.goodsId}&shopId=${searchData.shopId}`
    let serverProjectRouter = `/pages/serverProject/index?merchantUserId=${searchData.merchantUserId}&shopId=${searchData.shopId}`
    return (
      <View className='appointment'>
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
        <ScrollView
          scrollY
          className='home__wrap'
          onScrollToLower={this.loadRecommend}
          style={{height: getWindowHeight()}}
        >
          <View className='banner-box'>
            <Image className='img' src={searchData.picture} />
          </View>
          <View className='c-box'>
            <View className='shop'>
              <View className='left'>
                <View className='left-t'>{searchData.shopName}</View>
              </View>
              <View className='right'>
                <View className='right-l'>
                  <Picker mode='selector'
                    range={indexShop.shops}
                    rangeKey='name'
                    onChange={this.shopChange.bind(this)}
                  >
                    <Text className='title'>切换</Text>
                    <AtIcon value='chevron-down' size='16' color='#9A9A9A'></AtIcon>
                  </Picker>
                </View>
                <View className='right-r' onClick={this.callPhone.bind(this)}>
                  <AtIcon value='phone' size='18' color='#09bb07'></AtIcon>
                </View>
              </View>
            </View>
            <View className='info'>
              <View className='info-item'>
                <Text>到店日期</Text>
                <View className='item-picker'>
                  <Picker
                    mode='date'
                    start={startDate}
                    end={endDate}
                    onChange={this.onDateChange.bind(this)}
                  >
                    <View className='picker'>
                      {searchData.arriveDate}
                      <AtIcon value='calendar' size='16' color='#9E9E9E'></AtIcon>
                    </View>
                  </Picker>
                </View>
              </View>
              <View className='info-item'>
                <Text>到店时间</Text>
                <View className='item-picker'>
                  <Picker mode='time' start='09:00:00' end='21:30:00' onChange={this.onTimeChange.bind(this)}>
                    <View className='picker'>
                      {searchData.arriveTime}
                      <AtIcon value='clock' size='16' color='#9E9E9E'></AtIcon>
                    </View>
                  </Picker>
                </View>
              </View>
              <AtInput
                name='customerName'
                border={false}
                title='预约人姓名'
                maxLength='20'
                type='text'
                placeholder='请输入预约人称呼'
                value={searchData.customerName}
                onChange={this.customerNameChange.bind(this)}
              />
              <AtInput
                name='phone'
                border={false}
                title='预约人手机号'
                type='phone'
                placeholder='请输入预约人手机号'
                value={searchData.phone}
                onChange={this.phoneChange.bind(this)}
              />
            </View>
            <View className='styles' onClick={this.gotoClick.bind(this, stylistRouter)}>
              <Text>指定设计师</Text>
              <View className='value'>
                {searchData.merchantUserName}
                <AtIcon value='chevron-right' size='18' color='#9E9E9E'></AtIcon>
              </View>
            </View>
            <View className='styles project' onClick={this.gotoClick.bind(this, serverProjectRouter)}>
              <Text>预约服务项目</Text>
              <View className='value'>
                {searchData.goodsName}
                <AtIcon value='chevron-right' size='18' color='#9E9E9E'></AtIcon>
              </View>
            </View>
            <Form className='btns' reportSubmit='true' onSubmit={this.submitClick.bind(this)}>
              <Button className='btn' formType='submit'>提交预约</Button>
            </Form>
          </View>
        </ScrollView>
      </View>
    )
  }
}
