import Taro, { Component } from '@tarojs/taro'
import { View, Text, Image, ScrollView } from '@tarojs/components'
import { AtIcon } from 'taro-ui'
import { connect } from '@tarojs/redux'
import * as actions from '../../actions/product'
import { getWindowHeight } from '../../utils/style'
import './index.scss'
import proClass from '../../assets/images/proClass.png'

@connect(state => state.product, { ...actions })
export default class Stylist extends Component {

  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '设计师',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: true,
    selectId: -1,
    list: []
  }

  componentWillMount () { }

  componentDidMount () { }

  componentWillUnmount () { }

  componentDidShow () {
    this.dispatchAppointmentHair()
  }

  componentDidHide () { }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  dispatchAppointmentHair () {
    Taro.showLoading()
    const { appointmentData } = this.props
    const params = this.$router.params
    if (params.goodsId == -1) params.goodsId = ''
    this.props.dispatchAppointmentHair({
      goodsId: params.goodsId || '',
      shopId: params.shopId
    }).then((res) => {
      this.setState({
        loading: false,
        list: res || [],
        selectId: appointmentData.merchantUserId || -1
      }, () => {
        Taro.hideLoading()
      })
    })
  }

  selectProject = (item) => {
    if(item.match===false) return
    this.setState({
      selectId: item.merchantUserId
    }, () => {
      let router = '/pages/appointment/index?merchantUserId=-1&merchantUserName=到店确认'
      if (item.merchantUserId !== -1) {
        router = `/pages/appointment/index?merchantUserId=${item.merchantUserId}&merchantUserName=${item.name}`
      }
      Taro.reLaunch({
        url: router
      })
    })
  }

  render () {
    const { list, selectId, loading } = this.state
    return (
      <View className='stylist'>
            <ScrollView
              scrollY
              className='home__wrap'
              onScrollToLower={this.loadRecommend}
              style={{ height: getWindowHeight()}}
            >
                <View className='projectList'>
                  <View
                    className={'top-tip project'+' '+(selectId==-1?'active':'')}
                    onClick={this.selectProject.bind(this, {merchantUserId: -1})}
                  >
                      <View className='img-box'>
                          <Image className='img' src={proClass} />
                      </View>
                      <View className='content'>
                          <View className='title'>
                              <Text>不选择服务设计师</Text>
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
                  {list && list.map((item,index)=>(
                      <View
                        className={'project'+' '+(item.merchantUserId===selectId?'active':'')+' '+(item.match?'':'gray')}
                        key={'project'+index}
                        onClick={this.selectProject.bind(this, item)}
                      >
                          <View className='img-box'>
                              <Image className='img' src={item.portrait} />
                          </View>
                          <View className='content'>
                              <View className='name'>
                                  <Text>{item.name}</Text>
                                  <View className='grade'>
                                    <AtIcon value={item.favorableRate>0?'star-2':'star'} size='15' color='#C9C090'></AtIcon>
                                    <AtIcon value={item.favorableRate>10?'star-2':'star'} size='15' color='#C9C090'></AtIcon>
                                    <AtIcon value={item.favorableRate>20?'star-2':'star'} size='15' color='#C9C090'></AtIcon>
                                    <AtIcon value={item.favorableRate>30?'star-2':'star'} size='15' color='#C9C090'></AtIcon>
                                    <AtIcon value={item.favorableRate>40?'star-2':'star'} size='15' color='#C9C090'></AtIcon>
                                  </View>
                              </View>
                              <View className='position'>
                                  <Text>{item.title}</Text>
                              </View>
                              <View className='info'>
                                  <Text>{item.introduce}</Text>
                              </View>
                          </View>
                          <View className='triangle_border_nw'>
                              <View className='checkIcon'>
                                  <AtIcon value='check' size='15' color='#fff'></AtIcon>
                              </View>
                          </View>
                      </View>
                  ))}
                </View>
             {!loading?<View className='noData'>
              {list.length === 0 ? (
                <View className='iconfont iconzu' style='font-size:80PX;padding-top: 40%; color:#e6e6e6'></View>) : (
                <View className='iconfont iconmeiyougengduo' style='font-size:40PX;color:#dbdbdb'></View>)}
            </View>:null}
            </ScrollView>
      </View>
    )
  }
}
