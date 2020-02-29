import Taro, {Component} from '@tarojs/taro'
import {View, ScrollView, Button, Image, Input} from '@tarojs/components'
import {AtTabs, AtTabsPane} from 'taro-ui'
import {connect} from '@tarojs/redux'
import * as actions from '@actions/users'
import {getScrollViewHeight} from '../../utils/style'
import CouponList from './couponList'
import CardList from './cardList'
import './index.scss'
import saoyisao from '../../assets/images/saoyisao.png'

@connect(state => state.user, {...actions})
export default class MyCoupon extends Component {
  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '我的卡券',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: true,
    current: 0,
    tabList: [{title: '优惠券'}, {title: '卡项'}],
    couponCode: '',
    myCoupon: {
      coupons: [],
      cards: []
    },
    myCoupon2: []
  }

  componentWillMount() {
  }

  componentDidMount() {
  }

  componentWillUnmount() {
  }

  componentDidShow() {
    this.dispatchCoupons()
  }

  componentDidHide() {
  }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  dispatchCoupons = () => {
    Taro.showLoading({title: '加载中...', mask: true})
    this.props.dispatchCoupons().then((res) => {
      if (!res) return
      this.setState({
        myCoupon: res,
        myCoupon2: res.coupons,
      }, () => {
        this.setState({
          loading: false
        })
      })
      Taro.hideLoading()
    })
  }

  handleClick(value) {
    const {myCoupon} = this.state
    let myCoupon2 = []
    myCoupon2 = value === 0 ? myCoupon.coupons : myCoupon.cards
    this.setState({
      current: value,
      myCoupon2: myCoupon2 || []
    })
  }

  couponsAdd = () => {
    const {couponCode} = this.state
    if (!couponCode) return
    this.props.dispatchCouponsAdd({
      couponCode: couponCode
    }).then((res) => {
      if (!res) return
      Taro.showToast({
        title: '兑换成功',
        icon: 'success'
      }).then(()=>{
        setTimeout(()=>{
          this.dispatchCoupons()
        }, 2000)
      })
    })
  }

  couponCodeChange(e) {
    this.setState({
      couponCode: e.detail.value
    })
    // 在小程序中，如果想改变 value 的值，需要 `return value` 从而改变输入框的当前值
    return e.detail.value
  }

  scanClick() {
    Taro.scanCode({
      onlyFromCamera: false,
      scanType: ['barCode', 'qrCode'],
      success(res) {
        console.log(res)
        // result	string	所扫码的内容
        // scanType	string	所扫码的类型
        // charSet	string	所扫码的字符集
        // path	string	当所扫的码为当前小程序二维码时，会返回此字段，内容为二维码携带的 path
        // rawData	string	原始数据，base64编码
      }
    })
  }

  render() {
    const {current, tabList, couponCode, myCoupon2, loading} = this.state
    let headerHeight = 90
    let footerHeight = 0
    if (current===1) headerHeight = 40
    return (
      <View className='myCoupon'>
        {current === 0 ? (<View className='couponFooter'>
          <Input
            className='f-ipt'
            type='number'
            placeholder='请输入兑换码'
            maxLength='15'
            placeholderStyle='color: #A1A1A1'
            value={couponCode}
            onChange={this.couponCodeChange.bind(this)}
          />
          <Button onClick={this.couponsAdd.bind(this)} className='f-btn'>兑换</Button>
          <Image onClick={this.scanClick.bind(this)} className='f-img' src={saoyisao} />
        </View>) : null}
        <View className={`at-header ${current === 0 ? 'at-header2' : ''}`}>
          <AtTabs
            current={current}
            tabList={tabList}
            onClick={this.handleClick.bind(this)}
          >
            {tabList.map((item, index) => (
              <AtTabsPane current={current} index={index} key={'tab' + index}>
                <ScrollView
                  scrollY
                  className='home__wrap'
                  onScrollToLower={this.loadRecommend}
                  style={{ height: getScrollViewHeight(headerHeight, footerHeight) }}
                >
                  {index === 0 ? (<CouponList list={myCoupon2}></CouponList>) : null}
                  {index === 1 ? (<CardList list={myCoupon2}></CardList>) : null}

                  {!loading?<View className='noData'>
                      {myCoupon2.length === 0 ? (
                      <View className='iconfont iconzu' style='font-size:80PX;padding-top: 40%; color:#e6e6e6'></View>) : (
                      <View className='iconfont iconmeiyougengduo' style='font-size:40PX;color:#dbdbdb'></View>)}
                  </View>:null}
                </ScrollView>
              </AtTabsPane>
            ))}
          </AtTabs>
        </View>
      </View>
    )
  }
}
