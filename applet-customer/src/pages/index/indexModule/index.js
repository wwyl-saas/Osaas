import Taro, { Component } from '@tarojs/taro'
import { View, Text, Image } from '@tarojs/components'
import './index.scss'

export default class IndexModule extends Component {
  static defaultProps = {
    obj: {}
  }
  constructor() {
    super(...arguments)
  }

  state = {
  }

  onScrollToUpper(e){
    console.log(e.detail)
  }

  onScroll(e){
    console.log(e.detail)
  }

  gotoClick (id) {
    Taro.navigateTo({
      url: `/pages/serverDetail/index?goodsId=${id}`
    })
  }

  render () {
    const { obj } = this.props
    if (!obj.img) return (<View></View>)
    const styleBackImg = {
      backgroundImage: `url(${obj.img})`
    }
    return (
      <View className='index-module'>
        <View className='module-item'>
                <View className='item-top' style={styleBackImg}>
                    <View className='top-title'>
                      <Text>{obj.name}</Text>
                    </View>
                    {obj.descArray && obj.descArray.map((item,index)=>(
                      <View key={'des'+index} className='des'>
                        <Text>- {item}</Text>
                      </View>
                    ))}
                </View>
                <View className='sw-box'>
                    <View className='sw-pro'>
                        {obj.goodsList && obj.goodsList.map((item2, index_item) => (
                            <View
                              key={'good'+index_item}
                              onClick={this.gotoClick.bind(this,item2.id)}
                              className='pro-item'
                            >
                                <Image className='pro-img' src={item2.briefPicUrl} />
                                <View className='name'>
                                    <Text>{item2.name}</Text>
                                </View>
                                <View className='price'>
                                    <Text className='money'>¥{item2.counterPrice}</Text>
                                    <Text>起</Text>
                                </View>
                            </View>
                        ))}
                    </View>
                </View>
            </View>
      </View>
    )
  }
}
