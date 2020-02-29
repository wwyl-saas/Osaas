import Taro, {Component} from '@tarojs/taro'
import {View, Text, Image} from '@tarojs/components'
import {AtIcon} from 'taro-ui'
import './index.scss'
import proClass from '../../../assets/images/proClass.png'

export default class ProjectList extends Component {
  static defaultProps = {
    list: [],
    appointmentData: {}
  }
  state = {
    selectId: -1
  }

  componentDidMount() {
    const {appointmentData} = this.props
    this.setState({
      selectId: appointmentData.goodsId || -1
    })
  }

  selectProject = (item) => {
    if (item.match === false) return
    this.setState({
      selectId: item.id
    }, () => {
      let router = `/pages/appointment/index?goodsId=-1&goodsName=${'到店确认'}`
      if (item.id !== -1) {
        router = `/pages/appointment/index?goodsId=${item.id}&goodsName=${item.name}`
      }
      Taro.reLaunch({
        url: router
      })
    })
  }

  render() {
    const {list} = this.props
    const {selectId} = this.state
    return (
      <View className='projectList'>
        <View
          className={'top-tip project' + ' ' + (selectId == -1 ? 'active' : '')}
          onClick={this.selectProject.bind(this, {id: -1})}
        >
          <View className='img-box'>
            <Image className='img' src={proClass} />
          </View>
          <View className='content'>
            <View className='name'>
              <Text>不选择预约项目</Text>
            </View>
            <View className='des'>
              <Text>到店确认</Text>
            </View>
          </View>
          <View className='triangle_border_nw'>
            <View className='checkIcon'>
              <AtIcon value='check' size='15' color='#fff'></AtIcon>
            </View>
          </View>
        </View>
        {list && list.map((item, index) => (
          <View
            className={'project' + ' ' + (item.id === selectId ? 'active' : '') + ' ' + (item.match ? '' : 'gray')}
            key={'project' + index}
            onClick={this.selectProject.bind(this, item)}
          >
            <View className='img-box'>
              <Image className='img' src={item.briefPicUrl} />
            </View>
            <View className='content'>
              <View className='name'>
                <Text className='goodsName'>{item.name}</Text>
                <View className='label'>
                  {item.isRecommend ? (<Text className='labelImg color2'>荐</Text>) : null}
                  {item.isNew ? (<Text className='labelImg color1'>新</Text>) : null}
                  {item.isHot ? (<Text className='labelImg color0'>热</Text>) : null}
                </View>
              </View>
              <View className='des'>
                <Text>{item.goodBrief}</Text>
              </View>
              <View className='sold'>
                <Text className='price'>¥{item.counterPrice}</Text>
                <Text className='oldPrice'>¥{item.marketPrice}</Text>
                <Text className='soldNum'>已售{item.sellVolume}</Text>
              </View>
            </View>
            {item.id === selectId ? (<View className='triangle_border_nw'>
              <View className='checkIcon'>
                <AtIcon value='check' size='15' color='#fff'></AtIcon>
              </View>
            </View>) : null}
          </View>
        ))}
      </View>
    )
  }
}
