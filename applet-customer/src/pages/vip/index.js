import Taro, {Component} from '@tarojs/taro'
import {View, Text, Image, Swiper, SwiperItem} from '@tarojs/components'
import {AtModal} from 'taro-ui'
import {connect} from '@tarojs/redux'
import * as actions from '@actions/users'
import './index.scss'
import vipBox from '../../assets/images/vip-box.png'

@connect(state => state.user, {...actions})
export default class Vip extends Component {

  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '会员福利',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: false,
    showModal: false,
    activeIndex: 0,
    payId: '',
    welfare: {}
  }

  componentWillMount() {
  }

  componentDidMount() {
  }

  componentWillUnmount() {
  }

  componentDidShow() {
    this.dispatchPersonWelfare()
  }

  componentDidHide() {
  }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  dispatchPersonWelfare () {
    this.props.dispatchPersonWelfare().then(res => {
      this.setState({
        welfare: res,
        activeIndex: res.currentLevel || 0
      })
    })
  }

  swChange = (e) => {
    this.setState({
      activeIndex: e.detail.current
    })
  }

  chargeButton = (chargeAmount) => {
    this.props.dispatchMemberCharge({
      chargeAmount: chargeAmount
    }).then(res => {
      const params = {
        timeStamp: res.timeStamp,
        nonceStr: res.nonceStr,
        package: res.packageValue,
        signType: res.signType,
        paySign: res.paySign
      }
      this.setState({payId: res.payId})
      Taro.requestPayment(params).then(res2 => {
        console.log(res2)
        this.handleConfirm()
      })
    })
  }

  cancelClick = () => {
    this.setState({
      showModal: !this.state.showModal
    })
  }

  handleConfirm = () => {
    this.props.dispatchQueryCharge({
      payId: this.state.payId
    }).then(res => {
      if (res) {
        this.dispatchPersonWelfare()
        Taro.showToast({
          title: '支付成功',
          icon: 'success',
          duration: 2000
        })
      }
    })
  }

  render() {
    const {welfare, activeIndex, showModal} = this.state
    const memberItem = welfare.detailDtoList && welfare.detailDtoList[activeIndex] || {}
    return (
      <View className='vip'>
        <AtModal
          isOpened={showModal}
          title=''
          cancelText='支付失败'
          confirmText='支付完成'
          onClose={this.cancelClick}
          onCancel={this.cancelClick}
          onConfirm={this.handleConfirm}
          content='您确认已经支付完成了吗？'
        />
        <Swiper
          className='v-sp'
          current={activeIndex}
          circular
          onChange={this.swChange.bind(this)}
          autoplay={false}
        >
          {welfare.detailDtoList && welfare.detailDtoList.map((item, index) => (
            <SwiperItem className={'s-item' + ' ' + (activeIndex === index ? 's-active' : '')} key={'s' + index}>
              <Image className='item-img' src={item.memberCardUrl} />
              <View className='card-msg'>
                <Text>{item.memberName}</Text>
                <Text className='cardTag'>{item.cardTag}</Text>
              </View>
            </SwiperItem>
          ))}
        </Swiper>
        <View className='vipEquity'>
          {memberItem.unlockedFares && memberItem.unlockedFares.map((item, index) => (
            <View className='e-item' key={'u' + index}>
              {item.unit !== '图' ?
                (<View className='e-name'><Text className='name-b'>{item.value * 10}</Text>{item.unit}</View>)
                : (<Image className='e-img' src={item.value} />)}
              <View className='name'>{item.name}</View>
            </View>
          ))}
          {memberItem.lockedFares && memberItem.lockedFares.map((item, index) => (
            <View className='e-item' key={'e' + index}>
              {item.unit !== '图' ?
                (<View className='e-name'><Text className='name-b'>{item.value * 10}</Text>{item.unit}</View>)
                : (<Image className='e-img' src={item.value} />)}
              <View className='name'>{item.name}</View>
            </View>
          ))}
        </View>
        {memberItem.couponFlag ? (<View className='gift-bag'>
          <Text className='title'>大礼包</Text>
          <View className='bag-list'>
            {memberItem.coupons && memberItem.coupons.map((item, index) => (
              <View key={'bag' + index} className='img-box'>
                <Image className='bag-img' src={vipBox} />
                <View className='gift'>
                  <View className='name'>{item.couponName} x {item.number}</View>
                  <View className='tag'>{item.tag}</View>
                  <View className='vipLogo'>VIP</View>
                </View>
              </View>
            ))}
          </View>
        </View>) : null}
        <View className='vip-content'>
          <View className='c-item'>
            <View className='title'>
              普通等级要求
              <Text className='flag'>{memberItem.levelConditionTag}</Text>
            </View>
            <View className='right'>
              下列条件满足一条既可
            </View>
          </View>
          {memberItem.levelCondition && memberItem.levelCondition.map((item, index) => (
            <View className='c-item' key={'c' + index}>
              <View className='left'>
                {item.name}
              </View>
              <View className='right'>
                {item.value}/{item.unit}
              </View>
            </View>
          ))}
        </View>
        {memberItem.chargeButton ? (
          <View className='vip-btn' onClick={this.chargeButton.bind(this, memberItem.chargeAmount)}>
            {memberItem.chargeButton}
          </View>) : null}
      </View>
    )
  }
}
