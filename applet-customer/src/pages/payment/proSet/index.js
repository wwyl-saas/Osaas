import Taro, { Component } from '@tarojs/taro'
import { View, Text } from '@tarojs/components'
import { AtIcon } from 'taro-ui'
import './index.scss'

export default class ProSet extends Component {
  static defaultProps = {
    setData: {}
  }

  render () {
    const { setData } = this.props
    return (
      <View className='proSet'>
        <View className='set-item'>
            <Text>商品合计</Text>
            <Text className='allMoney'>¥{setData.allMoney}</Text>
        </View>
        <View className='set-item'>
            <Text>优惠</Text>
            <Text className='discount'>-¥{setData.coupon}</Text>
        </View>
        <View className='set-item'>
            <Text>实付</Text>
            <Text className='discount'>-¥{setData.discount}</Text>
        </View>
      </View>
    )
  }
}
