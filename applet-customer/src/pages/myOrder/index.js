import Taro, { Component } from '@tarojs/taro'
import {View, Text, Button, ScrollView} from '@tarojs/components'
import { AtIcon, AtModal } from 'taro-ui'
import { connect } from '@tarojs/redux'
import * as actions from '../../actions/product'
import './index.scss'
import {getWindowHeight} from "../../utils/style";

@connect(state => state.product, { ...actions })
export default class MyOrder extends Component {
  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '我的预约',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: true,
    showModal: false,
    id: '',
    list: []
  }

  componentWillMount () { }

  componentDidMount () { }

  componentWillUnmount () { }

  componentDidShow () {
    this.dispatchAppointmentMy()
  }

  componentDidHide () { }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  dispatchAppointmentMy () {
    Taro.showLoading({title: '加载中...', mask: true})
    this.props.dispatchAppointmentMy({}).then((res) => {
      this.setState({
        loading: false,
        list: res
      })
      Taro.hideLoading()
    })
  }

  callPhone (phone) {
    Taro.makePhoneCall({
      phoneNumber: phone
    }).then(
    )
  }

  cancleClick = (id) => {
    this.setState({
      showModal: !this.state.showModal,
      id: id || ''
    })
  }

  handleConfirm = () => {
    Taro.showLoading({title: '', mask: true})
    this.props.dispatchAppointmentCancle({
      appointmentId: this.state.id
    }).then(() => {
      Taro.hideLoading()
      this.setState({
        showModal: !this.state.showModal,
      }, () => {
        Taro.showToast({
          title: '取消成功！',
          icon: 'none'
        }).then(()=>{
          this.dispatchAppointmentMy()
        })
      })
    })
  }

  render () {
    const { list, showModal, loading } = this.state
    return (
      <View className='myOrder'>
        <AtModal
          isOpened={showModal}
          title=''
          cancelText='取消'
          confirmText='确认'
          onClose={this.cancleClick}
          onCancel={this.cancleClick}
          onConfirm={this.handleConfirm}
          content='您确认取消该预约订单吗？'
        />
        <ScrollView
          scrollY
          className='home__wrap'
          onScrollToLower={this.loadRecommend}
          style={{ height: getWindowHeight() }}
        >
          {list && list.map((item,index)=>(
              <View className='order-item' key={'order'+index}>
                <View className='item-line'>
                    <Text className='name'>预约到店日期</Text>
                    <Text className='value'>{item.arriveDate}</Text>
                </View>
                <View className='item-line'>
                    <Text className='name'>预约到店时间</Text>
                    <Text className='value'>{item.arriveTimeStart}-{item.arriveTimeEnd}</Text>
                </View>
                <View className='item-line'>
                    <Text className='name'>预约技师</Text>
                    <Text className='value'>{item.merchantUserName}</Text>
                </View>
                <View className='item-line'>
                    <Text className='name'>服务项目</Text>
                    <Text className='value'>{item.goodsName}</Text>
                </View>
                <View className='item-line'>
                    <Text className='name'>服务门店</Text>
                    <View className='value'>
                        {item.shopName}
                        <AtIcon onClick={this.callPhone.bind(this, item.shopPhone)} className='phone' value='phone' size='15' color='#C3BAA2'></AtIcon>
                    </View>
                </View>
                <View className='item-last'>
                    <Button onClick={this.cancleClick.bind(this, item.id)} className='btn'>取消</Button>
                </View>
              </View>
          ))}
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
