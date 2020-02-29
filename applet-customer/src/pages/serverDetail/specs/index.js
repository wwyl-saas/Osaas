import Taro, { Component } from '@tarojs/taro'
import { View, Text, ScrollView } from '@tarojs/components'
import { AtIcon } from 'taro-ui'
import './index.scss'
import { listenerCount } from 'cluster';

export default class Specs extends Component {
  static defaultProps = {
    info: []
  }
  state = {
  }
  render () {
    const { info } = this.props
    return (
      <View className='specs'>
        <View className='title'>
            <Text className='line'></Text>
            <Text className='title-text'>
                {'商品规格'}
            </Text> 
        </View>
        {info.map((item,index)=>(
            <View key={'specs'+index} className='specs-item'>
                <Text className='name'>{item.name}</Text>
                <Text className='value'>{item.value}</Text>
            </View>
        ))}
      </View>
    )
  }
}
