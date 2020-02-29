import Taro, { Component } from '@tarojs/taro'
import { View, Text, ScrollView, Button } from '@tarojs/components'
import { getWindowHeight } from '../../utils/style'
import ProList from './proList'
import ProSet from './proSet'
import './index.scss'

export default class Payment extends Component {

  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '订单结算',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: false,
    proList: [
      {img: '', name: '剪发烫发套餐', price: '29.9', des: '价值29.9剪发+价值88.8烫发', num: 1},
      {img: '', name: '剪发烫发套餐', price: '29.9', des: '价值29.9剪发+价值88.8烫发', num: 1},
      {img: '', name: '剪发烫发套餐', price: '29.9', des: '价值29.9剪发+价值88.8烫发', num: 1},
      {img: '', name: '剪发烫发套餐', price: '29.9', des: '价值29.9剪发+价值88.8烫发', num: 1}
    ],
    setData: {
      allMoney: 0,
      coupon: 0,
      discount: 0,
      allNum: 0,
      realityMoney: 0
    },
    orderInfo: [
    ]
  }

  componentWillMount () { }

  componentDidMount () { }

  componentWillUnmount () { }

  componentDidShow () { }

  componentDidHide () { }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  render () {
    const { proList, setData, orderInfo } = this.state
    return (
      <View className='payment'>
            <ScrollView
              scrollY
              className='home__wrap'
              onScrollToLower={this.loadRecommend}
              style={{ height: getWindowHeight()}}
            >
                <View className='lists'>
                  <View className='title'>
                    <Text>商品信息</Text>
                    <Text className='status'>未付款</Text>
                  </View>
                  <ProList list={proList}></ProList>
                </View>
                <ProSet setData={setData}></ProSet>
                <View className='orderInfo'>
                  {orderInfo && orderInfo.map((item,index) => (
                    <View className='order-item' key={'info'+index}>
                      <Text className='name'>{item.name}</Text>
                      <Text className='value'>{item.value}</Text>
                    </View>
                  ))}
                </View>
            </ScrollView>
            <View className='payment-footer'>
                <Button className='cancle'>取消订单</Button>
                <Button className='pay'>去付款</Button>
            </View>
      </View>
    )
  }
}
