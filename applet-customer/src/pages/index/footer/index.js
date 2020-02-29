import Taro, { Component } from '@tarojs/taro'
import { View, Text, Image, Button, Swiper, SwiperItem } from '@tarojs/components'
import { AtIcon } from 'taro-ui'
import wechat from '../../../assets/images/wechat.png'
import './index.scss'

export default class IndexFooter extends Component {
  static defaultProps = {
    obj: {}
  }

  state = {
    currentSwipe: 0
  }
  swiperChange = (e) => {
    this.setState({
      currentSwipe: e.detail.current
    })
  }
  callPhone (phone) {
    Taro.makePhoneCall({
      phoneNumber: phone
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

  handleContact (e) {
    console.log(e.path)
    console.log(e.query)
  }

  render () {
    const { obj } = this.props
    const { currentSwipe } = this.state
    if (!obj.shops) return (<View></View>)
    const tipRender = (<Text className='tip'>离我最近</Text>)
    return (
      <View className='index-footer'>
        <Image className='footer-img' src={obj.shops && obj.shops[currentSwipe].picture}/>
        <View className='footer-card'>
            <View className='title'>
                <Text>{obj.name}</Text>
            </View>
            <View className='info'>
                <Text>{obj.desc}</Text>
            </View>
            <View className='time'>
                <AtIcon value='clock' size='16' color='#DCD5C9'></AtIcon>
                <Text className='time-text'>营业时间 {obj.workTime}</Text>
            </View>
            <View className='shop-list'>
              <Swiper
                className='sw-list'
                onChange={this.swiperChange.bind(this)}
                current='0'
                circular
                autoplay={false}
              >
                {obj.shops && obj.shops.map((item,index) => (
                  <SwiperItem key={'foot'+index} className={'shop-item'+' '+(currentSwipe===index?'s-active':'')}>
                    <View className='item-card'>
                      <View className='top'>
                        <Text className='shop'>{item.name}</Text>
                        {item.ifNearest?tipRender:''}
                      </View>
                      <View className='c-msg' onClick={this.callPhone.bind(this, item.telephone)}>
                        <AtIcon value='phone' size='16' color='#09BB07'></AtIcon>
                        <Text className='msg-icon'>{item.telephone}</Text>
                      </View>
                      <View className='c-msg' onClick={this.mapClick.bind(this, item)}>
                        <AtIcon value='map-pin' size='16' color='#3399FF'></AtIcon>
                        <Text className='msg-icon'>{item.address}</Text>
                      </View>
                    </View>
                  </SwiperItem>
                ))}
              </Swiper>
            </View>
            <View className='btn-box'>
              <Button className='btn' open-type='contact' onbindcontact={this.handleContact.bind(this)}>
                <Image className='wechat' src={wechat} />
                联系客服
              </Button>
            </View>
        </View>
      </View>
    )
  }
}
