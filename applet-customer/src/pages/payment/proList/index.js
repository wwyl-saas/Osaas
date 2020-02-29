import Taro, { Component } from '@tarojs/taro'
import { View, Swiper, SwiperItem, Image } from '@tarojs/components'
import './index.scss'

export default class ProList extends Component {
  static defaultProps = {
    list: []
  }

  render () {
    const { list } = this.props
    return (
      <View className='proList'>
        {list.map((item,index) => (
            <View key={index} className={'pro-item' + ' ' + (index === list.length - 1 ? 'last-item' : '')}>
                <View className='left'>
                    <Image className='left-img' src={item.img} />
                </View>
                <View className='right'>
                    <View className='name'>
                        <Text>{item.name}</Text>
                        <Text className='price'>¥{item.price}</Text>
                    </View>
                    <View className='des'>
                        <Text>{item.des}</Text>
                    </View>
                    <View className='num'>
                        <Text>X{item.num}</Text>
                    </View>
                </View>
            </View>
        ))}
      </View>
    )
  }
}
