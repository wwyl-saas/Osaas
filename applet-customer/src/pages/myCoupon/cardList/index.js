import Taro, { Component } from '@tarojs/taro'
import { View, Text } from '@tarojs/components'
import './index.scss'

export default class CardList extends Component {
  static defaultProps = {
    list: []
  }

  render () {
    const { list } = this.props
    return (
      <View className='cardList'>
        {list && list.map((item,index)=>(
            <View className='card' key={'card'+index}>
                <View className='tip'>
                    {item.tag}
                    <View className='arrow-left'></View>
                </View>
                <View className='name'>{item.name}</View>
                <View className='time'>有效期至：{item.endTime}</View>
                <View className='num'>
                    <Text className='enjoyNum'>有效次数：{item.effectNum}</Text>
                    <Text className='surplusNum'>剩余次数：{item.residueNum}</Text>
                </View>
            </View>
        ))}
      </View>
    )
  }
}
