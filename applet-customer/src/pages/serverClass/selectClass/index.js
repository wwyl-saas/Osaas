import Taro, { Component } from '@tarojs/taro'
import { View, Text, Image, ScrollView } from '@tarojs/components'
import './index.scss'

export default class SelectClass extends Component {
  static defaultProps = {
    list: [],
    selected: 0
  }

  state = {
    windowWidth: 375,
    navScrollLeft: 0
  }

  componentDidMount () {
    Taro.onWindowResize((res) => {
        this.setState({
            windowWidth: res.windowWidth
        })
    })
  }

  selectClick = (index) => {
    //每个tab选项宽度占1/5
    const singleNavWidth = this.state.windowWidth / 5;
    //tab选项居中
    this.setState({
      navScrollLeft: (index - 2) * singleNavWidth
    })
    this.props.onSelectClick && this.props.onSelectClick(index)
  }

  render () {
    const { navScrollLeft } = this.state
    const { list, selected } = this.props
    return (
      <View className='selectClass'>
        <ScrollView
          className='scrollview'
          scrollX
          scrollLeft={navScrollLeft}
          scrollWithAnimation
        >
        {list.map((item,index)=>(
            <View
              key={index}
              className={'class-item'+' '+(index == selected ? 'active' : '')}
              onClick={this.selectClick.bind(this,index)}
            >
                <Image className='class-img' src={index == selected ? item.iconOn : item.icon} />
                <Text className='class-name'>{item.name}</Text>
            </View>
        ))}
      </ScrollView>
      </View>
    )
  }
}
