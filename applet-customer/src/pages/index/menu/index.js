import Taro, { Component } from '@tarojs/taro'
import { View, Text, Image, Picker } from '@tarojs/components'
import { AtIcon } from 'taro-ui'
import './index.scss'

export default class IndexMenu extends Component {
  static defaultProps = {
    list: [],
    selector: [],
    selectorChecked: '',
    showMenu: false
  }

  state = {
  }

  componentDidMount () {
  }

  onChange = e => {
    let index = e.detail.value
    this.props.onSelectorChange && this.props.onSelectorChange(index)
  }
  typeClick = item => {
    this.props.onTypeClick && this.props.onTypeClick(item)
  }

  render () {
    const { list, selector, selectorChecked, showMenu } = this.props
    return (
      <View className='index-menu'>
        <View className='iconfont iconmeiyougengduo'></View>

        <View className='select-shop'>
            <AtIcon value='map-pin' size='18' color='#C3BAA2'></AtIcon>
            <Picker mode='selector' range={selector} rangeKey='name' onChange={this.onChange.bind(this)}>
                <View className='picker'>
                    {selectorChecked.name}
                </View>
                <AtIcon value='chevron-down' size='18' color='#113665'></AtIcon>
            </Picker>
        </View>
        {showMenu?(<View className='select-type'>
          {list.map((item, index) => (
            <View key={index} className='type-item' onClick={this.typeClick.bind(this,item)}>
              <View className='type-img'>
                <Image
                  className='t-img'
                  src={item.img}
                />
              </View>
              <Text className='name'>{item.name}</Text>
            </View>
          ))}
        </View>):null}
      </View>
    )
  }
}
