import Taro, {Component} from '@tarojs/taro'
import {View, Text, ScrollView} from '@tarojs/components'
import {AtTabs, AtTabsPane} from 'taro-ui'
import {connect} from '@tarojs/redux'
import * as actions from '@actions/users'
import {getWindowHeight} from '../../utils/style'
import './index.scss'

@connect(state => state.user, {...actions})
export default class VipBill extends Component {

  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '会员账单',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: true,
    current: 0,
    tabList: [{title: '全部'}, {title: '消费'}, {title: '充值'}],
    list: []
  }

  componentWillMount() {
  }

  componentDidMount() {
  }

  componentWillUnmount() {
  }

  componentDidShow() {
    this.dispatchPersonBill()
  }

  componentDidHide() {
  }

  dispatchPersonBill(type) {
    Taro.showLoading({title: '加载中...', mask: true})
    this.props.dispatchPersonBill({
      type: type || 0
    }).then(res => {
      this.setState({
        list: res,
        loading: false
      })
      Taro.hideLoading()
    })
  }

  handleClick(value) {
    console.log(value)
    this.dispatchPersonBill(value)
    this.setState({
      current: value
    })
  }

  render() {
    const {current,loading, tabList, list} = this.state
    return (
      <View className='vipBill'>
        <AtTabs
          current={current}
          tabList={tabList}
          onClick={this.handleClick.bind(this)}
        >
          {tabList.map((item, index) => (
            <AtTabsPane current={current} index={index} key={'tab' + index}>
              <ScrollView
                scrollY
                className='home__wrap'
                style={{ height: getWindowHeight()}}
              >
                <View className='bill'>
                  {list && list.map((item2, listIndex) => (
                    <View className='bill-item' key={'bill' + listIndex}>
                      <View className='left'>
                        <Text className='l-top'>{item2.dateTime}</Text>
                        <Text className='l-bottom'>{item2.goodsDesc}</Text>
                      </View>
                      <View className='right'>
                        <Text className={'r-top' + ' ' + (item2.flag === 2 ? 'blue' : '')}>
                          {item2.flag === 2 ? '+' : '-'} ¥
                          {item2.amount}
                        </Text>
                        <Text className='r-bottom'>{item2.settleDesc}</Text>
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
            </AtTabsPane>
          ))}

        </AtTabs>

      </View>
    )
  }
}
