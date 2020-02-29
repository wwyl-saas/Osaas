import Taro, { Component } from '@tarojs/taro'
import { View, Text, ScrollView } from '@tarojs/components'
import { AtIcon } from 'taro-ui'
import './index.scss'
import { listenerCount } from 'cluster';

export default class Detail extends Component {
  static defaultProps = {
    info: []
  }
  state = {
    loadMore: false
  }
  loadMoreClick = () => {
    let loadMore = this.state.loadMore
    this.setState({
      loadMore: !loadMore
    })
  }
  render () {
    const { info } = this.props
    const { loadMore } = this.state
    return (
      <View className='detail'>
        <View className='title'>
            <View className='t-left'>
              <Text className='line'></Text>
              <Text className='title-text'>
                  {'服务详情'}
              </Text>
            </View>
        </View>
        {info && info.map((item,index)=>(
            // <View key={'detail'+index} className={'detail-item' + ' ' + (index === 0 ? 'first' : '')}>
            //     <Image className='img' src={item} />
            // </View>
            <View key={'detail'+index} className='detail-item'>
                <Image className='img' src={item} />
            </View>
        ))}
      </View>
    )
  }
}
