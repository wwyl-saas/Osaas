import Taro, { Component } from '@tarojs/taro'
import {Button, Form, Image, ScrollView, Text, View} from '@tarojs/components'
import { connect } from '@tarojs/redux'
import * as actions from '../../actions/product'
import { getWindowHeight } from '../../utils/style'
import PayList from './payList'
import './index.scss'

@connect(state => state.product, { ...actions })
export default class OrderDetail extends Component {
  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '订单详情',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    orderItem: {}
  }

  componentWillMount () {
  }

  componentDidMount () {

  }

  componentWillUnmount () { }

  componentDidShow () {
    this.dispatchOrderDesc()
  }

  componentDidHide () { }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  dispatchOrderDesc () {
    Taro.showLoading()
    const params = this.$router.params
    this.props.dispatchOrderDesc({
      orderId: params.orderId
    }).then(data=>{
      this.setState({
        orderItem: data || {}
      }, () => {
        Taro.hideLoading()
      })
    })
  }

  cancelClick (item) {
    this.props.dispatchOrderClose({
      orderId: item.id
    }).then((data) =>{
      if (!data) return
      Taro.showToast({
        title: '取消成功',
        icon: 'success'
      }).then(()=>{
        Taro.navigateBack({ delta: 1 })
      })
    })
  }

  payClick (item, e) {
    this.props.dispatchPayConfirm({
      "formId": e.currentTarget.formId,
      "orderId": item.id
    }).then(data => {
      // "nonceStr": "string",
      // "packageValue": "string",
      // "pay": true,
      // "payId": 0,
      // "paySign": "string",
      // "signType": "string",
      // "timeStamp": "string",
      // "type": "余额支付"
      if (data.pay) {
        Taro.showToast({
          title: '余额支付完成',
          icon: 'success'
        }).then(()=>{
          Taro.navigateBack({ delta: 1 })
        })
        return
      }
      Taro.requestPayment({
        timeStamp: data.timeStamp,
        nonceStr: data.nonceStr,
        package: data.packageValue,
        signType: data.signType || 'MD5',
        paySign: data.paySign,
        success(res) {
          console.log(res)
          Taro.showToast({
            title: '支付完成',
            icon: 'success'
          }).then(()=>{
            Taro.navigateBack({ delta: 1 })
          })
        },
        fail(res) {
          console.log(res)
          Taro.showToast({
            title: '支付失败',
            icon: 'none'
          })
        }
      })
    })
  }

  render () {
    const { orderItem } = this.state
    return (
      <View className='orderDetail'>
        <ScrollView
          scrollY
          className='home__wrap'
          onScrollToLower={this.loadRecommend}
          style={{ height: getWindowHeight() }}
        >
          <View className='payList'>
            <View className='order'>
              <View className='orderNum'>
                <Text className='line'></Text>
                <Text className='name'>商品信息</Text>
              </View>
              <View className={'status' + ' ' + (orderItem.status === '代付款' ? 'red' : '')}>
                {orderItem.status}
              </View>
            </View>
            <View className='goods'>
              {orderItem.orderDescList && orderItem.orderDescList.map((item, index) => (
                <View
                  key={'good' + index}
                  className={'pro-item'+' '+(index===orderItem.orderDescList.length-1?'last-item':'')}
                >
                  <View className='left'>
                    <Image className='left-img' src={item.briefPicUrl} />
                  </View>
                  <View className='right'>
                    <View className='name'>
                      <Text>{item.name}</Text>
                    </View>
                    <View className='des'>
                      <Text>{item.goodsBrief}</Text>
                    </View>
                    <View className='num'>
                      <Text>x {item.goodsNum}</Text>
                    </View>
                  </View>
                </View>
              ))}
            </View>
            <View className='line'></View>
            <View className='pay_info'>
              <View className='info_item'>
                <View className='name'>商品合计</View>
                <View className='value'>¥{orderItem.orderSumAmount}</View>
              </View>
              <View className='info_item'>
                <View className='name'>优惠</View>
                <View className='value'>-¥{orderItem.couponAmount}</View>
              </View>
              <View className='info_item'>
                <View className='name'>实付</View>
                <View className='value'>¥{orderItem.payAmount}</View>
              </View>
              <View className='info_item'>
                <View className='name'>支付方式</View>
                <View className='value'>{orderItem.consumeType}</View>
              </View>
            </View>
            <View className='line'></View>
            <View className='pay_info2'>
              <View className='name'>服务时间：<Text className='value'>{orderItem.createTime}</Text></View>
              <View className='name'>订单编号：<Text className='value'>{orderItem.orderNo}</Text></View>
              <View className='name'>服务店铺：<Text className='value'>{orderItem.shopName}</Text></View>
            </View>
            <View className='line'></View>
            <View className='operation'>
              <View></View>
              <View className='btns'>
                {orderItem.status === '待支付' && <Button onClick={this.cancelClick.bind(this, orderItem)} className='cancleBtn'>
                  取消订单
                </Button>}
                {orderItem.status === '待支付' && <Form className='right submit' reportSubmit='true' onSubmit={this.payClick.bind(this, orderItem)}>
                  <Button formType='submit' className='sureBtn'>
                    去付款
                  </Button>
                </Form>}
                {orderItem.status === '已支付' && <Button onClick={this.goToHref.bind(this, orderItem)} className='sureBtn'>
                  评价
                </Button>}
              </View>
            </View>
          </View>
        </ScrollView>
      </View>
    )
  }
}
