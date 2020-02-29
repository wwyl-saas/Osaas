import Taro, { Component } from '@tarojs/taro'
import { View, Swiper, SwiperItem, Image } from '@tarojs/components'
import './index.scss'

export default class Banner extends Component {
  static defaultProps = {
    list: []
  }

  gotoClick (link) {
    console.log(link)
    Taro.navigateTo({
      url: link
    })
  }

  render () {
    const { list } = this.props
    return (
      <View className='home-banner'>
        <Swiper
          className='home-banner__swiper'
          circular
          autoplay
          indicatorDots
          indicatorActiveColor='rgb(178, 42, 49)'
          // TODO 目前 H5、RN 暂不支持 previousMargin、nextMargin
          // previousMargin
          // nextMargin
        >
          {list.map(item => (
            <SwiperItem
              key={'banner'+item.id}
              className='home-banner__swiper-item'
              onClick={this.gotoClick.bind(this,item.link)}
            >
              <Image
                className='home-banner__swiper-item-img'
                src={item.pictureUrl}
              />
            </SwiperItem>
          ))}
        </Swiper>
      </View>
    )
  }
}
