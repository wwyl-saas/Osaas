import Taro, { Component } from '@tarojs/taro'
import { View, Text, Image } from '@tarojs/components'
import './index.scss'
import selected1 from '../../../assets/images/selected1.png'

export default class CouponList extends Component {
  static defaultProps = {
    list: [],
    couponId: ''
  }

  selectClick (item) {
    if (item.match===true) {
      this.props.onSelectCoupon && this.props.onSelectCoupon(item)
    }
  }

  render () {
    const { list, couponId } = this.props
    return (
      <View className='couponList'>
        {list && list.map((item,index)=>(
          <View
            className={'coupon-item' + ' ' + (item.match===false?'gray':'')}
            key={'coupon'+index}
            onClick={this.selectClick.bind(this, item)}
          >
            {item.id === couponId?(<Image className='selected1' src={selected1} />):null}
            <View className='msg'>
                <View className='msg-left'>
                    <View className='left-img'>¥</View>
                    <View className='left-info'>
                        <Text className='name'>{item.name}</Text>
                        <Text className='time'>有效期至：{item.endTime}</Text>
                    </View>
                </View>
                <View className='msg-right'>会员专享</View>
            </View>
            <View className='tip'>{item.description}</View>
            {(item.match===false && item.reasons) ? (
              <View className='match_reason'>
                <View className='title'>不可用原因：</View>
                {item.reasons.map((item2, index2)=>(
                  <View key={'reasons' + index2} className='reason'>
                    {item2}
                  </View>
                ))}
              </View>
            ) : null}
          </View>
        ))}
      </View>
    )
  }
}
