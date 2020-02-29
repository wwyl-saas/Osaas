import Taro, { Component } from '@tarojs/taro'
import { View, Text, Image, Swiper, SwiperItem } from '@tarojs/components'
import './index.scss'

export default class DetailInfo extends Component {
  static defaultProps = {
    info: {}
  }
  state = {
  }
  render () {
    const { info } = this.props
    return (
      <View className='detailInfo'>
        <View className='img-box'>
          <Swiper
            className='test-h'
            indicatorColor='#999'
            indicatorActiveColor='#333'
            circular
            indicatorDots
            autoplay
          >
          {info.primaryPics && info.primaryPics.map((item,index)=>(
            <SwiperItem key={'swip'+index}>
              <Image className='img' src={item} />
            </SwiperItem>
          ))}
          </Swiper>
        </View>
        <View className='info'>
            <View className='name'>
                <Text className='name-text'>
                    {info.goodsName}
                </Text>
            </View>
            <View className='des'>
                <Text className='des-text'>
                    {info.goodsDesc}
                </Text>
            </View>
            <View className='price-box'>
                <Text className='price-text'>
                    ¥{info.counterPrice}
                </Text>
                <Text className='s-text'>
                    会员价
                </Text>
                <Text className='old-text'>
                    ¥{info.marketPrice} 市场价
                </Text>
            </View>
            <View className='label'>
                {info.goodsTags && info.goodsTags.map((item,index)=>(
                    <Text key={'tag'+index} className='label-item'>
                        {item}
                    </Text>
                ))}
            </View>
        </View>
      </View>
    )
  }
}
