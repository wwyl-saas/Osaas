import Taro, { Component } from '@tarojs/taro'
import {View, ScrollView} from '@tarojs/components'
import {connect} from "@tarojs/redux";
import * as actions from "../../actions/product"
import {getScrollViewHeight} from '../../utils/style'
import ProList from './proList'
import ProSet from './proSet'
import SetFooter from './setFooter'
import CouponList from '../myCoupon/couponList/index'
import './index.scss'

@connect(state => state.product, { ...actions })
export default class Settlement extends Component {
  static defaultProps = {
    settleData: {}
  }
  state = {
    loading: false,
    hasMore: true,
    changeData: {
      couponAmount: '',
      payAmount: '',
      change: true
    },
    showCoupon: false,
    couponId: ''
  }

  componentWillMount () { }

  componentDidMount () { }

  componentWillUnmount () { }

  componentDidShow () {
  }

  componentDidHide () { }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  dispatchPayConfirm (orderId, formId) {
    const self = this
    this.props.dispatchPayConfirm({
      formId: formId,
      orderId: orderId
    }).then(data => {
      if (data.pay) {
        Taro.showToast({
          title: '余额支付成功',
          icon: 'success'
        })
        setTimeout(()=>{
          self.props.onCloseSettle && self.props.onCloseSettle()
        }, 1500)
        return
      }
      Taro.requestPayment({
        timeStamp: data.timeStamp,
        nonceStr: data.nonceStr,
        package: data.packageValue,
        signType: data.signType || 'MD5',
        paySign: data.paySign,
        success () {
          Taro.showToast({
            title: '支付完成',
            icon: 'success'
          })
          setTimeout(()=>{
            self.props.onCloseSettle && self.props.onCloseSettle()
          }, 1500)
        },
        fail (res) {
          console.log(res)
          Taro.showToast({
            title: '支付失败',
            icon: 'none'
          })
          self.cancelClick()
          // setTimeout(()=>{
          //   self.props.onCloseSettle && self.props.onCloseSettle()
          // }, 1500)
        }
      })
    })
  }

  cancelClick () {
    this.props.dispatchPayAbandon({
      "orderId": this.props.settleData.orderId
    }).then(data => {
      if (!data) return
      this.props.onCloseSettle && this.props.onCloseSettle()
    })
  }

  selectCoupon (item) {
    this.props.dispatchOrderChangeCoupon({
      couponId: item.id,
      orderId: this.props.settleData.orderId
    }).then(data => {
      if (!data) return
      let { changeData } = this.state
      changeData.couponAmount = data.couponAmount
      changeData.payAmount = data.payAmount
      changeData.change = data.couponAmount!==0?true:false
      this.setState({
        couponId: item.id,
        changeData: changeData,
        showCoupon: false
      })
    })
  }

  changeClick () {
    this.setState({
      showCoupon: !this.state.showCoupon
    })
  }

  render () {
    const { settleData } = this.props
    const { changeData, showCoupon, couponId } = this.state
    return (
      <View className='settlement index-modal'>
        <View className='model-mask'></View>
        {showCoupon ? (<View className='modal-msg'>
          <View className='title'>请选择要使用的优惠券</View>
          <ScrollView
            scrollY
            className='home__wrap'
            onScrollToLower={this.loadRecommend}
            style={{ height: getScrollViewHeight(50, 60) }}
          >
            <CouponList
              list={settleData.coupons}
              couponId={!changeData.change? '' : (couponId || settleData.couponId)}
              onSelectCoupon={this.selectCoupon.bind(this)}
            >
            </CouponList>

          </ScrollView>
          <View className='noSelect' onClick={this.selectCoupon.bind(this, {id: ''})}>不使用优惠券</View>
        </View>) : (<View className='modal-msg'>
          <ScrollView
            scrollY
            className='home__wrap'
            onScrollToLower={this.loadRecommend}
            style={{ height: getScrollViewHeight(50, 60) }}
          >
            <ProList list={settleData.orderDescs || []}></ProList>
            <ProSet
              setData={settleData}
              changeData={changeData}
              onChangeClick={this.changeClick.bind(this)}
            >
            </ProSet>
          </ScrollView>
          <SetFooter
            setData={settleData}
            changeData={changeData}
            onCancelClick={this.cancelClick.bind(this)}
            onDispatchPayConfirm={this.dispatchPayConfirm.bind(this)}
          >
          </SetFooter>
        </View>)}
      </View>
    )
  }
}
