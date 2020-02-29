import Taro, {Component} from '@tarojs/taro'
import {View, ScrollView} from '@tarojs/components'
import {AtTabs, AtTabsPane} from 'taro-ui'
import {connect} from '@tarojs/redux'
import * as actions from '../../actions/product'
import {getWindowHeight} from '../../utils/style'
import ProjectList from './projectList'
import './index.scss'

@connect(state => state.product, {...actions})
export default class ServerDeatil extends Component {

  /**
   * 指定config的类型声明为: Taro.Config
   *
   * 由于 typescript 对于 object 类型推导只能推出 Key 的基本类型
   * 对于像 navigationBarTextStyle: 'black' 这样的推导出的类型是 string
   * 提示和声明 navigationBarTextStyle: 'black' | 'white' 类型冲突, 需要显示声明类型
   */
  config = {
    navigationBarTitleText: '预约项目',
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#242424',
    navigationBarTextStyle: 'white',
    backgroundColor: '#F7F9FB'
  }

  state = {
    loading: true,
    current: 0,
    tabList: [],
    project: []
  }

  componentWillMount() {
  }

  componentDidMount() {
  }

  componentWillUnmount() {
  }

  componentDidShow() {
    this.dispatchAppointmentServer()
  }

  componentDidHide() {
  }

  loadRecommend = () => {
    if (!this.state.hasMore || this.state.loading) {
      return
    }
  }

  dispatchAppointmentServer () {
    Taro.showLoading({title: '加载中...', mask: true})
    const params = this.$router.params
    if (params.merchantUserId == -1) params.merchantUserId = ''
    this.props.dispatchAppointmentServer({
      merchantUserId: params.merchantUserId || '', // 技师Id
      shopId: params.shopId
    }).then((res) => {
      let tabList = res.map(item => {
        return {title: item.categoryName, id: item.categoryId}
      })
      this.setState({
        loading: false,
        tabList: tabList,
        project: res
      }, () => {
        Taro.hideLoading()
      })
    })
  }

  handleClick(value) {
    this.setState({
      current: value
    })
  }

  render() {
    const {tabList, current, project, loading} = this.state
    const {appointmentData} = this.props
    return (
      <View className='serverProject'>
        <ScrollView
          scrollY
          className='home__wrap'
          onScrollToLower={this.loadRecommend}
          style={{height: getWindowHeight()}}
        >
          <AtTabs
            current={current}
            scroll
            tabList={tabList}
            onClick={this.handleClick.bind(this)}
          >
            {tabList.map((item, index) => (
              <AtTabsPane current={current} index={index} key={'tab' + index}>
                <ProjectList appointmentData={appointmentData} list={project[current] && project[current].goodsList} />
              </AtTabsPane>
            ))}
          </AtTabs>
          {project[current] &&!loading?<View className='noData'>
              {project[current].goodsList.length === 0 ? (
                <View className='iconfont iconzu' style='font-size:80PX;padding-top: 40%; color:#e6e6e6'></View>) : (
                <View className='iconfont iconmeiyougengduo' style='font-size:40PX;color:#dbdbdb'></View>)}
            </View>:null}
        </ScrollView>
      </View>
    )
  }
}
