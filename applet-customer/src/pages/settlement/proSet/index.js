import Taro, { Component } from '@tarojs/taro'
import { View, Text } from '@tarojs/components'
import { AtIcon } from 'taro-ui'
import './index.scss'

export default class ProSet extends Component {
  static defaultProps = {
    setData: {},
    changeData: {}
  }

  changeClick () {
    const { setData } = this.props
    if (setData.coupons && setData.coupons.length !==0) {
      this.props.onChangeClick && this.props.onChangeClick()
    }
  }

  render () {
    const { setData, changeData } = this.props
    let couponAmount = changeData.couponAmount || (changeData.couponAmount ===0?'0':'') || setData.couponAmount
    return (
      <View className='proSet'>
        <View className='set-item'>
            <Text>商品合计</Text>
            <Text className='allMoney'>¥{setData.sumAmount}</Text>
        </View>
        <View className='set-item' onClick={this.changeClick.bind(this)}>
            <Text>优惠券</Text>
            <View>
                {(setData.usableCoupons===0) ? (<Text className='coupon'>-¥0</Text>) : (<Text className='coupon'>
                  {changeData.change?(`-¥${couponAmount}`):(`${setData.usableCoupons}张可用`)}
                </Text>)}
                {setData.coupons && setData.coupons.length!==0?(<AtIcon value='chevron-right' size='16' color='#9A9A9A'></AtIcon>):null}
            </View>
        </View>
        <View className='set-item'>
            <Text>专享折扣</Text>
            <Text className='discount'>-¥{setData.discountAmount}</Text>
        </View>
        <View className='set-item'>
            <Text>实付</Text>
            <Text className='allMoney'>¥{changeData.payAmount || setData.payAmount}</Text>
        </View>
      </View>
    )
  }
}
