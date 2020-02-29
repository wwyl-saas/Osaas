import Taro, { Component } from '@tarojs/taro'
import { View, Text, Image} from '@tarojs/components'
import { AtIcon, AtRate } from 'taro-ui'
import './index.scss'

export default class Evaluate extends Component {
  static defaultProps = {
    info: [],
    commentNum: 0,
    goodsId: ''
  }
  state = {
  }

  loadMoreClick = () => {
    Taro.navigateTo({
      url: `/pages/evaluateAll/index?goodsId=${this.props.goodsId || ''}`
    })
  }

  render () {
    const { info, commentNum } = this.props
    return (
      <View>
        {info.list && info.list.length > 0 ? (<View className='evaluate'>
          <View className='title'>
            <View className='t-left'>
              <Text className='line'></Text>
              <Text className='title-text'>
                {info.title}
              </Text>
            </View>
            <View className='t-right' onClick={this.loadMoreClick.bind(this)}>
              <Text className='r-text'>查看全部{commentNum}条评论</Text>
              <AtIcon value='chevron-right' size='20' color='#C3BAA2'></AtIcon>
            </View>
          </View>
          {info.list && info.list.map((item,index)=>(
            <View key={'evaluate'+index} className={'evaluate-item'+' '+(index>1?'hide-item':'')}>
              <View className='info-top'>
                <View className='top-left'>
                  <Image className='avatar' src={item.avatar} />
                  <View className='left-r'>
                    <Text>{item.customerName}</Text>
                    <Text className='time'>{item.createTime}</Text>
                  </View>
                </View>
                <View className='top-right'>
                  <AtRate value={item.grade/10} size='13' />
                  {item.grade/10}分
                </View>
              </View>
              <View className='content-box'>
                <Text>{item.remark}</Text>
              </View>
              <View className='pictureList'>
                {item.pictureList && item.pictureList.map((item2,index2)=>(
                  <Image className='picture' key={'img'+index2} src={item2}></Image>
                ))}
              </View>
              <View className='userinfo'>
                  <Text>— 此次服务由{item.merchantUserTitle}{item.merchantUserName}提供 —</Text>
              </View>
            </View>
          ))}
        </View>): null}
      </View>
    )
  }
}
