import Taro, { Component } from '@tarojs/taro'
import { View, Text, Image } from '@tarojs/components'
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
            <View key={'list'+index} className={'pro-item' + ' ' + (index === list.length - 1 ? 'last-item' : '')}>
                <View className='left'>
                    <Image className='left-img' src={item.briefPicUrl} />
                </View>
                <View className='right'>
                    <View className='name'>
                        <Text>{item.name}</Text>
                        <Text className='price'>¥{item.counterPrice}</Text>
                    </View>
                    <View className='des'>
                        <Text>{item.goodsBrief}</Text>
                    </View>
                    <View className='num'>
                        <Text>X{item.goodsNum}</Text>
                    </View>
                </View>
            </View>
        ))}
      </View>
    )
  }
}
