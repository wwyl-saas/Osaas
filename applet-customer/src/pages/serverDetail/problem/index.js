import Taro, { Component } from '@tarojs/taro'
import { View, Text } from '@tarojs/components'
import { AtIcon } from 'taro-ui'
import './index.scss'

export default class Problem extends Component {
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
      <View className='problem'>
        <View className='title'>
            <View className='t-left'>
              <Text className='line'></Text>
              <Text className='title-text'>
                  {'常见问题'}
              </Text>
            </View>
            <View className='t-right' onClick={this.loadMoreClick.bind(this)}>
              <Text className='r-text'>查看全部</Text>
              {info && info.length !== 1 ? (<AtIcon value={loadMore?'chevron-up':'chevron-down'} size='16' color='#C3BAA2'></AtIcon>):null}
            </View>
        </View>
        {info && info.map((item,index)=>(
            <View key={'problem'+index} className={'problem-item'+' '+(!loadMore&&index>0?'hide-item':'')}>
                <View className='info-top'>
                    <Text>{item.question}</Text>
                </View>
                <View className='content-box'>
                    <Text>{item.answer}</Text>
                </View>
            </View>
        ))}
      </View>
    )
  }
}
