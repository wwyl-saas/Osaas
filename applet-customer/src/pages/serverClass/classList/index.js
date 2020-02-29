import Taro, { Component } from '@tarojs/taro'
import { View, Text, Image } from '@tarojs/components'
import './index.scss'

export default class ClassList extends Component {
  static defaultProps = {
    list: [],
    selected: 0
  }
  state = {
  }

  gotoClick (id) {
    Taro.navigateTo({
      url: `/pages/serverDetail/index?goodsId=${id}`
    })
  }

  render () {
    const { list, selected } = this.props
    let productList = list
    if (selected !== -1) productList = list[selected] && list[selected].goodsList || []
    return (
      <View className='classList'>
        {productList.map((item,index)=>(
            <View key={'project'+index} className='list-item' onClick={this.gotoClick.bind(this, item.id)}>
                <View className='img-box'>
                    <Image className='img' src={item.briefPicUrl} />
                </View>
                <View className='name'>
                    <Text>{item.name}</Text>
                </View>
                <View className='price'>
                    <Text className='newprice'>¥{item.counterPrice}</Text>
                    <Text className='oldPrice'>¥{item.marketPrice}</Text>
                </View>
            </View>
        ))}
      </View>
    )
  }
}
