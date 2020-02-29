import Taro, { Component } from '@tarojs/taro'
import { View, Text } from '@tarojs/components'
import { AtIcon } from 'taro-ui'
import './index.scss'

export default class Shop extends Component {
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

  callPhone (telephone) {
    Taro.makePhoneCall({
      phoneNumber: telephone
    }).then(
    )
  }

  mapClick (item) {
    const params = {
      latitude: item.latitude,
      longitude: item.longitude,
      scale: 18,
      name: item.name,
      address: item.address
    }
    Taro.openLocation(params).then()
  }

  render () {
    const { info } = this.props
    const { loadMore } = this.state
    return (
      <View className='shop'>
        <View className='title'>
            <View className='t-left'>
              <Text className='line'></Text>
              <Text className='title-text'>适用门店</Text>
            </View>
            <View className='t-right' onClick={this.loadMoreClick.bind(this)}>
              <Text className='r-text'>展示全部{info && info.length}家门店</Text>
              {info && info.length !== 1 ? (<AtIcon value={loadMore?'chevron-up':'chevron-down'} size='16' color='#C3BAA2'></AtIcon>):null}
            </View>
        </View>
        {info && info.map((item,index)=>(
            <View key={'shop'+index} className={'shop-item'+' '+(!loadMore&&index>0?'hide-item':'')}>
                <View className='left'>
                    <View className='name-box'>
                        <Text className='name'>{item.name}</Text>
                    </View>
                    <View className='address-box' onClick={this.mapClick.bind(this, item)}>
                      <AtIcon value='map-pin' size='15' color='#3399FF'></AtIcon>
                      <Text className='address'>{item.address}</Text>
                    </View>
                </View>
                <View className='right' onClick={this.callPhone.bind(this, item.telephone)}>
                    <Text className='line'></Text>
                    <AtIcon  value='phone' size='20' color='#09BB07'></AtIcon>
                </View>
            </View>
        ))}
      </View>
    )
  }
}
